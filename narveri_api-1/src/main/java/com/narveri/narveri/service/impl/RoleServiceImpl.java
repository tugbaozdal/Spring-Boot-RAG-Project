package com.narveri.narveri.service.impl;

import com.narveri.narveri.dto.RoleDto;
import com.narveri.narveri.enums.ResponseMessageEnum;
import com.narveri.narveri.exception.BusinessException;
import com.narveri.narveri.mapper.PrivilegeMapper;
import com.narveri.narveri.model.Privilege;
import com.narveri.narveri.model.Role;
import com.narveri.narveri.model.User;
import com.narveri.narveri.repository.RoleRepository;
import com.narveri.narveri.repository.UserRepository;
import com.narveri.narveri.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrivilegeMapper privilegeMapper;

    @Override
    public boolean checkRolesExist(Set<RoleDto> roles) {
        List<Long> roleIds = roles.stream().map(RoleDto::getId).collect(Collectors.toList());
        return roleRepository.existsRolesByIdIn(roleIds);
    }

    @Override
    public List<Role> findAllById(List<Long> roleIds) {
        return roleRepository.findAllById(roleIds);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }


    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseMessageEnum.BACK_ROLE_MSG_001));
    }

    @Override
    public Role create(Role role) {
        controlUniqueName(role.getName(), -1l);
        return roleRepository.save(role);
    }

    @Override
    public Role update(Long id, RoleDto roleDto) {
        Role role = findById(id);
        if (StringUtils.isNotEmpty(roleDto.getName())) {
            controlUniqueName(roleDto.getName(), id);
            role.setName(roleDto.getName());
        }
        role.setPrivileges(privilegeMapper.toEntity(roleDto.getPrivileges()));
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        Role role = findById(id);
        List<User> assignedUsers = userRepository.findAllByRoles(role);
        if (!CollectionUtils.isEmpty(assignedUsers)) {
            throw new BusinessException(ResponseMessageEnum.BACK_ROLE_MSG_003);
        }

        roleRepository.deleteById(id);
    }



    @Override
    public List<Role> findAllByPrivileges(Privilege privilege) {
        return roleRepository.findAllByPrivileges(privilege);
    }

    @Override
    public List<Privilege> findPrivilegeByRoleId(Long roleId) {
        return roleRepository.findPrivilegeByRoleId(roleId);
    }

    private void controlUniqueName(String name, Long id) {
        if (roleRepository.existsByNameAndIdNot(name, id)) {
            throw new BusinessException(ResponseMessageEnum.BACK_ROLE_MSG_002);
        }
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }



}
