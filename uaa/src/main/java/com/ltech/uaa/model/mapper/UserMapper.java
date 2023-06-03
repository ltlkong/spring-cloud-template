package com.ltech.uaa.model.mapper;

import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.model.dto.UserInfoDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserInfoDto mapAppUserToUserInfo(AppUser user) {
        UserInfoDto userInfo = new UserInfoDto(user.getUsername(), user.getEmail(), user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));

        return userInfo;
    }
}
