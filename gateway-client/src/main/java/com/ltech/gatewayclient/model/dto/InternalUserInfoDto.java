package com.ltech.gatewayclient.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InternalUserInfoDto{

    private String userId;

    private String username;

    private String email;

    private String password;

    private boolean locked;

    private boolean enabled;

    private Set<String> roles;
}
