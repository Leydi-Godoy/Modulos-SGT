package com.sgturnos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "asignacion_turno")
public class AsignacionTurno {

    @EmbeddedId
    private AsignacionTurnoPK id;

    @ManyToOne
    @MapsId("idColaborador") // conecta con la PK compuesta
    @JoinColumn(name = "Id_colaborador", referencedColumnName = "Id_colaborador", nullable = false)
    private Colaborador colaborador;

    @ManyToOne
    @MapsId("idTurno") // conecta con la PK compuesta
    @JoinColumn(name = "Id_turno", referencedColumnName = "Id_turno", nullable = false)
    private Turno turno;

    @Column(name = "fecha", insertable = false, updatable = false)
    private LocalDate fecha;

    @Column(name = "observaciones")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "Id_malla", referencedColumnName = "Id_malla", nullable = false)
    private MallaTurnos mallaTurnos;

    // ================== Getters & Setters ==================
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
        if (fecha == null && id != null) {
            return id.getFecha();
        }
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
        if (id != null) {
            id.setFecha(fecha);
        }
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public MallaTurnos getMallaTurnos() {
        return mallaTurnos;
    }

    public void setMallaTurnos(MallaTurnos mallaTurnos) {
        this.mallaTurnos = mallaTurnos;
    }
}