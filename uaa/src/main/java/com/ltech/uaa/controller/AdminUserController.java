package com.ltech.uaa.controller;

import com.ltech.uaa.model.UserPrincipal;
import com.ltech.uaa.model.dto.AssignRolesDto;
import com.ltech.uaa.model.dto.UserInfoDto;
import com.ltech.uaa.model.mapper.UserMapper;
import com.ltech.uaa.repository.UserRepository;
import com.ltech.uaa.service.UserService;
import com.ltech.uaa.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ACCOUNT_ADMIN')")
public class AdminUserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/role/assign")
    public ResponseEntity<?> assignRolesToUser(@RequestBody AssignRolesDto assignRolesDto) {
        boolean isSuccess = userService.assignUserRoles(assignRolesDto.getUserId(), assignRolesDto.getRoleNames());

        if(isSuccess) return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().body("Role doesn't exist");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers() {
        Set<UserInfoDto> userInfos = userRepository
                .findAll()
                .stream()
                .map((user) -> userMapper.mapAppUserToUserInfo(user))
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(userInfos);
    }
}
