package com.narveri.narveri.service;

import com.narveri.narveri.dto.RegisterUser;
import com.narveri.narveri.dto.UserDto;
import com.narveri.narveri.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface UserService {

    boolean checkDuplicatePhoneOrEmail(String phone, String email);

    User register(RegisterUser registerUser);

    User findOneByPhone(String phone);

    User findOneByEmail(String loginOrEmail);

//    Optional<User> findByEmail(String loginOrEmail);

    User findById(Long id);


    Page<User> search(String searchText, String phoneNumber, Pageable pageable);

    void changePassword(String currentClearTextPassword, String newPassword);

    void deactivateUser(Long userId);

    User getCurrentUser();

    User updateCurrentUser(UserDto userDto);

    User updateUserByAdmin(Long userId, UserDto userDto);

    void activateUser(Long userId);




}
