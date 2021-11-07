package com.github.mtahasahin.evently.dto;

import com.github.mtahasahin.evently.validator.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email field can not be empty")
    private String email;
    @NotBlank(message = "Password field can not be empty")
    @Length(min = 8, message = "Password should be at least 8 characters long")
    private String password;
    @NotBlank(message = "Name field can not be empty")
    @Length(min = 2, message = "Name should be at least 2 characters long")
    private String name;
    private String language;
}
