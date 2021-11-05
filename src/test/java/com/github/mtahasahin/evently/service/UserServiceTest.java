package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.dto.PrivateProfileDto;
import com.github.mtahasahin.evently.dto.PublicProfileDto;
import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.UserProfile;
import com.github.mtahasahin.evently.exception.CustomValidationException;
import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.mapper.UserMapper;
import com.github.mtahasahin.evently.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Test
    void whenUserExists_shouldReturnUser() {
        AppUser user = AppUser.builder().build();
        UserDto userDto = UserDto.builder().build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.userToUserDto(user)).thenReturn(userDto);

        var result = userService.getUser(1);

        assertEquals(result, userDto);
    }

    @Test
    void whenUserDoesNotExist_shouldThrowException() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1));
    }

    @Test
    void whenProfileIsPrivateAndUserIsNotAFollower_shouldReturnPrivateProfile() {
        AppUser requestingUser = AppUser.builder()
                .id(1L)
                .username("user-1")
                .userProfile(UserProfile.builder()
                        .name("name1 surname1")
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        AppUser requestedUser = AppUser.builder()
                .id(2L)
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

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(requestingUser));
        Mockito.when(userRepository.findByUsername("user-2")).thenReturn(Optional.of(requestedUser));

        Mockito.when(userMapper.userToPrivateProfileDto(requestedUser, false, false)).thenReturn(profileDto);

        var result = userService.getProfile(1,"user-2");

        assertEquals(profileDto, result);
    }

    @Test
    void whenProfileIsPrivateAndUserIsAFollower_shouldReturnPublicProfile() {
        AppUser requestingUser = AppUser.builder()
                .id(1L)
                .username("user-1")
                .userProfile(UserProfile.builder()
                        .name("name1 surname1")
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        AppUser requestedUser = AppUser.builder()
                .id(2L)
                .username("user-2")
                .userProfile(UserProfile.builder()
                        .name("name2 surname2")
                        .profilePublic(false)
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        requestedUser.addFollower(requestingUser, true);

        PublicProfileDto profileDto = PublicProfileDto.builder()
                .username("user-2")
                .name("name2 surname2")
                .following(true)
                .hasFollowingRequest(false)
                .build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(requestingUser));
        Mockito.when(userRepository.findByUsername("user-2")).thenReturn(Optional.of(requestedUser));

        Mockito.when(userMapper.userToPublicProfileDto(requestedUser, true, false)).thenReturn(profileDto);

        var result = userService.getProfile(1,"user-2");

        assertEquals(profileDto, result);
    }

    @Test
    void whenProfileIsPublic_shouldReturnPublicProfile() {
        AppUser requestingUser = AppUser.builder()
                .id(1L)
                .username("user-1")
                .userProfile(UserProfile.builder()
                        .name("name1 surname1")
                        .build())
                .followings(new ArrayList<>())
                .followers(new ArrayList<>())
                .build();

        AppUser requestedUser = AppUser.builder()
                .id(2L)
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

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(requestingUser));
        Mockito.when(userRepository.findByUsername("user-2")).thenReturn(Optional.of(requestedUser));

        Mockito.when(userMapper.userToPublicProfileDto(requestedUser, false, false)).thenReturn(profileDto);

        var result = userService.getProfile(1,"user-2");

        assertEquals(profileDto, result);
    }

    @Test
    void whenUsernameEmailChange_shouldUpdateUser() {
        AppUser user = AppUser.builder().id(1L).username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-new").email("email-new").build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-new")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail("email-new")).thenReturn(Optional.empty());

        userService.updateUser(1, userDto);

        Mockito.verify(userMapper, Mockito.times(1)).updateUserFromDto(userDto, user);
    }

    @Test
    void whenNoUsernameEmailChange_shouldUpdateUser() {
        AppUser user = AppUser.builder().id(1L).username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-old").email("email-old").build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-old")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail("email-old")).thenReturn(Optional.of(user));

        userService.updateUser(1, userDto);

        Mockito.verify(userMapper, Mockito.times(1)).updateUserFromDto(userDto, user);
    }

    @Test
    void whenEmailAlreadyTaken_shouldThrowException() {
        AppUser user = AppUser.builder().id(1L).username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-new").email("email-new").build();

        AppUser existingUser = AppUser.builder().username("x").email("email-new").build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-new")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail("email-new")).thenReturn(Optional.of(existingUser));

        assertThrows(CustomValidationException.class, () -> userService.updateUser(1, userDto));
    }

    @Test
    void whenUsernameAlreadyTaken_shouldThrowException() {
        AppUser user = AppUser.builder().id(1L).username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-new").email("email-new").build();

        AppUser existingUser = AppUser.builder().id(2L).username("username-new").email("email").build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-new")).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.findByEmail("email-new")).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> userService.updateUser(1, userDto));
    }
}