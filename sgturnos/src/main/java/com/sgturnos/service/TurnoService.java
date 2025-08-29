package com.sgturnos.service;

import com.sgturnos.model.Turno;
import java.util.List;

public interface TurnoService {
    List<Turno> listarTodos();
    Turno guardarTurno(Turno turno);
    Turno obtenerPorId(Long id);
    void eliminar(Long id);
}