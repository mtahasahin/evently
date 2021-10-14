package com.github.mtahasahin.evently.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticationResponse {
    private final String accessToken;
    private final String refreshToken;
}
