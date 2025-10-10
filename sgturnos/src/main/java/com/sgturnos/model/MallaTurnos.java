package com.sgturnos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "malla_turnos")
public class MallaTurnos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_malla")
    private Long idMalla;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoMalla estado;

    @Column(name = "mes_malla", nullable = false, length = 25)
    private String mesMalla;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    @Lob
    @Column(name = "contenido")
    private byte[] contenido;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // ================== Constructores ==================
    public MallaTurnos() {}

    public MallaTurnos(EstadoMalla estado, String mesMalla, String rol, byte[] contenido) {
        this.estado = estado;
        this.mesMalla = mesMalla;
        this.rol = rol;
        this.contenido = contenido;
        this.fechaCreacion = LocalDateTime.now(); // puedes quitarlo si quieres permitir null
    }

    // ================== Getters & Setters ==================
    public Long getIdMalla() {
        return idMalla;
    }

    public void setIdMalla(Long idMalla) {
        this.idMalla = idMalla;
    }

    public EstadoMalla getEstado() {
        return estado;
    }

    public void setEstado(EstadoMalla estado) {
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

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // ================== Enum de Estados ==================
    public enum EstadoMalla {
        EDITADA,
        ENVIADA,
        GENERADA
    }
}