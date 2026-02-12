package com.narveri.narveri.mapper;

import com.narveri.narveri.dto.PrivilegeDto;
import com.narveri.narveri.model.Privilege;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper extends EntityMapper<PrivilegeDto, Privilege> {
    PrivilegeDto toDto(Privilege privilege);

    Privilege toEntity(PrivilegeDto privilegeDto);

    Set<Privilege> toEntity(Set<PrivilegeDto> privilegeDtoList);

    Set<PrivilegeDto> toDto(Set<Privilege> privilegeList);
}
