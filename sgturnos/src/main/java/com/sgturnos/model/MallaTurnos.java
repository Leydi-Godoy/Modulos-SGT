package com.sgturnos.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "malla_turnos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"mes_malla", "rol"}) // Evitar duplicados
)
public class MallaTurnos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_malla")
    private Long idMalla;

    @ManyToOne
    @JoinColumn(name = "Id_colaborador", referencedColumnName = "Id_colaborador")
    private Colaborador colaborador;

    @ManyToOne
    @JoinColumn(name = "Id_turno", referencedColumnName = "Id_turno")
    private Turno turno;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado = "GENERADA";  // valor por defecto

    @Column(name = "mes_malla", nullable = false, length = 7)
    private String mesMalla; // formato YYYY-MM

    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    @OneToMany(mappedBy = "malla", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsignacionTurno> asignaciones = new ArrayList<>();

    public MallaTurnos() {}

    public MallaTurnos(String estado, String mesMalla, String rol) {
        this.estado = estado;
        this.mesMalla = mesMalla;
        this.rol = rol;
    }

    // --- Getters y Setters ---
    public Long getIdMalla() {
        return idMalla;
    }

    public void setIdMalla(Long idMalla) {
        this.idMalla = idMalla;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMesMalla() {
        return mesMalla;
    }

    public void setMesMalla(String mesMalla) {
        this.mesMalla = mesMalla;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<AsignacionTurno> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<AsignacionTurno> asignaciones) {
        this.asignaciones = asignaciones;
    }
}