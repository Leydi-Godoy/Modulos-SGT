package com.sgturnos.service;

import com.sgturnos.model.Usuario;
import com.sgturnos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        if (usuario.getIdUsuario() != null) {
            Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario()).orElse(null);
            if (usuarioExistente != null) {
                if (!passwordEncoder.matches(usuario.getContrasena(), usuarioExistente.getContrasena())) {
                    usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
                } else {
                    usuario.setContrasena(usuarioExistente.getContrasena());
                }
            } else {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        } else {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}