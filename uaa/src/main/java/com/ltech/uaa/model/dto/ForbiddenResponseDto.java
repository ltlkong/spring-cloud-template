package com.ltech.uaa.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonSerialize
public class ForbiddenResponseDto {
    private String message;
    private String reason;
}
