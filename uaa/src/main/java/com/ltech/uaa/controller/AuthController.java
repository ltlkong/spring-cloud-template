package com.ltech.uaa.controller;

import com.ltech.uaa.model.UserPrincipal;
import com.ltech.uaa.model.dto.*;
import com.ltech.uaa.model.mapper.UserMapper;
import com.ltech.uaa.repository.UserRepository;
import com.ltech.uaa.service.AuthService;
import com.ltech.uaa.util.VerifyUserChain;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;
    private final VerifyUserChain verifyUserChain;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthController(@Qualifier("UserAuthService") AuthService authService, VerifyUserChain verifyUserChain, UserRepository userRepository, UserMapper userMapper) {
        this.authService = authService;
        this.verifyUserChain = verifyUserChain;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginDto loginRequest) {
        try {
            AuthenticationDto responseBody =  authService.login(loginRequest);

            return ResponseEntity.ok().body(responseBody);
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(new ErrorDto(ex.getMessage(), ex.getClass().getSimpleName()));
        }
    }



    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody SignUpDto signUpRequest) {
        try {
            verifyUserChain
                    .password(signUpRequest.getPassword())
                    .email(signUpRequest.getEmail())
                    .verifyPassword()
                    .verifyEmail()
                    .isValid();

            authService.register(signUpRequest);

            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(403).body(new ErrorDto(ex.getMessage(), ex.getClass().getSimpleName()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public  ResponseEntity<?> me(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserInfoDto userInfo = userMapper.mapAppUserToUserInfo(userRepository.getReferenceById(userPrincipal.getId()));

        return ResponseEntity.ok().body(userInfo);
    }
}
