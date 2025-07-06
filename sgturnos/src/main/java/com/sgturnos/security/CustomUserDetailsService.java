package com.sgturnos.security;

import com.sgturnos.model.Usuario;
import com.sgturnos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
        }

        // Ejemplo básico: usar "ROLE_ADMIN" o "ROLE_USER" desde el campo 'rol' si lo tienes en tu entidad Usuario
        // Si aún no tienes roles en la base, puedes asignar por defecto "ROLE_USER"
        String rol = "ROLE_USER";
        if (usuario.getRol() != null && usuario.getRol().equalsIgnoreCase("admin")) {
            rol = "ROLE_ADMIN";
        }

        return new User(
                usuario.getCorreo(),
                usuario.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(rol))
        );
    }
}