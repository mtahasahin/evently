package com.github.mtahasahin.evently.service;


import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.exception.EmailAlreadyTakenException;
import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.exception.UsernameAlreadyTakenException;
import com.github.mtahasahin.evently.mapper.UserMapper;
import com.github.mtahasahin.evently.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUser(String userName) {
        Optional<AppUser> user = userRepository.findByUsername(userName);
        if(user.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        return userMapper.userToUserDto(user.get());
    }

    @Transactional
    public UserDto updateUser(String username, UserDto userDto){
        Optional<AppUser> userEntity = userRepository.findByUsername(username);
        if(userEntity.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        var x = userRepository.findByUsername(userDto.getUsername());
        var y = userRepository.findByEmail(userDto.getEmail());

        if(x.isPresent() && x.get() != userEntity.get()){
            throw new UsernameAlreadyTakenException("This username has been already taken");
        }

        if(y.isPresent() && y.get() != userEntity.get()){
            throw new EmailAlreadyTakenException("This email has been already taken");
        }

        userMapper.updateUserFromDto(userDto, userEntity.get());
        return userMapper.userToUserDto(userEntity.get());
    }
}
