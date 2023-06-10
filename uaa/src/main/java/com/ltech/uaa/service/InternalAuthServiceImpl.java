package com.ltech.uaa.service;

import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.model.UserPrincipal;
import com.ltech.uaa.model.dto.AuthenticationDto;
import com.ltech.uaa.model.dto.LoginDto;
import com.ltech.uaa.model.dto.SignUpDto;
import com.ltech.uaa.repository.UserRepository;
import com.ltech.uaa.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service("InternalAuthService")
public class InternalAuthServiceImpl implements AuthService{
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    @Value("#{'${service.names}'.split(',')}")
    private List<String> internalUserNames;
    @Override
    public AppUser register(SignUpDto signUpRequest) {
        if(!internalUserNames.contains(signUpRequest.getKey())) throw new BadCredentialsException("Access denied");
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

    @Override
    public AuthenticationDto login(LoginDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if(!userPrincipal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("INTERNAL"))) {
            throw new BadCredentialsException("Permission denied");
        }

        // Ten years
        Date expiryDate = new Date(new Date().getTime() + 86400000 * 365 * 10);

        String access_token = tokenProvider.generateToken( expiryDate , userPrincipal);
        String refresh_token = tokenProvider.generateRefreshToken(authentication);

        return new AuthenticationDto(access_token, refresh_token,"Success!");
    }
}
