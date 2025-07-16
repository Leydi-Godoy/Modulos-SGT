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

        // Verificamos si estamos editando (usuario ya tiene ID)
        if (usuario.getIdUsuario() != null) {
            Usuario usuarioExistente = usuarioService.findById(usuario.getIdUsuario());

            // Si la contraseña del formulario es diferente a la guardada en BD, significa que la cambiaron
            if (!usuario.getContrasena().equals(usuarioExistente.getContrasena())) {
                String hashedPassword = passwordEncoder.encode(usuario.getContrasena());
                usuario.setContrasena(hashedPassword);
            }
        } else {
            // Si es un usuario nuevo, siempre se encripta la contraseña
            String hashedPassword = passwordEncoder.encode(usuario.getContrasena());
            usuario.setContrasena(hashedPassword);
        }

        // Guardamos el usuario con el rol seleccionado, no es necesario reasignar el rol aquí
        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        // Cargamos el usuario existente para la edición
        model.addAttribute("usuario", usuarioService.findById(id));
        // También pasamos todos los roles disponibles para la edición
        model.addAttribute("roles", new String[]{"aux01", "enf02", "med03", "ter04", "adm05"});
        return "usuarios/form";
    }

    @GetMapping("/eliminar/{id}")
    public String deleteUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Eliminar el usuario
        usuarioService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        return "redirect:/usuarios";
    }
}