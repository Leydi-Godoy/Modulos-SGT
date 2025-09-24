package com.sgturnos.controller;

import com.sgturnos.model.MallaTurnos;
import com.sgturnos.repository.ColaboradorRepository;
import com.sgturnos.repository.TurnoRepository;
import com.sgturnos.service.MallaTurnosService;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/malla_turnos")
public class MallaTurnosController {

    private final MallaTurnosService mallaTurnosService;
    private final ColaboradorRepository colaboradorRepository;
    private final TurnoRepository turnoRepository;

    public MallaTurnosController(MallaTurnosService mallaTurnosService,
                                 ColaboradorRepository colaboradorRepository,
                                 TurnoRepository turnoRepository) {
        this.mallaTurnosService = mallaTurnosService;
        this.colaboradorRepository = colaboradorRepository;
        this.turnoRepository = turnoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mallas", mallaTurnosService.listar());
        return "admin/listar_mallas";
    }

    @GetMapping("/nueva")
    public String nuevaMalla(Model model) {
        model.addAttribute("malla", new MallaTurnos());
        model.addAttribute("colaboradores", colaboradorRepository.findAll());
        model.addAttribute("turnos", turnoRepository.findAll());
        return "admin/editar_malla";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        MallaTurnos malla = mallaTurnosService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Id inválido: " + id));
        model.addAttribute("malla", malla);
        model.addAttribute("colaboradores", colaboradorRepository.findAll());
        model.addAttribute("turnos", turnoRepository.findAll());
        return "admin/editar_malla";
    }

    @PostMapping("/generar")
    public String generarMalla(@RequestParam String mes,
                               @RequestParam String rol,
                               @RequestParam Long colaboradorId,
                               @RequestParam Long turnoId,
                               RedirectAttributes ra) {
        MallaTurnos malla = mallaTurnosService.generarYMantenerMalla(mes, rol);
        mallaTurnosService.asignarTurno(malla, colaboradorId, turnoId, LocalDate.now());

        ra.addFlashAttribute("mensaje", "✅ Malla generada y turno asignado correctamente");
        return "redirect:/malla_turnos";
    }

    @PostMapping("/guardar")
    public String guardarEdicion(@ModelAttribute("malla") MallaTurnos malla) {
        if (malla.getIdMalla() != null && malla.getIdMalla() == 0) {
            malla.setIdMalla(null);
        }
        if (malla.getIdMalla() == null) {
            malla.setEstado("GENERADA");
        }
        mallaTurnosService.guardar(malla);
        return "redirect:/malla_turnos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id) {
        mallaTurnosService.eliminar(id);
        return "redirect:/malla_turnos";
    }
}