

package com.sgturnos.controller.api;

import com.sgturnos.model.Turno;
import com.sgturnos.model.TipoTurno; // Ensure TipoTurno is accessible
import com.sgturnos.service.TurnoService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/turnos")
public class TurnoApiController {
    
    @Autowired
    private TurnoService turnoService;

    // Endpoint for FullCalendar to fetch turns
    @GetMapping("/by-range")
    public List<Map<String, Object>> getTurnosByRange(
            @RequestParam("start") String startDateString,
            @RequestParam("end") String endDateString) {
        // ... (Logic to parse dates and return turnos in FullCalendar format)
        // Make sure your TurnoService has getTurnosBetweenDates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);

        List<Turno> turnos = turnoService.getTurnosBetweenDates(startDate, endDate);

        List<Map<String, Object>> events = new ArrayList<>();
        for (Turno turno : turnos) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", turno.getId());
            event.put("title", turno.getUsuario().getNombre() + " - " + turno.getTipoTurno().getNombre());
            event.put("start", turno.getFecha().atTime(turno.getHoraInicio()).toString());
            event.put("end", turno.getFecha().atTime(turno.getHoraFin()).toString());
            // Puedes añadir un color o cualquier otra propiedad para FullCalendar
            // event.put("color", "#4CAF50");
            events.add(event);
        }
        return events;
    }

    // Endpoint for admin to create a new turn (example)
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')") // Apply security if using Spring Security
    public Turno createTurno(@RequestBody Turno turno) {
        return turnoService.saveTurno(turno);
    }
    
    
}
