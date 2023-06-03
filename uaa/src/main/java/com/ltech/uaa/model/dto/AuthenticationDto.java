package com.ltech.uaa.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonSerialize
public class AuthenticationDto {
    private String access_token;
    private String refresh_token;
    private String message;

}
