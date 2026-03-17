package com.imaugar.task_manager_api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.imaugar.task_manager_api.dtos.RegisterDTO;
import com.imaugar.task_manager_api.dtos.LoginDTO;
import com.imaugar.task_manager_api.dtos.TokenResponseDTO;
import com.imaugar.task_manager_api.entities.User;
import com.imaugar.task_manager_api.enums.Role;
import com.imaugar.task_manager_api.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    //Registro
    public TokenResponseDTO register(RegisterDTO registerData) {
        User user = new User();
        user.setUsername(registerData.getUsername());
        user.setEmail(registerData.getEmail());
        user.setPassword(passwordEncoder.encode(registerData.getPassword()));
        user.setRole(Role.USER);
        //Guardamos el usuario en la base de datos
        userRepository.save(user);
        String token = jwtService.createToken(user);
        return new TokenResponseDTO(token);
    }

    //Login
    public TokenResponseDTO login(LoginDTO loginData){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword()));
        //Buscar user en la base de datos para generar el token
        User user = userRepository.findByUsername(loginData.getUsername()).orElseThrow();
        String token = jwtService.createToken(user);
        return new TokenResponseDTO(token);
    }
}
