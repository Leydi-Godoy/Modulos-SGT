package com.sgturnos.repository;

import com.sgturnos.model.Turno;
import com.sgturnos.model.Colaborador; // Cambiado de Empleado a Colaborador
import com.sgturnos.model.Horario;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // Lista todos los turnos asignados a un colaborador específico
    @Query("SELECT t FROM Turno t JOIN t.asignaciones a WHERE a.colaborador = :colaborador") // Cambiado
    List<Turno> findByColaborador(@Param("colaborador") Colaborador colaborador); // Cambiado
    
    Optional<Turno> findByFechaIniAndFechaFinAndHorario(LocalDate fechaIni, LocalDate fechaFin, Horario horario);
}