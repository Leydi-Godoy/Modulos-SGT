package com.sgturnos.service;

import com.sgturnos.model.AsignacionTurno;
import com.sgturnos.model.MallaTurnos;
import com.sgturnos.model.Usuario;
import com.sgturnos.repository.AsignacionTurnoRepository;
import com.sgturnos.repository.HorarioRepository;
import com.sgturnos.sgturnos.model.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlanificacionTurnosService {

    @Autowired
    private UsuarioService usuarioService; // usamos tu servicio de login

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private AsignacionTurnoRepository asignacionRepo;

    // ------------------------
    // CRUD Asignaciones
    // ------------------------
    public List<AsignacionTurno> listarAsignaciones() {
        return asignacionRepo.findAll();
    }

    public AsignacionTurno obtenerPorId(Long id) {
        return asignacionRepo.findById(id).orElse(null);
    }

    public void guardarAsignacion(Long usuarioId, String fechaStr, String horaIniStr, String horaFinStr, String area) {
        Usuario usuario = usuarioService.findById(usuarioId);
        LocalDate fecha = LocalDate.parse(fechaStr);
        AsignacionTurno asignacion = new AsignacionTurno(usuario, fecha, java.time.LocalTime.parse(horaIniStr),
                java.time.LocalTime.parse(horaFinStr), area, "");
        asignacionRepo.save(asignacion);
    }

    public void editarAsignacion(Long id, Long usuarioId, String fechaStr, String horaIniStr, String horaFinStr, String area) {
        AsignacionTurno asignacion = obtenerPorId(id);
        if (asignacion != null) {
            Usuario usuario = usuarioService.findById(usuarioId);
            asignacion.setUsuario(usuario);
            asignacion.setFecha(LocalDate.parse(fechaStr));
            asignacion.setHoraInicio(java.time.LocalTime.parse(horaIniStr));
            asignacion.setHoraFin(java.time.LocalTime.parse(horaFinStr));
            asignacion.setArea(area);
            asignacionRepo.save(asignacion);
        }
    }

    public void eliminarAsignacion(Long id) {
        asignacionRepo.deleteById(id);
    }

    // ------------------------
    // Listado de usuarios
    // ------------------------
    public List<Usuario> listarUsuarios() {
        return usuarioService.findAll();
    }

    // ------------------------
    // Generación de malla de turnos
    // ------------------------
    public List<MallaTurnos> generarMalla(YearMonth mes) {
        List<MallaTurnos> malla = new ArrayList<>();
        List<Usuario> usuarios = usuarioService.findAll();

        var dia = horarioRepository.findByTipo("DIA");
        var noche = horarioRepository.findByTipo("NOCHE");
        var libre = horarioRepository.findByTipo("LIBRE");

        for (Usuario u : usuarios) {
            LocalDate fecha = mes.atDay(1);
            boolean ultimaNoche = false;

            for (int i = 1; i <= mes.lengthOfMonth(); i++) {
    Horario horarioAsignado; // declarar con el tipo concreto

    if (ultimaNoche) {
        horarioAsignado = libre;
        ultimaNoche = false;
    } else {
        horarioAsignado = (i % 2 == 1) ? dia : noche;
        if (horarioAsignado.getTipo().equals("NOCHE")) {
            ultimaNoche = true;
        }
    }

                MallaTurnos registro = new MallaTurnos(
                        null,
                        u.getIdUsuario(),
                        fecha,
                        horarioAsignado.getTipo(),
                        ""
                );

                malla.add(registro);
                fecha = fecha.plusDays(1);
            }
        }

        return malla;
    }
}