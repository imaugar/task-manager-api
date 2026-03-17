package com.imaugar.task_manager_api.facades;

import com.imaugar.task_manager_api.dtos.TokenResponseDTO;
import com.imaugar.task_manager_api.dtos.RegisterDTO;
import com.imaugar.task_manager_api.dtos.LoginDTO;
import com.imaugar.task_manager_api.services.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@Valid @RequestBody RegisterDTO request) {
            //TODO: Implementar captura de errores HTTP
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginDTO request) {
        //TODO: Implementar captura de errores HTTP
        return ResponseEntity.ok(authService.login(request));
    }
}
