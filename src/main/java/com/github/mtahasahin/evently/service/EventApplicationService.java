package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.domainevent.EventApplicationCancelledEvent;
import com.github.mtahasahin.evently.domainevent.GoingEventEvent;
import com.github.mtahasahin.evently.dto.DisplayEventDto;
import com.github.mtahasahin.evently.dto.EventApplicationDto;
import com.github.mtahasahin.evently.dto.EventQuestionAnswerDto;
import com.github.mtahasahin.evently.dto.EventQuestionDto;
import com.github.mtahasahin.evently.entity.EventApplication;
import com.github.mtahasahin.evently.entity.EventQuestionAnswer;
import com.github.mtahasahin.evently.exception.*;
import com.github.mtahasahin.evently.mapper.EventMapper;
import com.github.mtahasahin.evently.mapper.EventQuestionAnswerMapper;
import com.github.mtahasahin.evently.mapper.EventQuestionMapper;
import com.github.mtahasahin.evently.mapper.UserMapper;
import com.github.mtahasahin.evently.repository.EventApplicationRepository;
import com.github.mtahasahin.evently.repository.EventRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RequiredArgsConstructor
@Service
public class EventApplicationService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventApplicationRepository eventApplicationRepository;
    private final EventMapper eventMapper;
    private final EventQuestionMapper eventQuestionMapper;
    private final EventQuestionAnswerMapper eventQuestionAnswerMapper;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher applicationEventPublisher;


    public EventApplicationDto getAnswer(UUID userId, String eventSlug, UUID applicationId) {
        var application = eventApplicationRepository.findByEventSlugAndId(eventSlug,applicationId)
                .orElseThrow(() -> new EventApplicationNotFoundException(applicationId));

        if(!application.getEvent().getOrganizer().getId().equals(userId)){
            throw new CustomAccessDeniedException("User is not organizer of event");
        }

        if(!application.getEvent().getSlug().equals(eventSlug)){
            throw new CustomAccessDeniedException("Application does not belong to event");
        }

        return convertToDto(application);
    }

    public Page<EventApplicationDto> getAnswers(UUID userId, String eventSlug, int page, boolean fetchAll) {
        var event = eventRepository.findBySlug(eventSlug).orElseThrow(() -> new EventNotFoundException(eventSlug));
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));

        if(!user.isOrganizing(event)){
            throw new CustomAccessDeniedException("User is not organizer of event");
        }

        Pageable pageable = PageRequest.of(page, 5);
        var applications = fetchAll ?
                eventApplicationRepository.findAllByEventIdOrderByCreatedDateDesc(event.getId(), pageable) :
                eventApplicationRepository.findAllByEventIdAndLastModifiedDateGreaterThanOrConfirmedIsFalseOrderByCreatedDateDesc(event.getId(), LocalDateTime.now().minusHours(1), pageable);

        List<EventApplicationDto> result = new ArrayList<>();
        applications.getContent().forEach((application) -> result.add(convertToDto(application)));

        return new PageImpl<>(result,pageable,applications.getTotalElements());
    }

    public DisplayEventDto applyToEvent(UUID userId, String eventSlug, Set<EventQuestionAnswerDto> answers) {
        var event = eventRepository.findBySlug(eventSlug).orElseThrow(() -> new EventNotFoundException(eventSlug));
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));

        if(event.getEndDate().atZone(ZoneId.of(event.getTimezone())).isBefore(java.time.ZonedDateTime.now())) {
            throw new EventIsAlreadyOverException();
        }

        if(user.isJoiningEvent(event) || user.isWaitingForApprovalForEvent(event) || user.isOrganizing(event)){
            throw new UserHasAlreadyAppliedToEventException();
        }

        var eventApplication = EventApplication.builder().event(event).applicant(user).confirmed(!event.isApprovalRequired()).build();

        event.addEventApplication(eventApplication);

        event.getEventQuestions().forEach(eventQuestion -> {
            var answer = answers.stream().filter(a -> a.getQuestionId().equals(eventQuestion.getId())).findFirst().orElse(null);
            if(eventQuestion.isRequired() && (answer == null || answer.getAnswer().isBlank())) {
                throw new CustomValidationException(new ApiResponse.ApiSubError(eventQuestion.getId().toString(), "Answer is required"));
            }
            if(answer != null) {
                eventQuestion.getAnswers().add(new EventQuestionAnswer(null, eventQuestion, eventApplication, answer.getAnswer()));
            }
        });

        eventRepository.saveAndFlush(event);
        return eventMapper.eventToDisplayEventDto(event, user);
    }

    @Transactional
    public void confirmApplication(UUID userId, String slug, UUID applicationId) {
        var event = eventRepository.findBySlug(slug).orElseThrow(() -> new EventNotFoundException(slug));
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));

        if(!user.isOrganizing(event)){
            throw new CustomAccessDeniedException("User is not organizer of event");
        }

        var application = eventApplicationRepository.findById(applicationId).orElseThrow(() -> new EventApplicationNotFoundException(applicationId));

        if(!application.getEvent().equals(event)){
            throw new CustomAccessDeniedException("Application does not belong to event");
        }

        application.setConfirmed(true);

        eventApplicationRepository.saveAndFlush(application);
        applicationEventPublisher.publishEvent(new GoingEventEvent(application.getApplicant().getId(), event.getId()));
    }

    private EventApplicationDto convertToDto(EventApplication application) {
        var dto = new EventApplicationDto();
        dto.setId(application.getId());
        dto.setApproved(application.isConfirmed());
        dto.setCreatedAt(application.getCreatedDate());
        dto.setUpdatedAt(application.getLastModifiedDate());

        var questions = eventQuestionMapper.eventQuestionEntityToDto(application.getEvent().getEventQuestions());
        questions.sort(Comparator.comparing(EventQuestionDto::getOrder));
        dto.setQuestions(questions);

        var answers = eventQuestionAnswerMapper.entityToEventQuestionAnswerDto(application.getAnswers());
        dto.setAnswers(answers);

        var applicant = userMapper.userToUserLightDto(application.getApplicant());
        dto.setUser(applicant);
        return dto;
    }

    @Transactional
    public void cancelEventApplication(UUID userId, String eventSlug) {
        var event = eventRepository.findBySlug(eventSlug).orElseThrow(() -> new EventNotFoundException(eventSlug));
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));
        var eventApplication = event.getEventApplications().stream().filter(e -> e.getApplicant().equals(user)).findFirst().orElseThrow(() -> new EventApplicationNotFoundException("User has not applied to the event"));
        event.getEventApplications().remove(eventApplication);
        eventRepository.saveAndFlush(event);
        applicationEventPublisher.publishEvent(new EventApplicationCancelledEvent(userId, event.getId()));
    }
}
