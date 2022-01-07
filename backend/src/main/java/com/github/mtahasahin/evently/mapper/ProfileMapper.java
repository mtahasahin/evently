package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.ProfileDto;
import com.github.mtahasahin.evently.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface ProfileMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    void ProfileDtoToUserProfile(ProfileDto profileDto, @MappingTarget UserProfile profile);

    ProfileDto ProfileToProfileDto(UserProfile userProfile);
}
