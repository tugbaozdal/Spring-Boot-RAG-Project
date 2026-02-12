package com.narveri.narveri.repository;

import com.narveri.narveri.model.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    boolean existsByNameAndIdNot(String name, Long id);

    Optional<Privilege> findByName(String name);

    Page<Privilege> findAllByOrderByName(Pageable pageable);
}

