package com.imaugar.task_manager_api.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import java.util.function.Function;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.imaugar.task_manager_api.enums.Role;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String createToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replace("ROLE_", "")) // Eliminar el prefijo ROLE_
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene rol asignado"));

        return Jwts.builder()
                    .subject(userDetails.getUsername())
                    .claim("role", role)       
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(getSecretKey())
                    .compact();
    }

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Devuelve directamente el enum Role
    public Role getRoleFromToken(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        return Role.valueOf(role);        
    }

    // Devuelve el rol (CON PREFIJO ROLE_) como GrantedAuthority para el SecurityContext
    public GrantedAuthority getAuthorityFromToken(String token) {
        return new SimpleGrantedAuthority("ROLE_" + getRoleFromToken(token).name());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(this.secretKey.getBytes());
    }
}
