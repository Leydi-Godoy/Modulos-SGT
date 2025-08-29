package com.sgturnos.service;

import com.sgturnos.model.Turno;
import com.sgturnos.repository.TurnoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;

    public TurnoServiceImpl(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @Override
    public List<Turno> listarTodos() {
        return turnoRepository.findAll();
    }

    @Override
    public Turno guardarTurno(Turno turno) {
        return turnoRepository.save(turno);
    }

    @Override
    public Turno obtenerPorId(Long id) {
        Optional<Turno> turno = turnoRepository.findById(id);
        return turno.orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        turnoRepository.deleteById(id);
    }
}