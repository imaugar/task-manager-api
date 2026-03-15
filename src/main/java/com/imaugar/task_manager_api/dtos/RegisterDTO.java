package com.imaugar.task_manager_api.dtos;

import lombok.Data;
import jakarta.validation.constraints.*;;

@Data
public class RegisterDTO {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    private String password;
}
