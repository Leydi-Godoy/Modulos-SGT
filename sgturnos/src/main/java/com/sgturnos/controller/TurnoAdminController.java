package com.sgturnos.controller;

import com.sgturnos.model.AsignacionTurno;
import com.sgturnos.service.PlanificacionTurnosService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TurnoAdminController {

    @Autowired
    private PlanificacionTurnosService planificacionService;

    // Página principal de planificación
    @GetMapping("/planificar_turnos")
    public String planificarTurnos(Model model) {
        model.addAttribute("colaboradores", planificacionService.listarColaboradores()); // Cambiado
        model.addAttribute("horarios", planificacionService.listarHorarios());
        model.addAttribute("asignaciones", planificacionService.listarAsignaciones());
        model.addAttribute("asignacionTurno", null);
        return "admin/planificar_turnos";
    }

    // Guardar asignación manual
    @PostMapping("/planificar_turnos")
    public String guardarTurno(@RequestParam("colaboradorId") Long idColaborador, // Cambiado
                               @RequestParam("turnoId") Long idTurno,
                               @RequestParam String fecha,
                               @RequestParam(required = false) String observaciones) {
        planificacionService.guardarAsignacion(idColaborador, idTurno, LocalDate.parse(fecha), observaciones); // Cambiado
        return "redirect:/admin/planificar_turnos";
    }

    // Cargar asignación en modo edición
    @GetMapping("/asignaciones/editar")
    public String editarAsignacion(@RequestParam("colaboradorId") Long idColaborador, // Cambiado
                                   @RequestParam("turnoId") Long idTurno,
                                   @RequestParam String fecha,
                                   Model model) {
        AsignacionTurno asignacion = planificacionService.obtenerPorId(idTurno, idColaborador, LocalDate.parse(fecha)); // Cambiado
        model.addAttribute("asignacionTurno", asignacion);
        model.addAttribute("colaboradores", planificacionService.listarColaboradores()); // Cambiado
        model.addAttribute("horarios", planificacionService.listarHorarios());
        model.addAttribute("asignaciones", planificacionService.listarAsignaciones());
        return "admin/planificar_turnos";
    }

    // Guardar edición de asignación
    @PostMapping("/asignaciones/editar")
    public String guardarEdicion(@RequestParam("colaboradorId") Long idColaborador, // Cambiado
                                 @RequestParam("turnoId") Long idTurno,
                                 @RequestParam String fecha,
                                 @RequestParam(required = false) String observaciones) {
        planificacionService.editarAsignacion(idTurno, idColaborador, LocalDate.parse(fecha), observaciones); // Cambiado
        return "redirect:/admin/planificar_turnos";
    }

    // Eliminar asignación
    @GetMapping("/asignaciones/eliminar")
    public String eliminarAsignacion(@RequestParam("colaboradorId") Long idColaborador, // Cambiado
                                     @RequestParam("turnoId") Long idTurno,
                                     @RequestParam String fecha) {
        planificacionService.eliminarAsignacion(idTurno, idColaborador, LocalDate.parse(fecha)); // Cambiado
        return "redirect:/admin/planificar_turnos";
    }

    // Ver detalle de una asignación
    @GetMapping("/asignaciones/detalle")
    public String detalleAsignacion(@RequestParam("colaboradorId") Long idColaborador, // Cambiado
                                    @RequestParam("turnoId") Long idTurno,
                                    @RequestParam String fecha,
                                    Model model) {
        AsignacionTurno asignacion = planificacionService.obtenerPorId(idTurno, idColaborador, LocalDate.parse(fecha)); // Cambiado
        model.addAttribute("asignacionTurno", asignacion);
        model.addAttribute("colaboradores", planificacionService.listarColaboradores()); // Cambiado
        model.addAttribute("horarios", planificacionService.listarHorarios());
        model.addAttribute("asignaciones", planificacionService.listarAsignaciones());
        return "admin/planificar_turnos";
    }

   // Generar malla automática de turnos (ahora acepta rol opcional)
@PostMapping("/planificar_turnos/generar")
public String generarMalla(@RequestParam String mes,
                           @RequestParam(required = false) String rol,
                           RedirectAttributes ra) {
    try {
        YearMonth yearMonth = YearMonth.parse(mes); // mes puro "yyyy-MM"

        List<AsignacionTurno> generadas;
        if (rol != null && !rol.isBlank() && !"TODOS".equalsIgnoreCase(rol)) {
            generadas = planificacionService.generarMallaPorRol(yearMonth, rol);
        } else {
            generadas = planificacionService.generarMalla(yearMonth);
        }

        Map<String, PlanificacionTurnosService.MallaDTO> mallasPorRol =
                planificacionService.armarMallasPorRol(generadas, yearMonth);

        ra.addFlashAttribute("mensaje", "✅ Malla generada correctamente para " + yearMonth);
        ra.addFlashAttribute("asignacionesGeneradas", generadas);
        ra.addFlashAttribute("mallasPorRol", mallasPorRol);

    } catch (Exception e) {
        ra.addFlashAttribute("error", "⚠️ Error al generar malla: " + e.getMessage());
    }
    return "redirect:/admin/planificar_turnos";
}

@GetMapping("/dashboard_admin")
public String dashboardAdmin() {
    return "admin/dashboard_admin"; // Asegúrate de tener este archivo HTML
}
}