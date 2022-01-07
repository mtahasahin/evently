package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.dto.PrivateProfileDto;
import com.github.mtahasahin.evently.dto.ProfileDto;
import com.github.mtahasahin.evently.dto.PublicProfileDto;
import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.UserProfile;
import com.github.mtahasahin.evently.exception.CustomValidationException;
import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.mapper.UserMapper;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void whenUserExists_shouldReturnUser() {
        AppUser user = AppUser.builder().build();
        UserDto userDto = UserDto.builder().build();

        var userUUID = UUID.randomUUID();
        Mockito.when(userRepository.findById(userUUID)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.userToUserDto(user)).thenReturn(userDto);

        var result = userService.getUser(userUUID);

        assertEquals(result, userDto);
    }

    @Test
    void whenUserDoesNotExist_shouldThrowException() {

        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(UUID.randomUUID()));
    }

    @Test
    void whenProfileIsPrivateAndUserIsNotAFollower_shouldReturnPrivateProfile() {
        UUID user1UUID = UUID.randomUUID();
        AppUser requestingUser = AppUser.builder()
                .id(user1UUID)
                .username("user-1")
                .userProfile(UserProfile.builder()
                        .name("name1 surname1")
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        UUID user2UUID = UUID.randomUUID();
        AppUser requestedUser = AppUser.builder()
                .id(user2UUID)
                .username("user-2")
                .userProfile(UserProfile.builder()
                        .profilePublic(false)
                        .name("name2 surname2")
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        PrivateProfileDto profileDto = PrivateProfileDto.builder()
                .username("user-2")
                .name("name2 surname2")
                .following(false)
                .hasFollowingRequest(false)
                .build();

        Mockito.when(userRepository.findById(user1UUID)).thenReturn(Optional.of(requestingUser));
        Mockito.when(userRepository.findByUsername("user-2")).thenReturn(Optional.of(requestedUser));
        Mockito.when(activityRepository.countByUserId(user2UUID)).thenReturn(3);

        Mockito.when(userMapper.userToPrivateProfileDto(requestedUser, false, false, 3)).thenReturn(profileDto);

        var result = userService.getProfile(user1UUID, "user-2");

        assertEquals(profileDto, result);
    }

    @Test
    void whenProfileIsPrivateAndUserIsAFollower_shouldReturnPublicProfile() {
        UUID user1UUID = UUID.randomUUID();
        AppUser requestingUser = AppUser.builder()
                .id(user1UUID)
                .username("user-1")
                .userProfile(UserProfile.builder()
                        .name("name1 surname1")
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        UUID user2UUID = UUID.randomUUID();
        AppUser requestedUser = AppUser.builder()
                .id(user2UUID)
                .username("user-2")
                .userProfile(UserProfile.builder()
                        .name("name2 surname2")
                        .profilePublic(true)
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .domainEvents(new ArrayList<>())
                .build();

        requestedUser.addFollower(requestingUser);
        requestedUser.getUserProfile().setProfilePublic(false);

        PublicProfileDto profileDto = PublicProfileDto.builder()
                .username("user-2")
                .name("name2 surname2")
                .following(true)
                .hasFollowingRequest(false)
                .build();

        Mockito.when(userRepository.findById(user1UUID)).thenReturn(Optional.of(requestingUser));
        Mockito.when(userRepository.findByUsername("user-2")).thenReturn(Optional.of(requestedUser));
        Mockito.when(activityRepository.countByUserId(user2UUID)).thenReturn(2);

        Mockito.when(userMapper.userToPublicProfileDto(requestedUser, true, false, false, 2)).thenReturn(profileDto);

        var result = userService.getProfile(user1UUID, "user-2");

        assertEquals(profileDto, result);
    }

    @Test
    void whenProfileIsPublic_shouldReturnPublicProfile() {
        UUID user1UUID = UUID.randomUUID();
        AppUser requestingUser = AppUser.builder()
                .id(user1UUID)
                .username("user-1")
                .userProfile(UserProfile.builder()
                        .name("name1 surname1")
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        UUID user2UUID = UUID.randomUUID();
        AppUser requestedUser = AppUser.builder()
                .id(user2UUID)
                .username("user-2")
                .userProfile(UserProfile.builder()
                        .profilePublic(true)
                        .name("name2 surname2")
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();


        PublicProfileDto profileDto = PublicProfileDto.builder()
                .username("user-2")
                .name("name2 surname2")
                .following(false)
                .hasFollowingRequest(false)
                .build();

        Mockito.when(userRepository.findById(user1UUID)).thenReturn(Optional.of(requestingUser));
        Mockito.when(userRepository.findByUsername("user-2")).thenReturn(Optional.of(requestedUser));
        Mockito.when(activityRepository.countByUserId(user2UUID)).thenReturn(1);

        Mockito.when(userMapper.userToPublicProfileDto(requestedUser, false, false, false, 1)).thenReturn(profileDto);

        var result = userService.getProfile(user1UUID, "user-2");

        assertEquals(profileDto, result);
    }

    @Test
    void whenUsernameEmailChange_shouldUpdateUser() {
        UUID userUUID = UUID.randomUUID();
        AppUser user = AppUser.builder().id(userUUID).username("username-old").email("email-old").followers(List.of()).build();
        UserDto userDto = UserDto.builder().username("username-new").email("email-new")
                .profile(ProfileDto.builder().profilePublic(true).build()).build();

        Mockito.when(userRepository.findById(userUUID)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-new")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail("email-new")).thenReturn(Optional.empty());

        userService.updateUser(userUUID, userDto);

        Mockito.verify(userMapper, Mockito.times(1)).updateUserFromDto(userDto, user);
    }

    @Test
    void whenNoUsernameEmailChange_shouldUpdateUser() {
        UUID userUUID = UUID.randomUUID();
        AppUser user = AppUser.builder().id(userUUID).username("username-old").email("email-old").followers(new ArrayList<>()).build();
        UserDto userDto = UserDto.builder().username("username-old").email("email-old").profile(ProfileDto.builder().profilePublic(true).build()).build();

        Mockito.when(userRepository.findById(userUUID)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-old")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail("email-old")).thenReturn(Optional.of(user));

        userService.updateUser(userUUID, userDto);

        Mockito.verify(userMapper, Mockito.times(1)).updateUserFromDto(userDto, user);
    }

    @Test
    void whenEmailAlreadyTaken_shouldThrowException() {
        UUID userUUID = UUID.randomUUID();
        AppUser user = AppUser.builder().id(userUUID).username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-new").email("email-new").build();

        AppUser existingUser = AppUser.builder().username("x").email("email-new").build();

        Mockito.when(userRepository.findById(userUUID)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-new")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail("email-new")).thenReturn(Optional.of(existingUser));

        assertThrows(CustomValidationException.class, () -> userService.updateUser(userUUID, userDto));
    }

    @Test
    void whenUsernameAlreadyTaken_shouldThrowException() {
        UUID userUUID = UUID.randomUUID();
        AppUser user = AppUser.builder().id(userUUID).username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-new").email("email-new").build();

        UUID existingUserUUID = UUID.randomUUID();
        AppUser existingUser = AppUser.builder().id(existingUserUUID).username("username-new").email("email").build();

        Mockito.when(userRepository.findById(userUUID)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-new")).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.findByEmail("email-new")).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> userService.updateUser(userUUID, userDto));
    }
}