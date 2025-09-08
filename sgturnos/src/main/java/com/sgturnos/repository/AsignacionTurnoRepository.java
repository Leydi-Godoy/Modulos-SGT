package com.sgturnos.repository;

import com.sgturnos.model.AsignacionTurno;
import com.sgturnos.model.AsignacionTurnoPK;
import com.sgturnos.model.Colaborador; // Cambiado de Empleado a Colaborador
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionTurnoRepository extends JpaRepository<AsignacionTurno, AsignacionTurnoPK> {
    
    // El método que borra todas las asignaciones de un turno específico
    void deleteByTurno_IdTurno(Long idTurno);
    
    // Otros métodos existentes
    List<AsignacionTurno> findByColaborador(Colaborador colaborador);
}