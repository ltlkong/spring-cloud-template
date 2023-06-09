package com.ltech.uaa.service;

import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.model.dto.AuthenticationDto;
import com.ltech.uaa.model.dto.LoginDto;
import com.ltech.uaa.model.dto.SignUpDto;

public interface AuthService {
    AppUser register(SignUpDto signUpRequest);
    AuthenticationDto login(LoginDto loginRequest);

}
