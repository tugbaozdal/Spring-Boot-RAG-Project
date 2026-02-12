package com.narveri.narveri.controller;


import com.narveri.narveri.dto.AuthToken;
import com.narveri.narveri.dto.BaseResponseDto;
import com.narveri.narveri.dto.RegisterUser;
import com.narveri.narveri.enums.ResponseMessageEnum;
import com.narveri.narveri.exception.BusinessException;
import com.narveri.narveri.security.jwt.AuthRequest;
import com.narveri.narveri.security.jwt.TokenProvider;
import com.narveri.narveri.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Auth-Api")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public AuthToken login(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Login: {}", authRequest);
        AuthToken authToken;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
            log.info("auth : {} ", authenticationToken);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("auth : {} ", authentication.getDetails());
            authToken = tokenProvider.createToken(authentication);
            log.info("auth token : {}", authToken.getJwtToken());
        } catch (AuthenticationException e) {
            throw new BusinessException(ResponseMessageEnum.BACK_USER_MSG_007);
        }

        log.info("Login başarılı. Email: {}", authRequest.getEmail());
        return authToken;
    }

    @PostMapping("/register")
    public BaseResponseDto registerUser(@RequestBody RegisterUser registerUser) {
        userService.register(registerUser);
        return BaseResponseDto.builder().message("Kullanıcı oluşturma işleminiz başarılı gerçekleşmiştir.")
                .statusCode(HttpStatus.CREATED.value()).status(HttpStatus.CREATED.name()).build();
    }

}