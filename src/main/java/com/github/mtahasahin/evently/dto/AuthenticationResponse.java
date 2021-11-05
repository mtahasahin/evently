package com.github.mtahasahin.evently.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticationResponse {
    private final String accessToken;
    private final String refreshToken;
}
