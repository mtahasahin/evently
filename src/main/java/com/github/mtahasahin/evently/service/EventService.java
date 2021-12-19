package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.domainevent.EventCreatedEvent;
import com.github.mtahasahin.evently.domainevent.EventDeletedEvent;
import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.dto.DisplayEventDto;
import com.github.mtahasahin.evently.dto.EventDto;
import com.github.mtahasahin.evently.dto.EventQuestionDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.Event;
import com.github.mtahasahin.evently.entity.EventQuestion;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.exception.*;
import com.github.mtahasahin.evently.mapper.EventMapper;
import com.github.mtahasahin.evently.mapper.EventQuestionMapper;
import com.github.mtahasahin.evently.repository.EventRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.mtahasahin.evently.util.ImageUtils.extensions;

@RequiredArgsConstructor
@Service
public class EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventQuestionMapper eventQuestionMapper;
    private final S3BucketStorageService s3BucketStorageService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public DisplayEventDto createEvent(Long userId, CreateUpdateEventForm form) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found: " + userId));

        Event event = new Event();
        return saveEvent(form, user, event);
    }

    @Transactional
    public DisplayEventDto updateEvent(Long userId, String eventSlug, CreateUpdateEventForm form) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found: " + userId));

        Event event = eventRepository.findBySlug(eventSlug)
                .orElseThrow(() -> new EventNotFoundException("event not found: " + eventSlug));

        if (!event.getOrganizer().getId().equals(userId)) {
            throw new AccessDeniedException("forbidden");
        }

        return saveEvent(form, user, event);
    }

    public DisplayEventDto getEvent(Long userId, String eventSlug, String key) {
        AppUser user = userRepository.findById(userId)
                .orElse(null);

        Event event = eventRepository.findBySlug(eventSlug)
                .orElseThrow(() -> new EventNotFoundException("event not found: " + eventSlug));

        if (!(user != null && (user.isOrganizing(event) || user.isJoiningEvent(event))) && event.getVisibility() == EventVisibility.ONLY_WITH_LINK && !Objects.equals(event.getKey(), key)) {
            throw new EventNotFoundException("event not found: " + eventSlug);
        }

        return eventMapper.eventToDisplayEventDto(event, user);
    }

    public List<EventDto> getEventsById(List<Long> eventIds) {
        List<Event> events = eventRepository.findAllById(eventIds);

        return events
                .stream()
                .map(eventMapper::eventToEventDto)
                .collect(Collectors.toList());
    }

    private DisplayEventDto saveEvent(CreateUpdateEventForm form, AppUser user, Event event) {
        eventMapper.toEvent(form, user, event);
        if (form.getImage() != null) {
            var s3Key = user.getId() + "/" + event.getSlug() + "/" + "highlight-image" + extensions.get(form.getImage().getContentType());
            String url = null;
            try {
                url = s3BucketStorageService.uploadFile(s3Key, new ByteArrayInputStream(form.getImage().getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            event.setImagePath(url);
        }
        if (!event.isLimited()) {
            event.setAttendeeLimit(0);
        }
        var isNewEvent = event.getId() == null;
        eventRepository.saveAndFlush(event);
        if (isNewEvent && event.getVisibility() == EventVisibility.PUBLIC) {
            applicationEventPublisher.publishEvent(new EventCreatedEvent(user.getId(), event.getId()));
        }
        return eventMapper.eventToDisplayEventDto(event, user);
    }

    public List<EventQuestionDto> updateEventQuestions(Long userId, String eventSlug, List<EventQuestionDto> questionsDto) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found: " + userId));

        Event event = eventRepository.findBySlug(eventSlug)
                .orElseThrow(() -> new EventNotFoundException("event not found: " + eventSlug));

        if (!event.getOrganizer().getId().equals(userId)) {
            throw new CustomAccessDeniedException("You are not the organizer of this event");
        }

        validateQuestionOrder(questionsDto);

        List<EventQuestion> toDeleteList = new ArrayList<>();
        event.getEventQuestions().forEach(e -> {
            if (questionsDto.stream().noneMatch(x -> Objects.equals(x.getId(), e.getId()))) {
                toDeleteList.add(e);
            }
        });

        toDeleteList.forEach(event.getEventQuestions()::remove);

        questionsDto.forEach(e -> {
            if (e.getId() == null) {
                var questionEntity = new EventQuestion();
                eventQuestionMapper.eventQuestionDtoToEntity(e, questionEntity);
                event.addEventQuestion(questionEntity);
            } else {
                var questionEntity = event.getEventQuestions()
                        .stream()
                        .filter(x -> Objects.equals(x.getId(), e.getId()))
                        .findFirst()
                        .orElseThrow(() -> new EventQuestionNotFoundException("event question not found: " + e.getId()));

                eventQuestionMapper.eventQuestionDtoToEntity(e, questionEntity);
            }
        });

        eventRepository.saveAndFlush(event);
        return eventQuestionMapper.eventQuestionEntityToDto(event.getEventQuestions())
                .stream().sorted(Comparator.comparing(EventQuestionDto::getOrder))
                .collect(Collectors.toList());
    }

    private void validateQuestionOrder(Collection<EventQuestionDto> questions) {
        var count = questions.size();
        for (int i = 1; i <= count; i++) {
            int j = i;
            var exists = questions.stream().anyMatch(e -> e.getOrder() == j);
            if (!exists) {
                throw new QuestionOrderNotValidException();
            }
        }
    }


    @Transactional
    public void removeEvent(long userId, String eventSlug) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found: " + userId));

        Event event = eventRepository.findBySlug(eventSlug)
                .orElseThrow(() -> new EventNotFoundException("event not found: " + eventSlug));

        if (!event.getOrganizer().getId().equals(userId)) {
            throw new AccessDeniedException("forbidden");
        }
        eventRepository.delete(event);
        applicationEventPublisher.publishEvent(new EventDeletedEvent(userId, event.getId()));
    }
}
