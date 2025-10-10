package com.sgturnos.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "horario")
public class Horario {

    @Id
    @Column(name = "Id_horario")
    private String idHorario;

    @Column(name = "hora_inicio")
    private String horaInicio;

    @Column(name = "hora_fin")
    private String horaFin;

    @Enumerated(EnumType.STRING) // Guarda el nombre del enum en la BD
    @Column(name = "tipo", nullable = false)
    private TipoHorario tipo;

    @OneToMany(mappedBy = "horario")
    private List<Turno> turnos;

    // Enum interno para los tipos de horario
    public enum TipoHorario {
        COMITE,
        DIA,
        NOCHE,
        LIBRE
    }

    // Getters y Setters
    public String getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(String idHorario) {
        this.idHorario = idHorario;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public TipoHorario getTipo() {
        return tipo;
    }

    public void setTipo(TipoHorario tipo) {
        this.tipo = tipo;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }
}