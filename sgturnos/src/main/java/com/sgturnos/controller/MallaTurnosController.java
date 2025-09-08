package com.sgturnos.controller;

import com.sgturnos.model.MallaTurnos;
import com.sgturnos.repository.TurnoRepository;
import com.sgturnos.repository.UsuarioRepository;
import com.sgturnos.service.MallaTurnosService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/malla_turnos")
public class MallaTurnosController {

    private final MallaTurnosService mallaTurnosService;
    private final UsuarioRepository usuarioRepository;  // inyección
    private final TurnoRepository turnoRepository;      // inyección

    public MallaTurnosController(MallaTurnosService mallaTurnosService,
                                 UsuarioRepository usuarioRepository,
                                 TurnoRepository turnoRepository) {
        this.mallaTurnosService = mallaTurnosService;
        this.usuarioRepository = usuarioRepository;
        this.turnoRepository = turnoRepository;
    }

    // Listar mallas
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mallas", mallaTurnosService.listar());
        return "admin/listar_mallas";
    }

    // Formulario nueva malla
   @GetMapping("/nueva")
public String nuevaMalla(Model model) {
    model.addAttribute("malla", new MallaTurnos());
    model.addAttribute("usuarios", usuarioRepository.findAll()); // lista de usuarios
    model.addAttribute("turnos", turnoRepository.findAll());     // lista de turnos
    return "admin/editar_malla";
}

@GetMapping("/editar/{id}")
public String editar(@PathVariable("id") Long id, Model model) {
    MallaTurnos malla = mallaTurnosService.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Id inválido: " + id));
    model.addAttribute("malla", malla);
    model.addAttribute("usuarios", usuarioRepository.findAll()); // lista de usuarios
    model.addAttribute("turnos", turnoRepository.findAll());     // lista de turnos
    return "admin/editar_malla";
}
    
   @PostMapping("/guardar")
public String guardarEdicion(@ModelAttribute("malla") MallaTurnos malla) {
    mallaTurnosService.guardar(malla);
    return "redirect:/malla_turnos";
}

    // Eliminar malla
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id) {
        mallaTurnosService.eliminar(id);
        return "redirect:/malla_turnos";
    }
}