package com.ltech.uaa.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ltech.uaa.model.AppRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@JsonSerialize
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
