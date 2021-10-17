package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.exception.EmailAlreadyTakenException;
import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.exception.UsernameAlreadyTakenException;
import com.github.mtahasahin.evently.mapper.UserMapper;
import com.github.mtahasahin.evently.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void whenUserExists_getUser() {
        AppUser user = AppUser.builder().build();
        UserDto userDto = UserDto.builder().build();

        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        Mockito.when(userMapper.userToUserDto(user)).thenReturn(userDto);

        var result = userService.getUser("username");

        assertEquals(result, userDto);
    }

    @Test
    void whenUserDoesNotExist_getUser() {
        AppUser user = AppUser.builder().build();
        UserDto userDto = UserDto.builder().build();

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser("username"));
    }

    @Test
    void whenUsernameEmailChange_updateUser() {
        AppUser user = AppUser.builder().username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-new").email("email-new").build();

        Mockito.when(userRepository.findByUsername("username-old")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-new")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail("email-new")).thenReturn(Optional.empty());

        userService.updateUser("username-old",userDto);

        Mockito.verify(userMapper,Mockito.times(1)).updateUserFromDto(userDto,user);
    }

    @Test
    void whenNoUsernameEmailChange_updateUser() {
        AppUser user = AppUser.builder().username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-old").email("email-old").build();

        Mockito.when(userRepository.findByUsername("username-old")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail("email-old")).thenReturn(Optional.of(user));

        userService.updateUser("username-old",userDto);

        Mockito.verify(userMapper,Mockito.times(1)).updateUserFromDto(userDto,user);
    }

    @Test
    void whenEmailAlreadyTaken_updateUser() {
        AppUser user = AppUser.builder().username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-new").email("email-new").build();

        AppUser existingUser = AppUser.builder().username("x").email("email-new").build();

        Mockito.when(userRepository.findByUsername("username-old")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-new")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail("email-new")).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyTakenException.class, () -> userService.updateUser("username-old",userDto));
    }

    @Test
    void whenUsernameAlreadyTaken_updateUser() {
        AppUser user = AppUser.builder().username("username-old").email("email-old").build();
        UserDto userDto = UserDto.builder().username("username-new").email("email-new").build();

        AppUser existingUser = AppUser.builder().username("username-new").email("email").build();

        Mockito.when(userRepository.findByUsername("username-old")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("username-new")).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.findByEmail("email-new")).thenReturn(Optional.empty());

        assertThrows(UsernameAlreadyTakenException.class, () -> userService.updateUser("username-old",userDto));
    }
}