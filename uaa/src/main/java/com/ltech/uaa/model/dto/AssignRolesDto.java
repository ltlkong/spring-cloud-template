package com.ltech.uaa.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@JsonSerialize
@Data
@AllArgsConstructor
public class AssignRolesDto {
    private String userId;
    private Set<String> roleNames;
}
