package com.imaugar.task_manager_api.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    // Clave secreta y tiempo de expiración para el token JWT
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    // Crea el token JWT
    public String createToken(UserDetails userDetails) {
        return Jwts.builder()
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(getSecretKey())
                    .compact();
    }

    // Obtener el username del token
    public String getUsernameFromToken(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Comprobar si el token es válido
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Comprobar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    //Metodo para extraer cualquier claim del token
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        return claimsResolver.apply(claims);
    }

    // Clave para firmar el token JWT
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(this.secretKey.getBytes());
    }
}
