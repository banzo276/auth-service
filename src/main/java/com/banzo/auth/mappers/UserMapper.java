package com.banzo.auth.mappers;

import com.banzo.auth.dto.UserDto;
import com.banzo.auth.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

  UserDto userToUserDto(User user);

  User userDtoToUser(UserDto userDto);
}
