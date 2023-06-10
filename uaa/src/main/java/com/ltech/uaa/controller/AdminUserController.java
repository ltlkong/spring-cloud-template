package com.ltech.uaa.controller;

import com.ltech.uaa.model.dto.AssignRolesDto;
import com.ltech.uaa.model.dto.UserInfoDto;
import com.ltech.uaa.model.mapper.UserMapper;
import com.ltech.uaa.repository.UserRepository;
import com.ltech.uaa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ACCOUNT_ADMIN')")
public class AdminUserController {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;

    @PostMapping("/role/assign")
    public ResponseEntity<?> assignRolesToUser(@RequestBody AssignRolesDto assignRolesDto) {
        boolean isSuccess = roleService.assignUserRoles(assignRolesDto.getUserId(), assignRolesDto.getRoleNames());

        if(isSuccess) return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().body("Role doesn't exist");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers() {
        Set<UserInfoDto> userInfos = userRepository
                .findAll()
                .stream()
                .map(userMapper::mapAppUserToUserInfo)
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(userInfos);
    }
}
