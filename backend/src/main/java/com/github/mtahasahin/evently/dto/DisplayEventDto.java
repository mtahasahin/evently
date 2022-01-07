package com.github.mtahasahin.evently.dto;

import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.enums.EventVisibility;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisplayEventDto {
    private UserLightDto organizer;
    private List<EventQuestionDto> questions = new ArrayList<>();
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
    private Integer attendeeLimit;
    private int attendeeCount;
    private boolean eventStarted;
    private boolean eventEnded;
    private boolean approvalRequired;
    private boolean joined;
    private boolean waitingApproval;
    private boolean organizing;
    private EventVisibility visibility;
    public boolean isCanSee(){
        return joined || organizing;
    }
}

