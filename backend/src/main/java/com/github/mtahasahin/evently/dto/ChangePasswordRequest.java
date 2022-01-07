package com.github.mtahasahin.evently.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String currentPassword;
    @NotBlank
    @Length(min = 8, message = "Password should be at least 8 characters long.")
    private String newPassword;
}
