package com.ltech.uaa.controller;

import com.ltech.uaa.model.UserPrincipal;
import com.ltech.uaa.model.dto.*;
import com.ltech.uaa.model.mapper.UserMapper;
import com.ltech.uaa.repository.UserRepository;
import com.ltech.uaa.service.UserService;
import com.ltech.uaa.util.VerifyUserChain;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final VerifyUserChain verifyUserChain;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginDto loginRequest) {
        try {
            AuthenticationDto responseBody =  userService.loginUser(loginRequest);

            return ResponseEntity.ok().body(responseBody);
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(new ErrorDto(ex.getMessage(), ex.getClass().getSimpleName()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpDto signUpRequest) {
        try {
            verifyUserChain
                    .password(signUpRequest.getPassword())
                    .email(signUpRequest.getEmail())
                    .verifyPassword()
                    .verifyEmail()
                    .isValid();

            userService.registerUser(signUpRequest);

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test1")
    public ResponseEntity<?> test() {

        return ResponseEntity.ok().body("UAA service authenticated");
    }

    @GetMapping("/test2")
    public ResponseEntity<?> test2() {

        return ResponseEntity.ok().body("UAA service");
    }
}
