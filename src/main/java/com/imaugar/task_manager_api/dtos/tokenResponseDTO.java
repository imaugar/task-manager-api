package com.imaugar.task_manager_api.dtos;

import lombok.Data;

@Data
public class TokenResponseDTO {
    private String token;

    public TokenResponseDTO(String token) {
        this.token = token;
    }
}
