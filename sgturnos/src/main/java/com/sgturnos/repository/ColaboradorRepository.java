package com.sgturnos.repository;

import com.sgturnos.model.Colaborador;
import com.sgturnos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    // Devuelve el último colaborador según el ID
    Colaborador findTopByOrderByIdColaboradorDesc();

    // Verifica si existe colaborador asociado a un usuario
    Colaborador findByUsuario(Usuario usuario);
}