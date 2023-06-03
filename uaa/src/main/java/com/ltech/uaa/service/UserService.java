package com.ltech.uaa.service;

import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.model.dto.AuthenticationDto;
import com.ltech.uaa.model.dto.LoginDto;
import com.ltech.uaa.model.dto.SignUpDto;

import java.util.Set;

public interface UserService {
    AppUser registerUser(SignUpDto signUpRequest);
    AuthenticationDto loginUser(LoginDto loginRequest);
    boolean assignUserRoles(String userId, Set<String> roleNames);

}
