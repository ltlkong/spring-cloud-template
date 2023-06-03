package com.ltech.uaa.service;

import com.ltech.uaa.model.AppRole;
import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.model.dto.AuthenticationDto;
import com.ltech.uaa.model.dto.LoginDto;
import com.ltech.uaa.model.dto.SignUpDto;
import com.ltech.uaa.repository.RoleRepository;
import com.ltech.uaa.repository.UserRepository;
import com.ltech.uaa.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Value("${jwt.refreshTokenExpirationMs}")
    private int refreshTokenExpirationMs;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public AppUser registerUser(SignUpDto signUpRequest) {
        if(userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) throw new IllegalArgumentException("Email exits");
        if(userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) throw new IllegalArgumentException("Username exits");

        AppUser user = new AppUser();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setLocked(false);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public AuthenticationDto loginUser(LoginDto loginRequest) {
        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String access_token = tokenProvider.generateTokenWithExpirationAndSubject(authentication);
        String refresh_token = tokenProvider.generateToken(new Date(System.currentTimeMillis()+ refreshTokenExpirationMs), "refresh");

        return new AuthenticationDto(access_token, refresh_token,"Success!");
    }
    public boolean assignUserRoles(String userId,  Set<String> roleNames) {
        AppUser user = userRepository.getReferenceById(userId);
        Set<AppRole> roles = roleRepository.findByNameIn(roleNames);

        if(!roles.isEmpty()) {
            user.setRoles(roles);
            return true;
        }
        return false;
    }
}
