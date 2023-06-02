package com.ltech.uaa.model.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class SignUpRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
}