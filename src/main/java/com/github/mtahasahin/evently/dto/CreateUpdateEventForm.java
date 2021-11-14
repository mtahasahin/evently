package com.github.mtahasahin.evently.dto;

import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.validator.Language;
import com.github.mtahasahin.evently.validator.TimeZone;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUpdateEventForm {
    @NotBlank
    @Length(min = 5, max = 80)
    private String name;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
    @NotBlank
    @TimeZone
    private String timezone;
    private MultipartFile image;
    @NotBlank
    private String description;
    @NotNull
    private EventLocationType eventLocationType;
    private String location;
    private String eventUrl;
    @NotBlank
    @Language
    private String language;
    @NotNull
    private EventVisibility visibility;
    @NotNull
    private Boolean limited;
    private Integer attendeeLimit;
    @NotNull
    private Boolean approvalRequired;
}
