package com.ltech.uaa.model.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
