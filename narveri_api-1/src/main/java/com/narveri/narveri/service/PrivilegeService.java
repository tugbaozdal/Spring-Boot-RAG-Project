package com.narveri.narveri.service;

import com.narveri.narveri.dto.PrivilegeDto;
import com.narveri.narveri.model.Privilege;

import java.util.List;

public interface PrivilegeService {
    List<Privilege> findAll();

    Privilege findById(Long id);

    List<Privilege> findAllById(List<Long> ids);

    Privilege create(Privilege privilege);

    Privilege update(Long id, PrivilegeDto privilegeDto);

    void deleteById(Long id);
}
