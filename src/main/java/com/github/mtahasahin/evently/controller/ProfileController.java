package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.interfaces.Profile;
import com.github.mtahasahin.evently.service.UserService;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public UserDto getProfile(Authentication authentication) {
        return userService.getUser(Long.parseLong(authentication.getName()));
    }

    @GetMapping(path = "/{username}")
    public Profile getProfiled(Authentication authentication, @PathVariable String username) {
        return userService.getProfile(Long.parseLong(authentication.getName()), username);
    }

    @PutMapping
    public ApiResponse<UserDto> updateProfile(Authentication authentication,@Valid @RequestBody UserDto userDto) {
        return ApiResponse.Success(userService.updateUser(Long.parseLong(authentication.getName()), userDto), "Profile updated.");
    }
}
