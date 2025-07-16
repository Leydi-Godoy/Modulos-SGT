package com.sgturnos.controller;

import com.sgturnos.model.Usuario;
import com.sgturnos.service.UsuarioService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String showForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        // Aquí pasamos todos los roles disponibles
        model.addAttribute("roles", new String[]{"aux01", "enf02", "med03", "ter04", "adm05"});
        return "usuarios/form";
    }

    @PostMapping
public String saveUsuario(@ModelAttribute Usuario usuario,
                          @RequestParam(value = "terminos", required = false) String terminos,
                          RedirectAttributes redirectAttributes) {

    // Verificamos si aceptaron los términos y condiciones
    if (terminos == null || !terminos.equals("aceptado")) {
        redirectAttributes.addFlashAttribute("error", "Debe aceptar los términos y condiciones");
        return "redirect:/usuarios/nuevo";
    }

    if (usuario.getIdUsuario() != null) {
        Usuario usuarioExistente = usuarioService.findById(usuario.getIdUsuario());

        if (usuarioExistente != null) {
            if (!usuario.getContrasena().equals(usuarioExistente.getContrasena())) {
                String hashedPassword = passwordEncoder.encode(usuario.getContrasena());
                usuario.setContrasena(hashedPassword);
            } else {
                usuario.setContrasena(usuarioExistente.getContrasena());
            }
        } else {
            String hashedPassword = passwordEncoder.encode(usuario.getContrasena());
            usuario.setContrasena(hashedPassword);
        }
    } else {
        String hashedPassword = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(hashedPassword);
    }

    usuarioService.save(usuario);
    redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente");
    return "redirect:/usuarios";
}
}