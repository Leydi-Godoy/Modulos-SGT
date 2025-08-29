package com.sgturnos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_turno")
    private Long idTurno;

    @Column(name = "Fecha_ini")
private LocalDate fechaIni;

@Column(name = "Fecha_fin")
private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "id_horario", referencedColumnName = "Id_horario")
    private Horario horario;
    
    @OneToMany(mappedBy = "turno")
    private List<AsignacionTurno> asignaciones;

    // Getters y setters
    public Long getIdTurno() { return idTurno; }
    public void setIdTurno(Long idTurno) { this.idTurno = idTurno; }

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

    public Horario getHorario() { return horario; }
    public void setHorario(Horario horario) { this.horario = horario; }

    public List<AsignacionTurno> getAsignaciones() { return asignaciones; }
    public void setAsignaciones(List<AsignacionTurno> asignaciones) { this.asignaciones = asignaciones; }
}