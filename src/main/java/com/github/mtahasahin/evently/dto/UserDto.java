package com.github.mtahasahin.evently.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    @Email
    private String email;
    @NotBlank
    private String username;
    private ProfileDto profile;
}