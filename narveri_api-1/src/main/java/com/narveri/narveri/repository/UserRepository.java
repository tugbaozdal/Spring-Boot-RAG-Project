package com.narveri.narveri.repository;


import com.narveri.narveri.model.Role;
import com.narveri.narveri.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByRoles(Role role);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByPhone(String phone);

    boolean existsByPhoneOrEmail(String phone, String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailIgnoreCase(String email);

    Page<User> findAll(Pageable pageable);

    @Query(value = """
            select u from User u 
            where ('' = :searchText or :searchText is null 
                   or lower(concat(u.firstName, ' ', u.lastName)) like lower(concat('%', :searchText,'%'))
                   or lower(u.email) like lower(concat('%', :searchText,'%'))
                   or lower(u.phone) like lower(concat('%', :searchText,'%'))) 
             and ('' = :phone or :phone is null or lower(u.phone) like lower(concat('%', :phone,'%'))
                   )"""
    )
    Page<User> search(String searchText, String phone, Pageable pageable);


}
