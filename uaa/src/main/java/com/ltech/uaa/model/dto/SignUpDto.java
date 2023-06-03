package com.ltech.uaa.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonSerialize
@AllArgsConstructor
public class SignUpDto {
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
}