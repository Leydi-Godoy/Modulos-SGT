package com.sgturnos.security;

import com.sgturnos.model.Usuario;
import com.sgturnos.repository.UsuarioRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

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

        // Obtener rol de la base de datos
        String rolEnBd = usuario.getRol();
        String rolSpring = null;

        // Convertir tus roles personalizados en roles Spring Security
        switch (rolEnBd.toLowerCase()) {
            case "aux01":
                rolSpring = "ROLE_AUX";
                break;
            case "enf02":
                rolSpring = "ROLE_ENF";
                break;
            case "med03":
                rolSpring = "ROLE_MED";
                break;
            case "ter04":
                rolSpring = "ROLE_TER";
                break;
                default:
    throw new UsernameNotFoundException("Rol no válido: " + rolEnBd);
        }

        String nombreRol = usuario.getRol();
if (nombreRol == null || nombreRol.isEmpty()) {
    throw new UsernameNotFoundException("Rol no encontrado para el usuario: " + usuario.getCorreo());
}

List<GrantedAuthority> authorities = new ArrayList<>();
authorities.add(new SimpleGrantedAuthority("ROLE_" + nombreRol.toUpperCase()));

return new User(
    usuario.getCorreo(),
    usuario.getContrasena(),
    authorities
);
    }
}