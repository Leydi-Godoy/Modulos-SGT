package com.sgturnos.service;

import com.sgturnos.model.MallaTurnos;
import com.sgturnos.model.Usuario;
import com.sgturnos.model.Turno;
import com.sgturnos.repository.AsignacionTurnoRepository;
import com.sgturnos.repository.MallaTurnosRepository;
import com.sgturnos.repository.UsuarioRepository;
import com.sgturnos.repository.TurnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MallaTurnosService {

    private final MallaTurnosRepository mallaTurnosRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurnoRepository turnoRepository;
    private final AsignacionTurnoRepository asignacionTurnoRepository;

public MallaTurnosService(MallaTurnosRepository mallaTurnosRepository,
                          UsuarioRepository usuarioRepository,
                          TurnoRepository turnoRepository,
                          AsignacionTurnoRepository asignacionTurnoRepository) {
    this.mallaTurnosRepository = mallaTurnosRepository;
    this.usuarioRepository = usuarioRepository;
    this.turnoRepository = turnoRepository;
    this.asignacionTurnoRepository = asignacionTurnoRepository;
} 

    // Guardar o actualizar malla
    public MallaTurnos guardar(MallaTurnos mallaTurnos) {
        // Buscar Usuario
        Usuario usuario = usuarioRepository.findById(mallaTurnos.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Buscar Turno
        Turno turno = turnoRepository.findById(mallaTurnos.getTurno().getIdTurno())
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

       // Borrar asignaciones anteriores del turno
    asignacionTurnoRepository.deleteByTurno_IdTurno(turno.getIdTurno());
       
    // Asignar objetos completos
        mallaTurnos.setUsuario(usuario);
        mallaTurnos.setTurno(turno);

        return mallaTurnosRepository.save(mallaTurnos);
    }

    public List<MallaTurnos> listar() {
        return mallaTurnosRepository.findAll();
    }

    public Optional<MallaTurnos> buscarPorId(Long id) {
        return mallaTurnosRepository.findById(id);
    }

    public void eliminar(Long id) {
        mallaTurnosRepository.deleteById(id);
    }

    public List<MallaTurnos> buscarPorUsuario(Long idUsuario) {
        return mallaTurnosRepository.findByUsuario_IdUsuario(idUsuario);
    }

    public List<MallaTurnos> buscarPorTurno(Long idTurno) {
        return mallaTurnosRepository.findByTurno_IdTurno(idTurno);
    }

    public List<MallaTurnos> buscarPorEstado(String estado) {
        return mallaTurnosRepository.findByEstado(estado);
    }
}