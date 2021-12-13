package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.dto.UserLightDto;
import com.github.mtahasahin.evently.interfaces.Profile;
import com.github.mtahasahin.evently.service.UserService;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return userService.getProfile(authentication != null ? Long.parseLong(authentication.getName()) : 0, username);
    }

    @PutMapping
    public ApiResponse<UserDto> updateProfile(Authentication authentication, @Valid @RequestBody UserDto userDto) {
        return ApiResponse.Success(userService.updateUser(Long.parseLong(authentication.getName()), userDto), "Profile updated.");
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/{username}/followers")
    public Page<UserLightDto> getFollowers(Authentication authentication, @PathVariable String username, @RequestParam int page) {
        return userService.getFollowers(authentication != null ? Long.parseLong(authentication.getName()) : 0, username, page);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/{username}/following")
    public Page<UserLightDto> getFollowing(Authentication authentication, @PathVariable String username, @RequestParam int page) {
        return userService.getFollowing(authentication != null ? Long.parseLong(authentication.getName()) : 0, username, page);
    }

    @GetMapping(path = "/{username}/follower-requests")
    public Page<UserLightDto> getFollowerRequests(Authentication authentication, @PathVariable String username, @RequestParam int page) {
        return userService.getFollowerRequests(Long.parseLong(authentication.getName()),username, page);
    }

    @PutMapping(path = "/{username}/following")
    public ApiResponse<Object> follow(Authentication authentication, @PathVariable String username) {
        return userService.follow(Long.parseLong(authentication.getName()), username);
    }

    @DeleteMapping(path = "/{username}/following")
    public ApiResponse<Object> unfollow(Authentication authentication, @PathVariable String username) {
        return userService.unfollow(Long.parseLong(authentication.getName()), username);
    }

    @PutMapping(path = "/follower-request/{username}")
    public ApiResponse<Object> acceptFollowerRequest(Authentication authentication, @PathVariable String username) {
        return userService.acceptFollowerRequest(Long.parseLong(authentication.getName()), username);
    }

    @DeleteMapping(path = "/follower-request/{username}")
    public ApiResponse<Object> rejectFollowerRequest(Authentication authentication, @PathVariable String username) {
        return userService.rejectFollowerRequest(Long.parseLong(authentication.getName()), username);
    }
}
