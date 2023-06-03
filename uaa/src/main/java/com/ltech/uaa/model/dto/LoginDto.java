package com.ltech.uaa.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonSerialize
public class LoginDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
