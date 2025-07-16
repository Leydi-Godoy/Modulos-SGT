package com.sgturnos.controller;

import com.sgturnos.model.Usuario;
import com.sgturnos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @GetMapping("/sgturnos/login")
    public String mostrarFormularioLogin() {
        return "login"; // tu template
    }

    @GetMapping("/dashboard_admin")
    public String dashboardAdmin() {
        return "login/dashboard_admin";
    }

    @GetMapping("/dashboard_usuario")
    public String dashboardUsuario() {
        return "login/dashboard_usuario";
    }

    @GetMapping("/login/error_rol")
    public String errorRol() {
    return "login/error_rol";
  }
}