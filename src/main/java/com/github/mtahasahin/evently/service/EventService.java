package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.domainevent.EventCreatedEvent;
import com.github.mtahasahin.evently.domainevent.EventDeletedEvent;
import com.github.mtahasahin.evently.dto.*;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.Event;
import com.github.mtahasahin.evently.entity.EventApplication;
import com.github.mtahasahin.evently.entity.EventQuestion;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.exception.*;
import com.github.mtahasahin.evently.mapper.EventMapper;
import com.github.mtahasahin.evently.mapper.EventQuestionMapper;
import com.github.mtahasahin.evently.mapper.UserMapper;
import com.github.mtahasahin.evently.repository.EventApplicationRepository;
import com.github.mtahasahin.evently.repository.EventRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
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
    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventApplicationRepository eventApplicationRepository;
    private final EventMapper eventMapper;
    private final EventQuestionMapper eventQuestionMapper;
    private final S3BucketStorageService s3BucketStorageService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Event getEventBySlug(String slug) {
        return eventRepository.findBySlug(slug)
                .orElseThrow(() -> new EventNotFoundException("event not found: " + slug));
    }

    @Transactional
    public DisplayEventDto createEvent(UUID userId, CreateUpdateEventForm form) {
        AppUser user = userService.getUserById(userId, true);

        Event event = new Event();
        return saveEvent(form, user, event);
    }

    @Transactional
    public DisplayEventDto updateEvent(UUID userId, String eventSlug, CreateUpdateEventForm form) {
        AppUser user = userService.getUserById(userId, true);

        Event event = getEventBySlug(eventSlug);

        if (!event.getOrganizer().getId().equals(userId)) {
            throw new AccessDeniedException("forbidden");
        }

        return saveEvent(form, user, event);
    }

    public DisplayEventDto getEvent(UUID userId, String eventSlug, String key) {
        AppUser user = userService.getUserById(userId, false);

        Event event = getEventBySlug(eventSlug);

        if (!canUserViewEvent(user, event, key)) {
            throw new EventNotFoundException("event not found: " + eventSlug);
        }

        return eventMapper.eventToDisplayEventDto(event, user);
    }

    public List<UserLightDto> getEventAttendees(UUID userId, String slug, String key, int page, int limit) {
        AppUser user = userService.getUserById(userId, false);
        var event = getEventBySlug(slug);
        if(!canUserViewEvent(user, event, key)) {
            throw new EventNotFoundException("event not found: " + slug);
        }
        var pageRequest = PageRequest.of(page, limit);
        var attendees = eventApplicationRepository.getAllApplicants(event.getId(), pageRequest);
        return userService.getUsersById(attendees.toList());
    }

    public List<EventDto> getEventsById(List<UUID> eventIds) {
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

    public List<EventQuestionDto> updateEventQuestions(UUID userId, String eventSlug, List<EventQuestionDto> questionsDto) {
        AppUser user = userService.getUserById(userId, true);

        Event event = getEventBySlug(eventSlug);

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
    public void removeEvent(UUID userId, String eventSlug) {
        AppUser user = userService.getUserById(userId, true);

        Event event = getEventBySlug(eventSlug);

        if (!event.getOrganizer().getId().equals(userId)) {
            throw new AccessDeniedException("forbidden");
        }
        eventRepository.delete(event);
        applicationEventPublisher.publishEvent(new EventDeletedEvent(userId, event.getId()));
    }

    private boolean canUserViewEvent(AppUser user, Event event, String key) {
        return user != null && (user.isOrganizing(event) || user.isJoiningEvent(event)) || event.getVisibility() != EventVisibility.ONLY_WITH_LINK || Objects.equals(event.getKey(), key);
    }
}
