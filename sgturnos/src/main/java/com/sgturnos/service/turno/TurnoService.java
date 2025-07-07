
package com.sgturnos.service.turno;

import com.sgturnos.model.Turno;
import com.sgturnos.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TurnoService {
    
    @Autowired
    private TurnoRepository turnoRepository;

    // Methods to create, update, delete turns (for admin)
    public Turno saveTurno(Turno turno) {
        return turnoRepository.save(turno);
    }

    // Methods to fetch turns
    public List<Turno> getTurnosBetweenDates(LocalDate start, LocalDate end) {
        return turnoRepository.findByFechaBetween(start, end);
    }

    public List<Turno> getTurnosByUserId(Long userId) {
        return turnoRepository.findByUsuarioId(userId);
    }
    // ... more methods
    
    
}
