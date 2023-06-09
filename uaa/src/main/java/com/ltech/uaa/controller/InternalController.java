package com.ltech.uaa.controller;

import com.ltech.uaa.model.dto.AuthenticationDto;
import com.ltech.uaa.model.dto.ErrorDto;
import com.ltech.uaa.model.dto.LoginDto;
import com.ltech.uaa.model.dto.SignUpDto;
import com.ltech.uaa.model.mapper.UserMapper;
import com.ltech.uaa.repository.UserRepository;
import com.ltech.uaa.service.AuthService;
import com.ltech.uaa.util.JwtTokenProvider;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
public class InternalController {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    private final AuthService authService;

    public InternalController(UserMapper userMapper, UserRepository userRepository, JwtTokenProvider tokenProvider, @Qualifier("InternalAuthService") AuthService authService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authService = authService;
    }

    @PreAuthorize("hasAuthority('INTERNAL')")
    @GetMapping("/user/{token}")
    public ResponseEntity<?> getInternalUserInfo(@NotNull @PathVariable String token) {
        return ResponseEntity.ok().body(userMapper.mapAppUserToInternalUserInfo(userRepository.getReferenceById(tokenProvider.getUserIdFromToken(token))));
    }

    @PostAuthorize("hasAuthority('INTERNAL')")
    @PostMapping
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
            authService.register(signUpRequest);

            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(403).body(new ErrorDto(ex.getMessage(), ex.getClass().getSimpleName()));
        }
    }

}
