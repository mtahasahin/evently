package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.ProfileDto;
import com.github.mtahasahin.evently.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfileMapper {

    ProfileMapper INSTANCE = Mappers.getMapper( ProfileMapper.class );

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "profilePublic", source = "isProfilePublic")
    void ProfileDtoToUserProfile(ProfileDto profileDto, @MappingTarget UserProfile profile);

    @Mapping(source = "profilePublic", target = "isProfilePublic")
    ProfileDto ProfileToProfileDto(UserProfile userProfile);
}
