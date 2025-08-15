package com.sgturnos.repository;

import com.sgturnos.sgturnos.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, String> {

    // Método para buscar un horario por tipo: DIA, NOCHE, LIBRE, COMITE
    Horario findByTipo(String tipo);

}