package com.sgturnos.repository;

import com.sgturnos.model.AsignacionTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionTurnoRepository extends JpaRepository<AsignacionTurno, Long> {
    // JpaRepository ya tiene métodos como findAll(), save(), deleteById()
}