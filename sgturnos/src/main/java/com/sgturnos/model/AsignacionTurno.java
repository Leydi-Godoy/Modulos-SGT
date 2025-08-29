package com.sgturnos.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "asignacion_turno")
public class AsignacionTurno {

    @EmbeddedId
    private AsignacionTurnoPK id;

    @ManyToOne
    @MapsId("idColaborador")
    @JoinColumn(name = "Id_colaborador", referencedColumnName = "Id_colaborador")
    private Colaborador colaborador;

    @ManyToOne
    @MapsId("idTurno")
    @JoinColumn(name = "Id_turno", referencedColumnName = "Id_turno")
    private Turno turno;

    @Column(name = "fecha", insertable = false, updatable = false)
    private LocalDate fecha;

    @Column(name = "observaciones")
    private String observaciones;

    // Getters y setters
    public AsignacionTurnoPK getId() {
        return id;
    }

    public void setId(AsignacionTurnoPK id) {
        this.id = id;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}