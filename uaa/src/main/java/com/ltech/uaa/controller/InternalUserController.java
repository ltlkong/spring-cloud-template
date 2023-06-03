package com.ltech.uaa.controller;

import com.ltech.uaa.model.UserPrincipal;
import com.ltech.uaa.model.dto.AssignRolesDto;
import com.ltech.uaa.model.dto.UserInfoDto;
import com.ltech.uaa.model.mapper.UserMapper;
import com.ltech.uaa.repository.RoleRepository;
import com.ltech.uaa.repository.UserRepository;
import com.ltech.uaa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal")
@AllArgsConstructor
@PreAuthorize("hasAuthority('INTERNAL')")
public class InternalUserController {
    private final RoleRepository roleRepository;

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(String userId) {
        return ResponseEntity.ok().body(roleRepository.findByUserId(userId));
    }
}
