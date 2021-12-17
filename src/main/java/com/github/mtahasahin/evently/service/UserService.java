package com.github.mtahasahin.evently.service;


import com.github.mtahasahin.evently.domainevent.AcceptMultipleFriendRequestsEvent;
import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.dto.UserLightDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.FollowerFollowing;
import com.github.mtahasahin.evently.entity.FollowerFollowingId;
import com.github.mtahasahin.evently.exception.CustomAccessDeniedException;
import com.github.mtahasahin.evently.exception.CustomValidationException;
import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.interfaces.Profile;
import com.github.mtahasahin.evently.mapper.UserMapper;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import com.github.mtahasahin.evently.repository.FollowerFollowingRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowerFollowingRepository followerFollowingRepository;
    private final ActivityRepository activityRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final int PAGE_SIZE = 8;

    public UserDto getUser(long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        return userMapper.userToUserDto(user);
    }

    public UserDto getUser(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return userMapper.userToUserDto(user);
    }

    public List<UserLightDto> getUsersById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    public Profile getProfile(long requestingUserId, String requestedUser) {
        AppUser requestingUserEntity = userRepository.findById(requestingUserId)
                .orElse(null);
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUser)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUser));

        int activityCount = activityRepository.countByUserId(requestedUserEntity.getId());

        if (requestedUserEntity == requestingUserEntity) {
            return userMapper.userToPublicProfileDto(requestedUserEntity, false, false, true, activityCount);
        }

        var isFollowing = requestingUserEntity != null && requestingUserEntity.isFollowing(requestedUserEntity);
        var hasFollowingRequest = requestingUserEntity != null && requestingUserEntity.hasFollowingRequest(requestedUserEntity);

        if (!requestedUserEntity.getUserProfile().isProfilePublic() && !isFollowing) {
            return userMapper.userToPrivateProfileDto(requestedUserEntity, false, hasFollowingRequest, activityCount);
        }

        return userMapper.userToPublicProfileDto(requestedUserEntity, isFollowing, hasFollowingRequest, false, activityCount);
    }

    @Transactional
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

        var followerIds = new ArrayList<Long>();
        if (userDto.getProfile().isProfilePublic()) {
            userEntity
                    .getFollowers()
                    .stream()
                    .filter(e -> !e.isConfirmed())
                    .forEach(followerFollowing -> {
                        followerFollowing.setConfirmed(true);
                        followerIds.add(followerFollowing.getFollower().getId());
                    });
        }
        applicationEventPublisher.publishEvent(new AcceptMultipleFriendRequestsEvent(id, followerIds));

        userMapper.updateUserFromDto(userDto, userEntity);
        userRepository.saveAndFlush(userEntity);
        return userMapper.userToUserDto(userEntity);
    }

    @Transactional
    public ApiResponse<Object> follow(long requestingUserId, String username) {
        AppUser userEntity = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestingUserId));
        AppUser userToFollow = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        if (userEntity.isFollowing(userToFollow)) {
            return ApiResponse.Error(null, "You are already following this user");
        }

        userToFollow.addFollower(userEntity);
        userRepository.saveAll(List.of(userEntity, userToFollow));
        if (userToFollow.getUserProfile().isProfilePublic()) {
            return ApiResponse.Success(null, "You are now following this user");
        } else {
            return ApiResponse.Success(null, "A follow request has been sent to @" + username + " and is pending their approval.");
        }
    }

    public ApiResponse<Object> unfollow(long requestingUserId, String username) {
        AppUser userEntity = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestingUserId));
        AppUser userToFollow = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        var isFollowing = userEntity.isFollowing(userToFollow);
        var hasFollowingRequest = userEntity.hasFollowingRequest(userToFollow);

        if (!isFollowing && !hasFollowingRequest) {
            return ApiResponse.Error(null, "You are not following this user");
        }

        userToFollow.removeFollower(userEntity);
        userRepository.saveAndFlush(userToFollow);

        if (isFollowing) {
            return ApiResponse.Success(null, "You are no longer following this user");
        } else {
            return ApiResponse.Success(null, "You've canceled your pending follow request");
        }
    }

    public Page<UserLightDto> getFollowers(long requestingUserId, String requestedUserUsername, int page) {
        AppUser requestingUserEntity = userRepository.findById(requestingUserId)
                .orElseGet(() -> null);
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUserUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUserUsername));

        if (!requestedUserEntity.getUserProfile().isProfilePublic() &&
                (requestingUserEntity == null || (requestedUserEntity != requestingUserEntity &&
                        !requestingUserEntity.isFollowing(requestedUserEntity)))) {
            throw new CustomAccessDeniedException();
        }

        var pageable = PageRequest.of(page, PAGE_SIZE);

        var followers = userRepository.findByFollowings_Id_followingIdAndFollowings_confirmedOrderById(requestedUserEntity.getId(), true, pageable);

        var result = followers.stream()
                .map(userMapper::userToUserLightDto)
                .collect(Collectors.toList());

        return new PageImpl<UserLightDto>(result, pageable, followers.getTotalElements());
    }

    public Page<UserLightDto> getFollowing(long requestingUserId, String requestedUserUsername, int page) {
        AppUser requestingUserEntity = userRepository.findById(requestingUserId)
                .orElseGet(() -> null);
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUserUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUserUsername));

        if (!canSeeProfile(requestingUserId, requestedUserUsername)) {
            throw new CustomAccessDeniedException();
        }

        var pageable = PageRequest.of(page, PAGE_SIZE);

        var following = userRepository.findByFollowers_Id_followerIdAndFollowers_confirmedOrderById(requestedUserEntity.getId(), true, PageRequest.of(page, PAGE_SIZE));

        var result = following
                .stream()
                .map(userMapper::userToUserLightDto)
                .collect(Collectors.toList());

        return new PageImpl<UserLightDto>(result, pageable, following.getTotalElements());
    }


    public Page<UserLightDto> getFollowerRequests(long requestingUserId, String requestedUserUsername, int page) {
        AppUser userEntity = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestingUserId));

        if (!userEntity.getUsername().equals(requestedUserUsername)) {
            throw new CustomAccessDeniedException("Forbidden");
        }

        var pageable = PageRequest.of(page, PAGE_SIZE);

        var followerRequests = userRepository.findByFollowings_Id_followingIdAndFollowings_confirmedOrderById(requestingUserId, false, PageRequest.of(page, PAGE_SIZE));

        var result = followerRequests
                .stream()
                .map(userMapper::userToUserLightDto)
                .collect(Collectors.toList());

        return new PageImpl<UserLightDto>(result, pageable, followerRequests.getTotalElements());
    }

    public ApiResponse<Object> acceptFollowerRequest(long requestingUserId, String requestedUserUsername) {
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUserUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUserUsername));

        FollowerFollowing f = followerFollowingRepository.findById(new FollowerFollowingId(requestedUserEntity.getId(), requestingUserId))
                .orElseThrow(CustomAccessDeniedException::new);

        f.setConfirmed(true);

        followerFollowingRepository.saveAndFlush(f);
        return ApiResponse.Success(null, "Follower request accepted");
    }

    public ApiResponse<Object> rejectFollowerRequest(long requestingUserId, String requestedUserUsername) {
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUserUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUserUsername));

        FollowerFollowing f = followerFollowingRepository.findById(new FollowerFollowingId(requestedUserEntity.getId(), requestingUserId))
                .orElseThrow(CustomAccessDeniedException::new);

        followerFollowingRepository.delete(f);
        return ApiResponse.Success(null, "Follower request rejected");
    }

    public boolean canSeeProfile(long requestingUserId, String requestedUserUsername) {
        AppUser requestingUserEntity = userRepository.findById(requestingUserId)
                .orElseGet(() -> null);
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUserUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUserUsername));

        return requestedUserEntity.getUserProfile().isProfilePublic() ||
                (requestingUserEntity != null && (requestedUserEntity == requestingUserEntity || requestingUserEntity.isFollowing(requestedUserEntity)));
    }
}
