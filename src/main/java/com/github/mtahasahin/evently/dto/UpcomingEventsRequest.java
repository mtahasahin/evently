package com.github.mtahasahin.evently.dto;

import com.github.mtahasahin.evently.validator.Language;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class UpcomingEventsRequest {
    @Min(0)
    private int page;
    @Max(100)
    private int limit;
    private String language;
}
