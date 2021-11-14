package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.dto.DisplayEventDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.Event;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.exception.EventNotFoundException;
import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.mapper.EventMapper;
import com.github.mtahasahin.evently.repository.EventRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final S3BucketStorageService s3BucketStorageService;

    private final HashMap<String, String> extensions = new HashMap<>() {{
        put("image/png", ".png");
        put("image/jpeg", ".jpeg");
    }};

    public DisplayEventDto createEvent(Long userId, CreateUpdateEventForm form) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found: " + userId));

        Event event = new Event();
        return saveEvent(form, user, event);
    }

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
                .orElseThrow(() -> new UserNotFoundException("user not found: " + userId));

        Event event = eventRepository.findBySlug(eventSlug)
                .orElseThrow(() -> new EventNotFoundException("event not found: " + eventSlug));

        if (event.getVisibility() == EventVisibility.ONLY_WITH_LINK && !Objects.equals(event.getKey(), key)) {
            throw new EventNotFoundException("event not found: " + eventSlug);
        }

        return eventMapper.eventToDisplayEventDto(event, user);
    }

    private DisplayEventDto saveEvent(CreateUpdateEventForm form, AppUser user, Event event) {
        eventMapper.toEvent(form, user, event);
        if(form.getImage() != null){
            var s3Key = user.getId() + "/" + event.getSlug() + "/" + "highlight-image" + extensions.get(form.getImage().getContentType());
            var url = s3BucketStorageService.uploadFile(s3Key, form.getImage());
            event.setImagePath(url);
        }
        if(!event.isLimited()){
            event.setAttendeeLimit(0);
        }
        eventRepository.save(event);
        return eventMapper.eventToDisplayEventDto(event, user);
    }

}
