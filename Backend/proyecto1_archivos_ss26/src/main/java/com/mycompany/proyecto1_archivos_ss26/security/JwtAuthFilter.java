/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * OncePerRequestFilter: garantiza que este filtro se ejecute UNA sola vez por
 * peticion. Este filtro NO toca la DB: confia en lo que el token ya trae
 * firmado (username + rol). Eso es lo que hace que la autenticacion sea
 * "stateless" en cada peticion.
 *
 * @author david
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (headerAuth == null || !headerAuth.startsWith(PREFIJO_BEARER)) {
            // No hay token: se deja pasar la peticion tal cual (sin autenticacion). Si el endpoint solicitado requiere estar
            // autenticado, SecurityConfig la rechazara mas adelante en la cadena con un 401 (via CustomAuthenticationEntryPoint). No
            // se rechaza aqui mismo porque /api/auth/login es publico y no necesita token.
            filterChain.doFilter(request, response);
            return;
        }

        String token = headerAuth.substring(PREFIJO_BEARER.length());

        if (this.jwtService.esTokenValido(token)) {
            String username = this.jwtService.extraerUsername(token);
            String rol = this.jwtService.extraerRol(token);

            // Convertir los permisos en GrantedAuthority de Spring Security para permitir/denegar el acceso a los recursos protecgidos
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));

            // El segundo parametro (credentials) va null a proposito: ya se valido la contraseña en el login para
            // obtener este token; aqui no hace falta volver a manejar una contraseña.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Autenticar al usuario dentro del contexto de la app
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // Si el token es invalido/expirado, simplemente no se establece autenticacion (mismo efecto que arriba en "No hay token":

        filterChain.doFilter(request, response);
    }

}
