package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.ProfileDto;
import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.UserProfile;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    void before() {
        userMapper = new UserMapperImpl(new ProfileMapperImpl());
    }

    @Test
    void userToUserDto() {
        AppUser user = AppUser.builder()
                .username("user")
                .email("user@example.com")
                .userProfile(UserProfile.builder()
                        .profilePublic(true)
                        .name("name lastname")
                        .about("about")
                        .dateOfBirth(LocalDate.now())
                        .facebookUsername("user-facebook")
                        .githubUsername("user-github")
                        .instagramUsername("user-instagram")
                        .twitterUsername("user-twitter")
                        .websiteUrl("user-website.com")
                        .registrationDate(LocalDateTime.now())
                        .build())
                .build();

        UserDto userDto = userMapper.userToUserDto(user);
        ProfileDto profileDto = userDto.getProfile();
        UserProfile profile = user.getUserProfile();

        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(profile.isProfilePublic(), profileDto.isProfilePublic());
        assertEquals(profile.getAbout(), profileDto.getAbout());
        assertEquals(profile.getDateOfBirth(), profileDto.getDateOfBirth());
        assertEquals(profile.getName(), profileDto.getName());
        assertEquals(profile.getWebsiteUrl(), profileDto.getWebsiteUrl());
        assertEquals(profile.getTwitterUsername(), profileDto.getTwitterUsername());
        assertEquals(profile.getFacebookUsername(), profileDto.getFacebookUsername());
        assertEquals(profile.getGithubUsername(), profileDto.getGithubUsername());
        assertEquals(profile.getInstagramUsername(), profileDto.getInstagramUsername());
    }

    @Test
    void updateUserFromDto() {
        LocalDateTime registrationDate = LocalDateTime.now();
        UserDto userDto = UserDto.builder()
                .username("username-new")
                .email("user-new@example.com")
                .profile(ProfileDto.builder()
                        .profilePublic(false)
                        .dateOfBirth(LocalDate.now())
                        .name("name surname-new")
                        .about("about-new")
                        .facebookUsername("user-facebook-new")
                        .githubUsername("user-github-new-new")
                        .instagramUsername("user-instagram-new")
                        .twitterUsername("user-twitter-new")
                        .websiteUrl("user-website.com-new")
                        .build())
                .build();

        AppUser user = AppUser.builder()
                .id(1L)
                .username("user")
                .password("password")
                .email("user@example.com")
                .userProfile(UserProfile.builder()
                        .id(2L)
                        .profilePublic(true)
                        .name("name lastname")
                        .about("about")
                        .dateOfBirth(LocalDate.now())
                        .facebookUsername("user-facebook")
                        .githubUsername("user-github")
                        .instagramUsername("user-instagram")
                        .twitterUsername("user-twitter")
                        .websiteUrl("user-website.com")
                        .registrationDate(registrationDate)
                        .build())
                .build();

        userMapper.updateUserFromDto(userDto, user);
        UserProfile profile = user.getUserProfile();
        ProfileDto profileDto = userDto.getProfile();

        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(1L, user.getId());
        assertEquals("password", user.getPassword());
        assertEquals(2L, profile.getId());
        assertEquals(profileDto.getAbout(), profile.getAbout());
        assertEquals(profileDto.getName(), profile.getName());
        assertEquals(profileDto.isProfilePublic(), profile.isProfilePublic());
        assertEquals(profileDto.getDateOfBirth(), profile.getDateOfBirth());
        assertEquals(profileDto.getInstagramUsername(), profile.getInstagramUsername());
        assertEquals(profileDto.getFacebookUsername(), profile.getFacebookUsername());
        assertEquals(profileDto.getGithubUsername(), profile.getGithubUsername());
        assertEquals(profileDto.getTwitterUsername(), profile.getTwitterUsername());
        assertEquals(profileDto.getWebsiteUrl(), profile.getWebsiteUrl());
    }
}