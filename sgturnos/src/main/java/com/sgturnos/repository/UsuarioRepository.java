package com.sgturnos.repository;

import com.sgturnos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByCorreo(String correo);
    Usuario findByCorreoAndContrasena(String correo, String contrasena);
}