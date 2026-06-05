package com.nttdata.ecopetrol.talento.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.security.Key;

@Component
public class JwtUtil {

    // Clave secreta fuerte (32 caracteres mínimo). Configurable vía JWT_SECRET.
    @Value("${jwt.secret:bS7Gz2HkX3NdP8aQrLvEw5jFu6VmY1Tp}")
    private String SECRET_KEY;

    // Vigencia del token en milisegundos. Configurable vía JWT_EXPIRATION_MS (default 6 h).
    @Value("${jwt.expiration-ms:21600000}")
    private long EXPIRATION_MS;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Genera JWT (mínimo: usuario y rol)
    public String generateToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String extractRol(String token) {
        Object rol = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().get("rol");
        return rol != null ? rol.toString() : null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}