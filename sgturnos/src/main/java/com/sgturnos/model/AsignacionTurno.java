package com.sgturnos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "asignacion_turno")
public class AsignacionTurno {

    @EmbeddedId
    private AsignacionTurnoPK id;

    @ManyToOne
    @MapsId("Id_colaborador")
    @JoinColumn(name = "Id_colaborador", referencedColumnName = "Id_colaborador")
    private Colaborador colaborador;

    @ManyToOne
    @MapsId("Id_turno")
    @JoinColumn(name = "Id_turno", referencedColumnName = "Id_turno")
    private Turno turno;

    // <-- Aquí dejamos que el campo fecha sea solo lectura (mapeado por el PK)
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
        // Si quieres siempre devolver la fecha del id cuando esté presente:
        if (fecha == null && id != null) {
            return id.getFecha();
        }
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        // No se usará para insertar (la persistencia la maneja el EmbeddedId),
        // pero mantenemos el setter para compatibilidad con el código.
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
}