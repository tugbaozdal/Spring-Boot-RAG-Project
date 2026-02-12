package com.narveri.narveri.service.impl;

import com.narveri.narveri.dto.RegisterUser;
import com.narveri.narveri.dto.RoleDto;
import com.narveri.narveri.dto.UserDto;
import com.narveri.narveri.enums.ResponseMessageEnum;
import com.narveri.narveri.exception.BusinessException;
import com.narveri.narveri.exception.InvalidPasswordException;
import com.narveri.narveri.model.User;
import com.narveri.narveri.repository.UserRepository;
import com.narveri.narveri.service.RoleService;
import com.narveri.narveri.service.UserService;
import com.narveri.narveri.service.UserService;
import com.narveri.narveri.util.SecurityUtils;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Slf4j
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;


    @Override
    public boolean checkDuplicatePhoneOrEmail(String phone, String email) {
        return userRepository.existsByPhoneOrEmail(phone, email);
    }

    @Override
    public User register(RegisterUser registerUser) {
        if (checkDuplicatePhoneOrEmail(registerUser.getPhone(), registerUser.getEmail())) {
            throw new BusinessException(ResponseMessageEnum.BACK_USER_MSG_006);
        }
        User newUser = new User();
        if (StringUtils.isNotEmpty(registerUser.getEmail())) {
            newUser.setEmail(registerUser.getEmail().toLowerCase());
        }
        newUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        newUser.setFirstName(registerUser.getFirstName());
        newUser.setLastName(registerUser.getLastName());
        newUser.setPhone(registerUser.getPhone());
        newUser.setActive(true);
        try {
            userRepository.save(newUser);
        } catch (ConstraintViolationException ex) {
            throw new BusinessException(ex.getConstraintViolations().iterator().next().getMessage());
        }
        return newUser;

    }


    @Override
    public User findOneByPhone(String phone) {
        return userRepository.findOneByPhone(phone)
                .orElseThrow(() -> new BusinessException(ResponseMessageEnum.BACK_USER_MSG_001));
    }

    @Override
    public User findOneByEmail(String email) {
        return userRepository.findOneByEmail(email)
                .orElseThrow(() -> new BusinessException(ResponseMessageEnum.BACK_USER_MSG_001));

    }


    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseMessageEnum.BACK_USER_MSG_001));

    }




    @Override
    public Page<User> search(String searchText, String phoneNumber, Pageable pageable) {
        return userRepository.search(searchText, phoneNumber, pageable);
    }

    @Override
    public void changePassword(String currentClearTextPassword, String newPassword) {
        log.info("user: {}", SecurityUtils.getCurrentUsername());
        Optional.ofNullable(SecurityUtils.getCurrentUsername())
                .flatMap(userRepository::findOneByEmail)
                .ifPresent(user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    log.info("Kullanıcı şifresi değiştirildi. Kullanıcı: {}", user);
                });
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = findById(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        User user = null;
        String phone = SecurityUtils.getCurrentUsername();
        if (StringUtils.isNotEmpty(phone)) {
            user = userRepository.findOneByEmail(phone).orElse(null);
        }
        if (user == null) {
            throw new BusinessException(ResponseMessageEnum.BACK_USER_MSG_001);
        }
        return user;
    }

    @Override
    public User updateCurrentUser(UserDto userDto) {
        User user = getCurrentUser();
        User updatedUser = updateUser(user, userDto);
        userRepository.save(updatedUser);
        log.info("Kullanıcı güncellendi: {}", updatedUser.getPhone());
        return updatedUser;
    }

    @Override
    public User updateUserByAdmin(Long userId, UserDto userDto) {
        User updatedUser;
        User user;
        try {
            user = findById(userId);
            updatedUser = updateUser(user, userDto);
            if (userDto.getRoles() != null) {
                List<Long> roleIds = userDto.getRoles().stream().map(RoleDto::getId).collect(Collectors.toList());
                updatedUser.setRoles(new HashSet<>(roleService.findAllById(roleIds)));
            }
            if (userDto.getActive() != null) {
                updatedUser.setActive(userDto.getActive());
            }

            userRepository.save(updatedUser);
            log.info("Kullanıcı yönetici tarafından güncellendi: {}", updatedUser.getPhone());
        } catch (Exception e) {
            throw e;
        }
        return updatedUser;
    }

    @Override
    public void activateUser(Long userId) {
        User user = findById(userId);
        user.setActive(true);
        userRepository.save(user);
    }



    private void checkDuplicatePhone(String phone) {
        if (userRepository.existsByPhone(phone)) {
            throw new BusinessException(ResponseMessageEnum.BACK_USER_MSG_002);
        }
    }




    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException(ResponseMessageEnum.BACK_USER_MSG_003);
        }
    }

    private User updateUser(User updatedUser ,UserDto userDto){
        if (StringUtils.isNotEmpty(userDto.getFirstName())) {
            updatedUser.setFirstName(userDto.getFirstName());
        }
        if (StringUtils.isNotEmpty(userDto.getLastName())) {
            updatedUser.setLastName(userDto.getLastName());
        }
        return updatedUser;
    }



}
