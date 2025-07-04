package com.sgturnos.controller;

import com.sgturnos.model.Usuario;
import com.sgturnos.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String showForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", new String[]{"aux01", "enf02", "med03", "ter04"});
        return "usuarios/form";
    }

    @PostMapping
public String saveUsuario(@ModelAttribute Usuario usuario,
                          @RequestParam(value = "terminos", required = false) String terminos,
                          RedirectAttributes redirectAttributes) {
    if (terminos == null || !terminos.equals("aceptado")) {
        redirectAttributes.addFlashAttribute("error", "Debe aceptar los términos y condiciones");
        return "redirect:/usuarios/nuevo";
    }

    usuarioService.save(usuario);
    redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente");
    return "redirect:/usuarios";
}

    @GetMapping("/editar/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.findById(id));
        model.addAttribute("roles", new String[]{"aux01", "enf02", "med03", "ter04"});
        return "usuarios/form";
    }

    @GetMapping("/eliminar/{id}")
    public String deleteUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        return "redirect:/usuarios";
    }
}