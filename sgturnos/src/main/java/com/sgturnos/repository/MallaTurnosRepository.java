package com.sgturnos.repository;

import com.sgturnos.model.MallaTurnos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MallaTurnosRepository extends JpaRepository<MallaTurnos, Long> {

    // Buscar mallas por usuario
    List<MallaTurnos> findByUsuario_IdUsuario(Long idUsuario);

    // Buscar mallas por turno
    List<MallaTurnos> findByTurno_IdTurno(Long idTurno);

    // Buscar por estado
    List<MallaTurnos> findByEstado(String estado);
}