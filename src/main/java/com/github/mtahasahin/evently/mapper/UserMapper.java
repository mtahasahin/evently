package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.UserDto;
import com.github.mtahasahin.evently.entity.AppUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ProfileMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mapping(source = "userProfile", target = "profile")
    UserDto userToUserDto(AppUser user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(source = "profile", target = "userProfile")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDto userDto, @MappingTarget AppUser user);
}