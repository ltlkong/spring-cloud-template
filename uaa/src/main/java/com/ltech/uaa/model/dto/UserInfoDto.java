package com.ltech.uaa.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ltech.uaa.model.AppRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@JsonSerialize
@AllArgsConstructor
public class UserInfoDto {
    private String username;

    private String email;

    private Set<String> roles;
}
