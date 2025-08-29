package com.sgturnos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "colaborador")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_colaborador")
    private Long idColaborador;   // Cambiado de String idEmpleado a Long idColaborador (autoincremental)

    @ManyToOne
    @JoinColumn(name = "Id_rol", referencedColumnName = "Id_rol")
    private Rol rol;

    @OneToOne
@JoinColumn(name = "Id_usuario", referencedColumnName = "Id_usuario", unique = true)
private Usuario usuario;

    // Getters y setters
    public Long getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Long idColaborador) {
        this.idColaborador = idColaborador;
    }
   
    public Rol getRol() { 
        return rol; 
    }
    
    public void setRol(Rol rol) { 
        this.rol = rol; 
    }

    public Usuario getUsuario() { 
        return usuario; 
    }
    
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }
}