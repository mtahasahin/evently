package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.interfaces.Profile;
import com.github.mtahasahin.evently.service.UserService;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public UserDto me(Authentication authentication) {
        return userService.getUser(Long.parseLong(authentication.getName()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/{username}")
    public Profile getProfile(Authentication authentication, @PathVariable String username) {
        if(authentication != null){
            return userService.getProfile(Long.parseLong(authentication.getName()), username);
        }
        else{
            return userService.getProfile(0, username);
        }

    }

    @PutMapping
    public ApiResponse<UserDto> updateProfile(Authentication authentication, @Valid @RequestBody UserDto userDto) {
        return ApiResponse.Success(userService.updateUser(Long.parseLong(authentication.getName()), userDto), "Profile updated.");
    }

    @PutMapping(path = "/{username}/following")
    public ApiResponse<Object> follow(Authentication authentication, @PathVariable String username) {
        userService.follow(Long.parseLong(authentication.getName()), username);
        return ApiResponse.Success(null, "You are following " + username + " now.");
    }

    @DeleteMapping(path = "/{username}/following")
    public ApiResponse<Object> unfollow(Authentication authentication, @PathVariable String username) {
        userService.unfollow(Long.parseLong(authentication.getName()), username);
        return ApiResponse.Success(null, "You stopped following " + username + ".");
    }
}
