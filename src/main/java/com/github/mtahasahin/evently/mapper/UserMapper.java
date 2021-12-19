package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.PrivateProfileDto;
import com.github.mtahasahin.evently.dto.PublicProfileDto;
import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.dto.UserLightDto;
import com.github.mtahasahin.evently.entity.AppUser;
import org.mapstruct.*;

@Mapper(uses = {ProfileMapper.class})
public interface UserMapper {

    @Mapping(source = "userProfile", target = "profile")
    UserDto userToUserDto(AppUser user);

    @Mapping(target = "domainEvents", ignore = true)
    @Mapping(target = "organizedEvents", ignore = true)
    @Mapping(target = "eventApplications", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "followings", ignore = true)
    @Mapping(source = "profile", target = "userProfile")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDto userDto, @MappingTarget AppUser user);

    @Mapping(source = "user.userProfile.name", target = "name")
    @Mapping(source = "user.userProfile.about", target = "about")
    @Mapping(source = "user.userProfile.avatar", target = "avatar")
    @Mapping(source = "user.userProfile.profilePublic", target = "profilePublic")
    @Mapping(source = "user.userProfile.registrationDate", target = "registrationDate")
    @Mapping(target = "followersCount", expression = "java(user.getFollowers()!= null ? (int) user.getFollowers().stream().filter(e -> e.isConfirmed()).count():0)")
    @Mapping(target = "followingCount", expression = "java(user.getFollowings()!= null ? (int) user.getFollowings().stream().filter(e -> e.isConfirmed()).count():0)")
    @Mapping(target = "canEdit", constant = "false")
    PrivateProfileDto userToPrivateProfileDto(AppUser user, boolean following, boolean hasFollowingRequest, int activityCount);

    @Mapping(source = "user.userProfile.name", target = "name")
    @Mapping(source = "user.userProfile.about", target = "about")
    @Mapping(source = "user.userProfile.avatar", target = "avatar")
    @Mapping(source = "user.userProfile.profilePublic", target = "profilePublic")
    @Mapping(source = "user.userProfile.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "user.userProfile.registrationDate", target = "registrationDate")
    @Mapping(source = "user.userProfile.facebookUsername", target = "facebookUsername")
    @Mapping(source = "user.userProfile.githubUsername", target = "githubUsername")
    @Mapping(source = "user.userProfile.twitterUsername", target = "twitterUsername")
    @Mapping(source = "user.userProfile.instagramUsername", target = "instagramUsername")
    @Mapping(source = "user.userProfile.websiteUrl", target = "websiteUrl")
    @Mapping(target = "followersCount", expression = "java(user.getFollowers()!= null ? (int) user.getFollowers().stream().filter(e -> e.isConfirmed()).count():0)")
    @Mapping(target = "followingCount", expression = "java(user.getFollowings()!= null ? (int) user.getFollowings().stream().filter(e -> e.isConfirmed()).count():0)")
    PublicProfileDto userToPublicProfileDto(AppUser user, boolean following, boolean hasFollowingRequest, boolean canEdit, int activityCount);

    @Mapping(target = "name", source = "userProfile.name")
    @Mapping(target = "avatar", ignore = true)
    UserLightDto userToUserLightDto(AppUser user);
}