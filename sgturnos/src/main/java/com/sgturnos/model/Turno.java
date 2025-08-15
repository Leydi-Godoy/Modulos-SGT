package com.sgturnos.model;

import com.sgturnos.sgturnos.model.Horario;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long idTurno;

    @Column(name = "Fecha_ini")
    private LocalDate fechaIni;

    @Column(name = "Fecha_fin")
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "Id_horario")
    private Horario horario;

    @ManyToOne
    @JoinColumn(name = "Id_usuario")
    private Usuario usuario;

    // Getters y Setters
    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    public LocalDate getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(LocalDate fechaIni) {
        this.fechaIni = fechaIni;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}