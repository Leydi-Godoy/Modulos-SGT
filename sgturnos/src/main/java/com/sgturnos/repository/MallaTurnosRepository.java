package com.sgturnos.repository;

import com.sgturnos.model.MallaTurnos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MallaTurnosRepository extends JpaRepository<MallaTurnos, Long> {

    List<MallaTurnos> findByMesMallaAndRol(String mes, String rol);

    Optional<MallaTurnos> findTopByMesMallaAndRolOrderByFechaCreacionDesc(String mes, String rol);

    List<MallaTurnos> findByEstado(String estado);

   }