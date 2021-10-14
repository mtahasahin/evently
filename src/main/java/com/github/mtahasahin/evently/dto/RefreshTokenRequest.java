package com.github.mtahasahin.evently.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotEmpty
    private String refreshToken;
}
