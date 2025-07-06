package com.sgturnos.controller;

import com.sgturnos.model.Usuario;
import com.sgturnos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login/login"; // Busca templates/login/login.html
    }

    // Procesar login
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo,
                                @RequestParam("contrasena") String contrasena,
                                Model model) {

        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {
                return "redirect:/dashboard_admin";
            } else {
                return "redirect:/dashboard_usuario";
            }
        }

        // Si el login falla
        model.addAttribute("error", "Correo o contraseña incorrectos");
        return "login/login";
    }

    // Dashboard administrador
    @GetMapping("/dashboard_admin")
    public String dashboardAdmin() {
        return "login/dashboard_admin"; // Busca templates/login/dashboard_admin.html
    }

    // Dashboard usuario
    @GetMapping("/dashboard_usuario")
    public String dashboardUsuario() {
        return "login/dashboard_usuario"; // Busca templates/login/dashboard_usuario.html
    }
}