

package com.sgturnos.repository.turno;

import com.sgturnos.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public class TurnoRepository {
    
    List<Turno> findByFechaBetween(LocalDate startDate, LocalDate endDate);
    List<Turno> findByUsuarioId(Long userId);
    // Add more custom query methods as needed 
    
    
    
}
