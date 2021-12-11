package com.github.mtahasahin.evently.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@Document(indexName = "event_index")
public class EventSearchDto {
    private Long id;
    private UserSearchDto organizer;
    private String slug;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String timezone;
    private String imagePath;
    private int attendeeCount;

    public boolean getEventStarted() {
        return ZonedDateTime.of(startDate, ZoneId.of(timezone)).toLocalDateTime().isBefore(LocalDateTime.now());
    }

    public boolean getEventEnded() {
        return ZonedDateTime.of(endDate, ZoneId.of(timezone)).toLocalDateTime().isBefore(LocalDateTime.now());
    }
}
