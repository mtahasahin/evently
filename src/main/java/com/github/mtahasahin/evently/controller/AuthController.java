package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.*;
import com.github.mtahasahin.evently.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login")
    public AuthenticationResponse login(@Valid @RequestBody final LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping(path = "/refresh-token")
    public AuthenticationResponse refresh(@Valid @RequestBody final RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping(path = "/register")
    public AuthenticationResponse register(@Valid @RequestBody final RegisterRequest registerRequest){
        return authService.registerUser(registerRequest);
    }

}
