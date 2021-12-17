package com.github.mtahasahin.evently.domainevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventApplicationCancelledEvent {
    private Long userId;
    private Long eventId;
}
