package com.ltech.uaa.model.mapper;

import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.model.dto.InternalUserInfoDto;
import com.ltech.uaa.model.dto.UserInfoDto;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserInfoDto mapAppUserToUserInfo(AppUser user) {
        UserInfoDto userInfo = new UserInfoDto(user.getId(),user.getUsername(), user.getEmail(), user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));

        return userInfo;
    }
    public InternalUserInfoDto mapAppUserToInternalUserInfo(AppUser user) {
        Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());

        InternalUserInfoDto internalUserInfoDto = new InternalUserInfoDto(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.isLocked(), user.isEnabled(), roles);

        return internalUserInfoDto;
    }
}
