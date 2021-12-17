package com.github.mtahasahin.evently.dto;

import com.github.mtahasahin.evently.enums.EventLocationType;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class EventDto {
    private Long id;
    private UserLightDto organizer;
    private String name;
    private String slug;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String timezone;
    private String imagePath;
    private String description;
    private EventLocationType eventLocationType;
    private String location;
    private String eventUrl;
    private String language;
    private boolean limited;
    private int attendeeCount;

}
