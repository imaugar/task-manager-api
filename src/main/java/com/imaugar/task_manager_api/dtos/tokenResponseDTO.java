package com.imaugar.task_manager_api.dtos;

import lombok.Data;

@Data
public class tokenResponseDTO {
    private String token;

    public tokenResponseDTO(String token) {
        this.token = token;
    }
}
