package com.sgturnos.service;

import com.sgturnos.model.Colaborador;
import com.sgturnos.model.Usuario;
import com.sgturnos.repository.UsuarioRepository;
import com.sgturnos.repository.ColaboradorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
   
    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario save(Usuario usuario) {
        // 🔹 DEBUG TEMPORAL - INICIO
        System.out.println("=== DEBUG: TIPO DE ID ===");
    System.out.println("Valor: " + usuario.getIdUsuario());
    System.out.println("Tipo: " + (usuario.getIdUsuario() != null ? 
                      usuario.getIdUsuario().getClass().getName() : "null"));
        System.out.println("=== DEBUG: INTENTANDO GUARDAR USUARIO ===");
        System.out.println("ID: " + usuario.getIdUsuario());
        System.out.println("Nombre: " + usuario.getPrimerNombre());
        System.out.println("Correo: " + usuario.getCorreo());
        System.out.println("Rol: " + (usuario.getRol() != null ? usuario.getRol().getIdRol() : "null"));
        System.out.println("Contraseña: " + (usuario.getContrasena() != null ? "***" : "null"));
        // 🔹 DEBUG TEMPORAL - FIN

        // 🔹 Validar que el idUsuario (número de documento) no sea null
        if (usuario.getIdUsuario() == null) {
            throw new IllegalArgumentException("El número de documento (idUsuario) es obligatorio.");
        }

        // 🔹 Buscar si ya existe en BD por ID
        Usuario existente = usuarioRepository.findById(usuario.getIdUsuario()).orElse(null);

        if (existente == null) {
            // --- Nuevo usuario ---
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        } else {
            // --- Actualización ---
            if (!usuario.getContrasena().equals(existente.getContrasena())) {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            } else {
                usuario.setContrasena(existente.getContrasena());
            }
        }

        try {
            // --- Guardar usuario ---
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            System.out.println("✅ USUARIO GUARDADO EN BD EXITOSAMENTE");

            // --- Verificar si ya existe colaborador para este usuario ---
            Colaborador colaboradorExistente = colaboradorRepository.findByUsuario(usuarioGuardado);
            if (colaboradorExistente == null) {
                Colaborador colaborador = new Colaborador();
                colaborador.setUsuario(usuarioGuardado);
                colaborador.setRol(usuarioGuardado.getRol());
                colaboradorRepository.save(colaborador);
                System.out.println("✅ COLABORADOR CREADO EXITOSAMENTE");
            } else {
                System.out.println("ℹ️  COLABORADOR YA EXISTÍA");
            }

            return usuarioGuardado;

        } catch (Exception e) {
            System.out.println("❌ ERROR AL GUARDAR EN BD: " + e.getMessage());
            e.printStackTrace(); // ← ESTO ES CRUCIAL
            throw e; // Relanza la excepción para que el controller la capture
        }
    }

    @Override
    public void deleteByCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
        usuarioRepository.delete(usuario);
    }
    
    @Override
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);    
    }
}