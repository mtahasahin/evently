package com.github.mtahasahin.evently.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email field can not be empty")
    private final String email;
    @NotBlank(message = "Password field can not be empty")
    private final String password;
}
