package com.ltech.uaa.service;

import java.util.Set;

public interface RoleService {
    boolean assignUserRoles(String userId, Set<String> roleNames);
}
