package com.ltech.uaa.service;

import com.ltech.uaa.model.AppRole;
import com.ltech.uaa.model.AppUser;
import com.ltech.uaa.repository.RoleRepository;
import com.ltech.uaa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
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
