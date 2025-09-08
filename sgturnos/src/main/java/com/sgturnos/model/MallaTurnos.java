package com.sgturnos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "malla_turnos")
public class MallaTurnos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_malla")
    private Long idMalla;

    // Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "Id_usuario", nullable = false)
    private Usuario usuario;

    // Relación con Turno
    @ManyToOne
    @JoinColumn(name = "Id_turno", nullable = false)
    private Turno turno;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado = "GENERADA";  // valor por defecto

    // Nuevo: mes de la malla en formato "YYYY-MM"
    @Column(name = "mes_malla", nullable = false, length = 7)
    private String mesMalla;

    // Nuevo: rol asociado a la malla
    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    public MallaTurnos() {}

    public MallaTurnos(Usuario usuario, Turno turno, String estado, String mesMalla, String rol) {
        this.usuario = usuario;
        this.turno = turno;
        this.estado = estado;
        this.mesMalla = mesMalla;
        this.rol = rol;
    }

    // Getters y setters
    public Long getIdMalla() {
        return idMalla;
    }

    public void setIdMalla(Long idMalla) {
        this.idMalla = idMalla;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
}