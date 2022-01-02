package com.github.mtahasahin.evently.domainevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUnfollowedEvent {
    private UUID followerId;
    private UUID followingId;
}
