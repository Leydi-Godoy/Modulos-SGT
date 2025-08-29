package com.sgturnos.repository;

import com.sgturnos.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    Optional<Horario> findByTipo(String tipo); // usa Optional para manejar faltantes
}