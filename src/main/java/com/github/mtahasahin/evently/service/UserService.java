package com.github.mtahasahin.evently.service;


import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.exception.CustomValidationException;
import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.interfaces.Profile;
import com.github.mtahasahin.evently.mapper.UserMapper;
import com.github.mtahasahin.evently.repository.UserRepository;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUser(long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        return userMapper.userToUserDto(user);
    }

    public Profile getProfile(long requestingUserId, String requestedUser) {
        AppUser requestingUserEntity = userRepository.findById(requestingUserId)
                .orElse(null);
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUser)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUser));

        if (requestedUserEntity == requestingUserEntity) {
            return userMapper.userToPublicProfileDto(requestedUserEntity, false, false, true);
        }

        var isFollowing = requestingUserEntity != null && requestingUserEntity.isFollowing(requestedUserEntity);
        var hasFollowingRequest = requestingUserEntity != null && requestingUserEntity.hasFollowingRequest(requestedUserEntity);

        if (!requestedUserEntity.getUserProfile().isProfilePublic() && !isFollowing) {
            return userMapper.userToPrivateProfileDto(requestedUserEntity, false, hasFollowingRequest);
        }

        return userMapper.userToPublicProfileDto(requestedUserEntity, isFollowing, hasFollowingRequest,false);
    }

    public UserDto updateUser(long id, UserDto userDto) {
        AppUser userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));

        var x = userRepository.findByUsername(userDto.getUsername());
        var y = userRepository.findByEmail(userDto.getEmail());

        if (x.isPresent() && x.get() != userEntity) {
            throw new CustomValidationException(new ApiResponse.ApiSubError("username", "This username has already been taken"));
        }

        if (y.isPresent() && y.get() != userEntity) {
            throw new CustomValidationException(new ApiResponse.ApiSubError("email", "This email has already been taken"));
        }

        userMapper.updateUserFromDto(userDto, userEntity);
        userRepository.saveAndFlush(userEntity);
        return userMapper.userToUserDto(userEntity);
    }

    public void follow(long requestingUserId, String username){
        AppUser userEntity = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestingUserId));
        AppUser userToFollow = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        userToFollow.addFollower(userEntity);
        userRepository.saveAllAndFlush(List.of(userEntity, userToFollow));
    }

    public void unfollow(long requestingUserId, String username){
        AppUser userEntity = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestingUserId));
        AppUser userToFollow = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        userToFollow.removeFollower(userEntity);
        userRepository.saveAndFlush(userToFollow);
    }
}
