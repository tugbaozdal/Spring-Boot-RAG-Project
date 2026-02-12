package com.narveri.narveri.mapper;

import com.narveri.narveri.dto.RoleDto;
import com.narveri.narveri.model.Role;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDto, Role> {


}
