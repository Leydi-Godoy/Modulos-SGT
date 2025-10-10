package com.sgturnos.controller;

import com.sgturnos.model.Usuario;
import com.sgturnos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/dashboard_admin")
    public String mostrarDashboardAdmin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal(); // Usuario de Spring Security
        String username = user.getUsername();   // El correo
        Usuario usuario = usuarioRepository.findByCorreo(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));

        model.addAttribute("usuario", usuario);
        return "admin/dashboard_admin";
    }
    
   // 🔹 Endpoint adicional SOLO para pruebas en Postman
@GetMapping("/dashboard_admin/api")
@org.springframework.web.bind.annotation.ResponseBody
public Usuario mostrarDashboardAdminJson() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) auth.getPrincipal();
    String username = user.getUsername();
    return usuarioRepository.findByCorreo(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));
} 

    @GetMapping("/dashboard_usuario")
    public String mostrarDashboardUsuario(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        String username = user.getUsername();
        Usuario usuario = usuarioRepository.findByCorreo(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));

        model.addAttribute("usuario", usuario);
        return "usuario/dashboard_usuario";
    }
    
   // 🔹 Endpoint adicional SOLO para pruebas en Postman
@GetMapping("/dashboard_usuario/api")
@org.springframework.web.bind.annotation.ResponseBody
public Usuario mostrarDashboardUsuarioJson() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) auth.getPrincipal();
    String username = user.getUsername();
    return usuarioRepository.findByCorreo(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));
}

}