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
import com.github.mtahasahin.evently.interfaces.FileStorageService;
import com.github.mtahasahin.evently.interfaces.Profile;
import com.github.mtahasahin.evently.mapper.UserMapper;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import com.github.mtahasahin.evently.repository.FollowerFollowingRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import com.github.mtahasahin.evently.util.ImageUtils;
import com.github.mtahasahin.evently.util.RandomStringGenerator;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.mtahasahin.evently.util.ImageUtils.resizeImage;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowerFollowingRepository followerFollowingRepository;
    private final ActivityRepository activityRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final FileStorageService fileStorageService;

    private static final int PAGE_SIZE = 8;

    public AppUser getUserById(@Nullable UUID id, boolean required) {
        if(id == null) {
            if(required) {
                throw new UserNotFoundException("User not found");
            }
            else{
                return null;
            }
        }
        return userRepository.findById(id).orElseGet(() -> {
            if(required) {
                throw new UserNotFoundException("User not found: " + id);
            }
            return null;
        });
    }

    public AppUser getUserByUsername(@NonNull String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    public UserDto getUser(@NonNull UUID id) {
        AppUser user = getUserById(id, true);
        return userMapper.userToUserDto(user);
    }

    public UserDto getUser(@NonNull String username) {
        AppUser user = getUserByUsername(username);
        return userMapper.userToUserDto(user);
    }

    public List<UserLightDto> getUsersById(@NonNull List<UUID> ids) {
        return userRepository.findAllById(ids);
    }

    public Profile getProfile(@Nullable UUID requestingUserId,@NonNull String requestedUser) {
        AppUser requestingUserEntity = getUserById(requestingUserId, false);
        AppUser requestedUserEntity = getUserByUsername(requestedUser);

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
    public UserDto updateUser(@NonNull UUID id,@NonNull UserDto userDto) {
        AppUser userEntity = getUserById(id, true);

        var x = userRepository.findByUsername(userDto.getUsername());
        var y = userRepository.findByEmail(userDto.getEmail());

        if (x.isPresent() && x.get() != userEntity) {
            throw new CustomValidationException(new ApiResponse.ApiSubError("username", "This username has already been taken"));
        }

        if (y.isPresent() && y.get() != userEntity) {
            throw new CustomValidationException(new ApiResponse.ApiSubError("email", "This email has already been taken"));
        }

        var followerIds = new ArrayList<UUID>();
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
    public ApiResponse<Object> follow(@NonNull UUID requestingUserId,@NonNull String username) {
        AppUser userEntity = getUserById(requestingUserId, true);
        AppUser userToFollow = getUserByUsername(username);

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

    public ApiResponse<Object> unfollow(@NonNull UUID requestingUserId,@NonNull String username) {
        AppUser userEntity = getUserById(requestingUserId, true);
        AppUser userToFollow = getUserByUsername(username);

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

    public Page<UserLightDto> getFollowers(@Nullable UUID requestingUserId,@NonNull String requestedUserUsername, int page) {
        AppUser requestingUserEntity = getUserById(requestingUserId, false);
        AppUser requestedUserEntity = getUserByUsername(requestedUserUsername);

        if (!canSeeProfile(requestingUserEntity, requestedUserEntity)) {
            throw new CustomAccessDeniedException();
        }

        var pageable = PageRequest.of(page, PAGE_SIZE);

        var followers = userRepository.findByFollowings_Id_followingIdAndFollowings_confirmedOrderById(requestedUserEntity.getId(), true, pageable);

        var result = followers.stream()
                .map(userMapper::userToUserLightDto)
                .collect(Collectors.toList());

        return new PageImpl<UserLightDto>(result, pageable, followers.getTotalElements());
    }

    public Page<UserLightDto> getFollowing(@Nullable UUID requestingUserId,@NonNull String requestedUserUsername, int page) {
        AppUser requestingUserEntity = getUserById(requestingUserId, false);
        AppUser requestedUserEntity = getUserByUsername(requestedUserUsername);

        if (!canSeeProfile(requestingUserEntity, requestedUserEntity)) {
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


    public Page<UserLightDto> getFollowerRequests(@NonNull UUID requestingUserId,@NonNull String requestedUserUsername, int page) {
        AppUser userEntity = getUserById(requestingUserId, true);

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

    public ApiResponse<Object> acceptFollowerRequest(@NonNull UUID requestingUserId,@NonNull String requestedUserUsername) {
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUserUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUserUsername));

        FollowerFollowing f = followerFollowingRepository.findById(new FollowerFollowingId(requestedUserEntity.getId(), requestingUserId))
                .orElseThrow(CustomAccessDeniedException::new);

        f.setConfirmed(true);

        followerFollowingRepository.saveAndFlush(f);
        return ApiResponse.Success(null, "Follower request accepted");
    }

    public ApiResponse<Object> rejectFollowerRequest(@NonNull UUID requestingUserId,@NonNull String requestedUserUsername) {
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUserUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUserUsername));

        FollowerFollowing f = followerFollowingRepository.findById(new FollowerFollowingId(requestedUserEntity.getId(), requestingUserId))
                .orElseThrow(CustomAccessDeniedException::new);

        followerFollowingRepository.delete(f);
        return ApiResponse.Success(null, "Follower request rejected");
    }

    public boolean canSeeProfile(AppUser requestingUserEntity,AppUser requestedUserEntity) {
        return requestedUserEntity.getUserProfile().isProfilePublic() ||
                (requestingUserEntity != null && (requestedUserEntity == requestingUserEntity || requestingUserEntity.isFollowing(requestedUserEntity)));
    }

    public boolean canSeeProfile(UUID requestingUserId, String requestedUserUsername) {
        AppUser requestingUserEntity = userRepository.findById(requestingUserId)
                .orElseGet(() -> null);
        AppUser requestedUserEntity = userRepository.findByUsername(requestedUserUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + requestedUserUsername));

        return requestedUserEntity.getUserProfile().isProfilePublic() ||
                (requestingUserEntity != null && (requestedUserEntity == requestingUserEntity || requestingUserEntity.isFollowing(requestedUserEntity)));
    }

    public ApiResponse updateAvatar(@NonNull UUID userId,@NonNull String username, MultipartFile avatar) {
        AppUser user = getUserById(userId, true);

        if(!Objects.equals(user.getUsername(), username)) {
            throw new CustomAccessDeniedException();
        }

        try {
            var imageExtension = ImageUtils.getExtension(avatar);
            var s3Key = user.getId() + "/" + "profile-" + RandomStringGenerator.generate(5) + imageExtension;
            var image = ImageIO.read(avatar.getInputStream());
            var resizedImage = resizeImage(image, 300, 300);
            var url = fileStorageService.uploadFile(s3Key, new ByteArrayInputStream(ImageUtils.toByteArray(resizedImage,imageExtension.substring(1))));
            user.getUserProfile().setAvatar(url);
            userRepository.saveAndFlush(user);
            return ApiResponse.Success(null, "Avatar updated");
        } catch (IOException exception) {
            return ApiResponse.Error("Avatar couldn't be updated");
        }
    }

}
