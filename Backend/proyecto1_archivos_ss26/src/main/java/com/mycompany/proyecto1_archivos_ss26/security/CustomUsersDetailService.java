/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.security;

import com.mycompany.proyecto1_archivos_ss26.entity.Usuario;
import com.mycompany.proyecto1_archivos_ss26.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Solo se usa en el momento del '/login' para que Spring Security valide las
 * credenciales iniciales desde la DB, NO en cada peticion posterior: una vez
 * emitido el JWT, el filtro (JwtAuthenticationFilter) ya no vuelve a pasar por
 * aqui.
 *
 * @author david
 */
@Service
public class CustomUsersDetailService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUsersDetailService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = this.usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con username '" + username + "'"));

        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("El usuario '" + username + "' esta desactivado");
        }

        // Spring Security exige que los roles/autoridades empiecen con el prefijo
        // "ROLE_" para que funcionen los helpers hasRole(...) en @PreAuthorize.
        String nombreRol = usuario.getRol().getNombreRol().toUpperCase();
        return new User(
                usuario.getUsername(),
                usuario.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + nombreRol)));
    }

}
