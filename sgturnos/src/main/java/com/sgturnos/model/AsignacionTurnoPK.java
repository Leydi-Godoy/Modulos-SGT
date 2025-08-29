package com.sgturnos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class AsignacionTurnoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "Id_turno")
    private Long idTurno;

    @Column(name = "Id_colaborador")
    private Long idColaborador;  // Cambiado de String idEmpleado a Long idColaborador

    @Column(name = "fecha")
    private LocalDate fecha;

    public AsignacionTurnoPK() {}

    public AsignacionTurnoPK(Long idTurno, Long idColaborador, LocalDate fecha) {
        this.idTurno = idTurno;
        this.idColaborador = idColaborador;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    public Long getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Long idColaborador) {
        this.idColaborador = idColaborador;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AsignacionTurnoPK)) return false;
        AsignacionTurnoPK that = (AsignacionTurnoPK) o;
        return Objects.equals(idTurno, that.idTurno) &&
               Objects.equals(idColaborador, that.idColaborador) &&
               Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTurno, idColaborador, fecha);
    }
}