/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Genera y valida los JWT
 * @author david
 */
@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(
            @Value("${seguridad.jwt.secret}") String secreto,
            @Value("${seguridad.jwt.expiracion-horas}") long expiracionHoras) {
        // Keys.hmacShaKeyFor exige que el secreto tenga al menos 256 bits (32 caracteres) HS256
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionHoras * 60 * 60 * 1000;
    }

    public String generarToken(String username, String rol) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + this.expiracionMs);

        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(this.clave)
                .compact();
    }

    public String extraerUsername(String token) {
        Claims claims = this.extraerTodosLosClaims(token);
        return claims.getSubject();
    }

    public String extraerRol(String token) {
        Claims claims = this.extraerTodosLosClaims(token);
        return claims.get("rol", String.class);
    }

    /**
     * verifica que el token esta bien firmado (con nuestra clave) y no ha
     * expirado.
     *
     * @param token a evaluar
     * @return true si el token esta bien firmado
     */
    public boolean esTokenValido(String token) {
        try {
            this.extraerTodosLosClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Firma invalida, token expirado, o formato corrupto: en cualquiera de estos casos, el token simplemente no es valido
            return false;
        }
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
