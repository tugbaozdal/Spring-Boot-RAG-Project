package com.narveri.narveri.service;

import com.narveri.narveri.dto.RoleDto;
import com.narveri.narveri.model.Privilege;
import com.narveri.narveri.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    boolean checkRolesExist(Set<RoleDto> roles);

    List<Role> findAllById(List<Long> roleIds);

    List<Role> findAll();

    Role findById(Long id);

    Role create(Role role);

    Role update(Long id, RoleDto roleDto);

    void deleteById(Long id);

    List<Role> findAllByPrivileges(Privilege privilege);

    List<Privilege> findPrivilegeByRoleId(Long roleId);

    Role findByName(String name);
}
