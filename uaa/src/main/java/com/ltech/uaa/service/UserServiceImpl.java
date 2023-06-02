package com.ltech.uaa.service;

import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.model.dto.AuthenticationResponseDto;
import com.ltech.uaa.model.dto.LoginRequestDto;
import com.ltech.uaa.model.dto.SignUpRequestDto;
import com.ltech.uaa.repository.UserRepository;
import com.ltech.uaa.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    @Value("${jwt.refreshTokenExpirationMs}")
    private int refreshTokenExpirationMs;

    public boolean usernameExists(String username) {
        Optional<AppUser> user = userRepository.findByUsername(username);

        return user.isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public AppUser registerUser(SignUpRequestDto signUpRequest) {

        AppUser user = new AppUser();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setLocked(false);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public AuthenticationResponseDto loginUser(LoginRequestDto loginRequest) {
        try {
            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            String access_token = tokenProvider.generateTokenWithExpirationAndSubject(authentication);
            String refresh_token = tokenProvider.generateToken(new Date(System.currentTimeMillis()+ refreshTokenExpirationMs), "refresh");

            return new AuthenticationResponseDto(access_token, refresh_token,"Success!");
        } catch (AuthenticationException e) {
            return null;
        }
    }
}
