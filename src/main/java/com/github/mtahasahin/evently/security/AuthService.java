package com.github.mtahasahin.evently.security;

import com.github.mtahasahin.evently.dto.*;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.Authority;
import com.github.mtahasahin.evently.entity.UserProfile;
import com.github.mtahasahin.evently.enums.AuthProvider;
import com.github.mtahasahin.evently.exception.CustomValidationException;
import com.github.mtahasahin.evently.exception.EmailAlreadyTakenException;
import com.github.mtahasahin.evently.repository.AuthorityRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import com.github.mtahasahin.evently.service.S3BucketStorageService;
import com.github.mtahasahin.evently.util.ImageUtils;
import com.github.mtahasahin.evently.util.JwtTokenProvider;
import com.github.mtahasahin.evently.util.RandomStringGenerator;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import com.ibm.icu.util.ULocale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.mtahasahin.evently.util.UsernameUtils.createUniqueUsername;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final S3BucketStorageService s3BucketStorageService;

    public AuthenticationResponse login(final LoginRequest loginRequest) {
        return authenticate(loginRequest.getEmail(), loginRequest.getPassword());
    }

    public AuthenticationResponse refreshToken(final RefreshTokenRequest refreshTokenRequest) {
        var claims = jwtTokenProvider.getClaimsFromJWT(refreshTokenRequest.getRefreshToken());
        String userName = claims.getSubject();
        var user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        var accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()));
        return new AuthenticationResponse(accessToken, refreshTokenRequest.getRefreshToken());
    }

    @Transactional
    public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
        Optional<AppUser> user = userRepository.findByEmail(registerRequest.getEmail());
        if (user.isPresent()) {
            throw new EmailAlreadyTakenException("This email has already been taken");
        }

        var userEntity = new AppUser();
        userEntity.setUsername(createUniqueUsername(registerRequest.getName(), userRepository));
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setProvider(AuthProvider.local);
        userRepository.save(userEntity);

        var userProfileEntity = new UserProfile();
        userProfileEntity.setName(registerRequest.getName());
        userProfileEntity.setProfilePublic(true);
        userProfileEntity.setRegistrationDate(LocalDateTime.now());
        userProfileEntity.setUser(userEntity);
        var s3Key = userEntity.getId() + "/" + "profile-" + RandomStringGenerator.generate(5) + ".jpg";
        try {
            var url = s3BucketStorageService.uploadFile(s3Key, new ByteArrayInputStream(ImageUtils.toByteArray(ImageUtils.createProfileImageFromName(registerRequest.getName(), 300), "jpg")));
            userProfileEntity.setAvatar(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        var language = new ULocale(registerRequest.getLanguage()).getLanguage();
        userProfileEntity.setLanguage(language.isEmpty() ? "en" : language);

        userEntity.setUserProfile(userProfileEntity);

        var userAuthority = authorityRepository.getByAuthority("ROLE_USER");
        userEntity.getAuthorities().add(userAuthority);
        userRepository.saveAndFlush(userEntity);

        return authenticate(registerRequest.getEmail(), registerRequest.getPassword());
    }

    public void changePassword(UUID userId, ChangePasswordRequest changePasswordRequest) {
        var user = userRepository.findById(userId).orElseThrow();
        var passwordMatches = passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new CustomValidationException(new ApiResponse.ApiSubError("currentPassword", "Current password is invalid"));
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.saveAndFlush(user);
    }

    public void closeAccount(UUID userId, String password) {
        var user = userRepository.findById(userId).orElseThrow();
        var passwordMatches = passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatches) {
            throw new CustomValidationException(new ApiResponse.ApiSubError("password", "Password is invalid"));
        }
        userRepository.delete(user);
    }

    private AuthenticationResponse authenticate(String email, String password) {
        Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationProvider.authenticate(usernamePasswordAuthenticationToken);
        AppUser user = (AppUser) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        return new AuthenticationResponse(accessToken, refreshToken);
    }

}
