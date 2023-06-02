package com.ltech.uaa.service;

import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.model.dto.AuthenticationResponseDto;
import com.ltech.uaa.model.dto.LoginRequestDto;
import com.ltech.uaa.model.dto.SignUpRequestDto;

public interface UserService {
    boolean usernameExists(String username);
    boolean emailExists(String email);
    AppUser registerUser(SignUpRequestDto signUpRequest);
    AuthenticationResponseDto loginUser(LoginRequestDto loginRequest);

}
