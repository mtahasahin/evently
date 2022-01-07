package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.*;
import com.github.mtahasahin.evently.security.AuthService;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("isAnonymous()")
    @PostMapping(path = "/login")
    public AuthenticationResponse login(@Valid @RequestBody final LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping(path = "/refresh-token")
    public AuthenticationResponse refresh(@Valid @RequestBody final RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(path = "/register")
    public AuthenticationResponse register(@Valid @RequestBody final RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/change-password")
    public ApiResponse<Object> changePassword(Authentication authentication, @Valid @RequestBody final ChangePasswordRequest changePasswordRequest) {
        authService.changePassword(UUID.fromString(authentication.getName()), changePasswordRequest);
        return ApiResponse.Success(null, "Password is changed successfully.");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/close-account")
    public ApiResponse<Object> closeAccount(Authentication authentication,@RequestParam final String password) {
        authService.closeAccount(UUID.fromString(authentication.getName()), password);
        return ApiResponse.Success(null);
    }

}
