package com.github.mtahasahin.evently.service;


import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.exception.EmailAlreadyTakenException;
import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.exception.UsernameAlreadyTakenException;
import com.github.mtahasahin.evently.interfaces.Profile;
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
        AppUser user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userName));
        return userMapper.userToUserDto(user);
    }

    public Profile getProfile(String requestingUser, String requestedUser) {
        AppUser requestingUserEntity = userRepository.findByUsername(requestingUser)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestingUser));
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUser)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUser));

        if(requestedUserEntity == requestingUserEntity){
            return userMapper.userToPublicProfileDto(requestedUserEntity, false, false);
        }

        var isFollowing = requestingUserEntity.isFollowing(requestedUserEntity);
        var hasFollowingRequest = requestingUserEntity.hasFollowingRequest(requestedUserEntity);

        if (!requestedUserEntity.getUserProfile().isProfilePublic() && !isFollowing) {
            return userMapper.userToPrivateProfileDto(requestedUserEntity, false, hasFollowingRequest);
        }

        return userMapper.userToPublicProfileDto(requestedUserEntity, isFollowing, hasFollowingRequest);
    }

    @Transactional
    public UserDto updateUser(String username, UserDto userDto) {
        AppUser userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        var x = userRepository.findByUsername(userDto.getUsername());
        var y = userRepository.findByEmail(userDto.getEmail());

        if (x.isPresent() && x.get() != userEntity) {
            throw new UsernameAlreadyTakenException("This username has been already taken: " + username);
        }

        if (y.isPresent() && y.get() != userEntity) {
            throw new EmailAlreadyTakenException("This email has been already taken: " + username);
        }

        userMapper.updateUserFromDto(userDto, userEntity);
        return userMapper.userToUserDto(userEntity);
    }
}
