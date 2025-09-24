package com.sgturnos.service;

import com.sgturnos.model.*;
import com.sgturnos.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MallaTurnosService {

    private final MallaTurnosRepository mallaTurnosRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final TurnoRepository turnoRepository;
    private final AsignacionTurnoRepository asignacionTurnoRepository;

    public MallaTurnosService(MallaTurnosRepository mallaTurnosRepository,
                              ColaboradorRepository colaboradorRepository,
                              TurnoRepository turnoRepository,
                              AsignacionTurnoRepository asignacionTurnoRepository) {
        this.mallaTurnosRepository = mallaTurnosRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.turnoRepository = turnoRepository;
        this.asignacionTurnoRepository = asignacionTurnoRepository;
    }

    @Transactional
    public MallaTurnos generarYMantenerMalla(String mes, String rol) {
        return mallaTurnosRepository.findByMesMallaAndRol(mes, rol)
                .orElseGet(() -> {
                    MallaTurnos nueva = new MallaTurnos("GENERADA", mes, rol);
                    return mallaTurnosRepository.save(nueva);
                });
    }

    @Transactional
    public MallaTurnos guardar(MallaTurnos malla) {
        if (malla.getColaborador() != null && malla.getColaborador().getIdColaborador() != null) {
            malla.setColaborador(
                colaboradorRepository.findById(malla.getColaborador().getIdColaborador())
                        .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"))
            );
        }
        if (malla.getTurno() != null && malla.getTurno().getIdTurno() != null) {
            malla.setTurno(
                turnoRepository.findById(malla.getTurno().getIdTurno())
                        .orElseThrow(() -> new RuntimeException("Turno no encontrado"))
            );
        }
        return mallaTurnosRepository.save(malla);
    }

    @Transactional
    public void asignarTurno(MallaTurnos malla, Long idColaborador, Long idTurno, LocalDate fecha) {
        Colaborador colaborador = colaboradorRepository.findById(idColaborador)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        AsignacionTurnoPK pk = new AsignacionTurnoPK(idTurno, idColaborador, fecha);

        AsignacionTurno asignacion = new AsignacionTurno();
        asignacion.setId(pk);
        asignacion.setColaborador(colaborador);
        asignacion.setTurno(turno);
        asignacion.setMalla(malla);
        asignacion.setFecha(fecha);

        asignacionTurnoRepository.save(asignacion);
    }

    public List<MallaTurnos> listar() {
        return mallaTurnosRepository.findAll();
    }

    public Optional<MallaTurnos> buscarPorId(Long id) {
        return mallaTurnosRepository.findById(id);
    }

    @Transactional
    public void eliminar(Long id) {
        mallaTurnosRepository.deleteById(id);
    }

    public List<MallaTurnos> buscarPorEstado(String estado) {
        return mallaTurnosRepository.findByEstado(estado);
    }
}