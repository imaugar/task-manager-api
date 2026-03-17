package com.imaugar.task_manager_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.imaugar.task_manager_api.services.UserDetailService;
import com.imaugar.task_manager_api.services.JwtService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ApiGuard {
    private final UserDetailService userDetailService;
    private final JwtService jwtService;

    public ApiGuard(UserDetailService userDetailService, JwtService jwtService) {
        this.userDetailService = userDetailService;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Si la ruta empieza por la de auth, no requiere autenticación previa
                .requestMatchers("/api/auth/**").permitAll()
                //El resto de rutas requieren auth
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(
                new JwtRequestFilter(jwtService, userDetailService),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

}
