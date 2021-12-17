package com.github.mtahasahin.evently.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class UserDto {
    private Long id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Username must be alphanumeric and can contain hyphens")
    private String username;
    @Valid
    private ProfileDto profile;
}
