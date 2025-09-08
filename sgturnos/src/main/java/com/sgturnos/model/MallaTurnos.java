package com.sgturnos.model;

import jakarta.persistence.*;

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

    public MallaTurnos() {}

    public MallaTurnos(Usuario usuario, Turno turno, String estado) {
        this.usuario = usuario;
        this.turno = turno;
        this.estado = estado;
    }

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
}