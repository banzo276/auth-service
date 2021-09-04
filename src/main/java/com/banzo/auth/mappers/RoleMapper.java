package com.banzo.auth.mappers;

import com.banzo.auth.dto.RoleDto;
import com.banzo.auth.model.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {

  RoleDto roleToRoleDto(Role role);

  Role roleDtoToRole(RoleDto roleDto);
}
