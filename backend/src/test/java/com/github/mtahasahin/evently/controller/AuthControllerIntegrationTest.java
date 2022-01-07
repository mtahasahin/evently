package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.BaseIT;
import com.github.mtahasahin.evently.dto.ChangePasswordRequest;
import com.github.mtahasahin.evently.dto.RegisterRequest;
import com.github.mtahasahin.evently.repository.UserRepository;
import com.github.mtahasahin.evently.util.JwtTokenProvider;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class AuthControllerIntegrationTest extends BaseIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpHeaders getAuthenticatedHeaders() {
        var headers = new HttpHeaders();
        var user = userRepository.findByUsername("test-user").orElseThrow();
        var jwt = jwtTokenProvider.generateAccessToken(user.getId(), List.of("ROLE_USER"));
        String token = "Bearer " + jwt;
        headers.add("Authorization", token);
        return headers;
    }

    @Test
    void login() {
        var loginRequest = new RegisterRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        ResponseEntity<ApiResponse> x = restTemplate.postForEntity("/api/auth/login", loginRequest, ApiResponse.class);

        assertEquals(200, x.getStatusCodeValue());
    }

    @Test
    void register() {
        var registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@register.com");
        registerRequest.setPassword("test123test");
        registerRequest.setName("Test User");
        registerRequest.setLanguage("en");

        ResponseEntity<ApiResponse> x = restTemplate.postForEntity("/api/auth/register", registerRequest, ApiResponse.class);

        assertEquals(200, x.getStatusCodeValue());
        userRepository.findByEmail(registerRequest.getEmail()).ifPresentOrElse(user -> {
            assertEquals(registerRequest.getEmail(), user.getEmail());
            assertEquals(registerRequest.getName(), user.getUserProfile().getName());
            assertEquals(registerRequest.getLanguage(), user.getUserProfile().getLanguage());
        }, () -> fail("User not found"));
    }

    @Test
    void whenUserChangesPassword_returnSuccess() {
        var changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("password");
        changePasswordRequest.setNewPassword("newpassword");

        var headers = getAuthenticatedHeaders();
        var response = restTemplate.exchange("/api/auth/change-password", HttpMethod.PUT, new HttpEntity<>(changePasswordRequest, headers), ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());

        userRepository.findByUsername("test-user").ifPresentOrElse(user1 -> {
            assertTrue(passwordEncoder.matches("newpassword", user1.getPassword()));
        }, () -> fail("User not found"));
    }

    @Test
    void whenUserChangesPassword_returnError() {
        var changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("wrongpassword");
        changePasswordRequest.setNewPassword("newpassword");

        var headers = getAuthenticatedHeaders();
        var response = restTemplate.exchange("/api/auth/change-password", HttpMethod.PUT, new HttpEntity<>(changePasswordRequest, headers), ApiResponse.class);
        assertEquals(400, response.getStatusCodeValue());

        userRepository.findByUsername("test-user").ifPresentOrElse(user1 -> {
            assertTrue(passwordEncoder.matches("password", user1.getPassword()));
        }, () -> fail("User not found"));
    }

    @Test
    void closeAccount() {
        var headers = getAuthenticatedHeaders();
        var response = restTemplate.exchange("/api/auth/close-account?password={password}", HttpMethod.DELETE, new HttpEntity<>(headers), ApiResponse.class, Map.of("password", "password"));

        assertEquals(200, response.getStatusCodeValue());
        userRepository.findByUsername("test-user").ifPresent(user1 -> fail("User not deleted"));
    }
}