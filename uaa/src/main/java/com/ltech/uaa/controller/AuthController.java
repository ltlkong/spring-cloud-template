package com.ltech.uaa.controller;

import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.model.dto.AuthenticationResponseDto;
import com.ltech.uaa.model.dto.LoginRequestDto;
import com.ltech.uaa.model.dto.SignUpRequestDto;
import com.ltech.uaa.service.UserService;
import com.ltech.uaa.util.VerifyUserInfoChain;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final VerifyUserInfoChain verifyUserInfoChain;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequestDto loginRequest) {
        AuthenticationResponseDto responseBody =  userService.loginUser(loginRequest);
        if(responseBody != null)
            return ResponseEntity.ok().body(responseBody);

        else return ResponseEntity.status(401).body("Invalid username or password");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpRequestDto signUpRequest) {
        if (userService.usernameExists(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (userService.emailExists(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        userService.registerUser(signUpRequest);

        return ResponseEntity.ok().build();
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
