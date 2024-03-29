package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.dto.UserLightDto;
import com.github.mtahasahin.evently.interfaces.Profile;
import com.github.mtahasahin.evently.service.UserService;
import com.github.mtahasahin.evently.util.ImageUtils;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public UserDto me(Authentication authentication) {
        return userService.getUser(UUID.fromString(authentication.getName()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/{username}")
    public Profile getProfile(Authentication authentication, @PathVariable String username) {
        return userService.getProfile(authentication != null ? UUID.fromString(authentication.getName()) : null, username);
    }

    @PutMapping
    public ApiResponse<UserDto> updateProfile(Authentication authentication, @Valid @RequestBody UserDto userDto) {
        return ApiResponse.Success(userService.updateUser(UUID.fromString(authentication.getName()), userDto), "Profile updated.");
    }

    @PostMapping(consumes = "multipart/form-data", path = "/{username}/avatar")
    public ApiResponse updateAvatar(Authentication authentication, @Valid @RequestParam("avatar") MultipartFile avatar, @PathVariable String username){
        var validationResult = ImageUtils.isValid(avatar, 3 * 1024 * 1024, 128, 128);
        if(!validationResult.isSuccess()){
            return validationResult;
        }
        return userService.updateAvatar(UUID.fromString(authentication.getName()), username, avatar);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/{username}/followers")
    public Page<UserLightDto> getFollowers(Authentication authentication, @PathVariable String username, @RequestParam int page) {
        return userService.getFollowers(authentication != null ? UUID.fromString(authentication.getName()) : null, username, page);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/{username}/following")
    public Page<UserLightDto> getFollowing(Authentication authentication, @PathVariable String username, @RequestParam int page) {
        return userService.getFollowing(authentication != null ? UUID.fromString(authentication.getName()) : null, username, page);
    }

    @GetMapping(path = "/{username}/follower-requests")
    public Page<UserLightDto> getFollowerRequests(Authentication authentication, @PathVariable String username, @RequestParam int page) {
        return userService.getFollowerRequests(UUID.fromString(authentication.getName()),username, page);
    }

    @PutMapping(path = "/{username}/following")
    public ApiResponse<Object> follow(Authentication authentication, @PathVariable String username) {
        return userService.follow(UUID.fromString(authentication.getName()), username);
    }

    @DeleteMapping(path = "/{username}/following")
    public ApiResponse<Object> unfollow(Authentication authentication, @PathVariable String username) {
        return userService.unfollow(UUID.fromString(authentication.getName()), username);
    }

    @PutMapping(path = "/follower-request/{username}")
    public ApiResponse<Object> acceptFollowerRequest(Authentication authentication, @PathVariable String username) {
        return userService.acceptFollowerRequest(UUID.fromString(authentication.getName()), username);
    }

    @DeleteMapping(path = "/follower-request/{username}")
    public ApiResponse<Object> rejectFollowerRequest(Authentication authentication, @PathVariable String username) {
        return userService.rejectFollowerRequest(UUID.fromString(authentication.getName()), username);
    }
}
