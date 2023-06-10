package com.ltech.uaa.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@JsonSerialize
@AllArgsConstructor
public class UserInfoDto {
    private String userId;
    private String username;
    private String email;
    private Set<String> roles;
}
