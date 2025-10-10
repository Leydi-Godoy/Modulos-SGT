package com.sgturnos.service;

import com.sgturnos.model.MallaTurnos;
import com.sgturnos.repository.MallaTurnosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MallaTurnosService {

    private final MallaTurnosRepository mallaTurnosRepository;

    public MallaTurnosService(MallaTurnosRepository mallaTurnosRepository) {
        this.mallaTurnosRepository = mallaTurnosRepository;
    }

    @Transactional
    public MallaTurnos guardarMalla(MallaTurnos mallaTurnos) {
        if (mallaTurnos == null) {
            throw new IllegalArgumentException("MallaTurnos no puede ser null");
        }
        if (mallaTurnos.getFechaCreacion() == null) {
            mallaTurnos.setFechaCreacion(LocalDateTime.now());
        }
        return mallaTurnosRepository.save(mallaTurnos);
    }

    @Transactional
    public MallaTurnos guardar(MallaTurnos malla) {
        return guardarMalla(malla);
    }

    @Transactional(readOnly = true)
    public List<MallaTurnos> listar() {
        return mallaTurnosRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MallaTurnos> buscarPorId(Long id) {
        return mallaTurnosRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<MallaTurnos> buscarPorMesYRol(String mes, String rol) {
        if (mes == null || rol == null) return Collections.emptyList();
        return mallaTurnosRepository.findByMesMallaAndRol(mes, rol);
    }

    @Transactional(readOnly = true)
    public MallaTurnos obtenerUltimaVersion(String mes, String rol) {
        return mallaTurnosRepository.findTopByMesMallaAndRolOrderByFechaCreacionDesc(mes, rol)
                .orElse(null);
    }

    @Transactional
    public void eliminar(Long id) {
        if (id == null) return;
        mallaTurnosRepository.deleteById(id);
    }
}