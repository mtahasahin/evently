package com.github.mtahasahin.evently.domainevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptMultipleFriendRequestsEvent {
    private Long userId;
    private List<Long> followerIds;
}
