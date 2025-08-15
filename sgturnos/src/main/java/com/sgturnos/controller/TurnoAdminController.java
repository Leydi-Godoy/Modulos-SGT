package com.sgturnos.controller;

import com.sgturnos.model.AsignacionTurno;
import com.sgturnos.model.Usuario;
import com.sgturnos.service.PlanificacionTurnosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TurnoAdminController {

    @Autowired
    private PlanificacionTurnosService planificacionService;

    // Página principal de planificación de turnos
    @GetMapping("/planificar_turnos")
    public String planificarTurnos(Model model) {
        List<Usuario> usuarios = planificacionService.listarUsuarios();
        List<AsignacionTurno> asignaciones = planificacionService.listarAsignaciones();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("asignaciones", asignaciones);
        model.addAttribute("turno", new AsignacionTurno()); // objeto vacío para el formulario

        return "planificar_turnos"; // tu HTML centralizado
    }

    // Guardar nueva asignación de turno
    @PostMapping("/planificar_turnos")
    public String guardarTurno(@RequestParam Long usuarioId,
                               @RequestParam String fecha,
                               @RequestParam String horaInicio,
                               @RequestParam String horaFin,
                               @RequestParam String area) {

        planificacionService.guardarAsignacion(usuarioId, fecha, horaInicio, horaFin, area);
        return "redirect:/admin/planificar_turnos";
    }

    // Editar asignación: mostrar datos en el formulario
    @GetMapping("/asignaciones/editar/{id}")
    public String editarAsignacion(@PathVariable Long id, Model model) {
        AsignacionTurno asignacion = planificacionService.obtenerPorId(id);
        List<Usuario> usuarios = planificacionService.listarUsuarios();

        model.addAttribute("asignacion", asignacion);
        model.addAttribute("usuarios", usuarios);

        return "planificar_turnos"; // misma página centralizada
    }

    // Guardar edición de asignación
    @PostMapping("/asignaciones/editar")
    public String guardarEdicion(@RequestParam Long id,
                                 @RequestParam Long usuarioId,
                                 @RequestParam String fecha,
                                 @RequestParam String horaInicio,
                                 @RequestParam String horaFin,
                                 @RequestParam String area) {

        planificacionService.editarAsignacion(id, usuarioId, fecha, horaInicio, horaFin, area);
        return "redirect:/admin/planificar_turnos";
    }

    // Eliminar asignación
    @GetMapping("/asignaciones/eliminar/{id}")
    public String eliminarAsignacion(@PathVariable Long id) {
        planificacionService.eliminarAsignacion(id);
        return "redirect:/admin/planificar_turnos";
    }

    // Ver detalle de asignación
    @GetMapping("/asignaciones/detalle/{id}")
    public String detalleAsignacion(@PathVariable Long id, Model model) {
        AsignacionTurno asignacion = planificacionService.obtenerPorId(id);
        model.addAttribute("asignacion", asignacion);

        return "planificar_turnos"; // misma página centralizada
    }
}