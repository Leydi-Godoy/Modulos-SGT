package com.sgturnos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "Id_usuario")
    private Long idUsuario;

    @Column(name = "primer_nombre")
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @ManyToOne
    @JoinColumn(name = "Id_rol", referencedColumnName = "Id_rol")
    private Rol rol;

    @Column(name = "correo")
    private String correo;

    @Column(name = "contrasena")
    private String contrasena;
    
   // Relación uno a uno con Colaborador
@OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
private Colaborador colaborador;

    // Getters y setters

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    // Setter con sincronización
public void setColaborador(Colaborador colaborador) {    
    this.colaborador = colaborador;
    if (colaborador != null) {
        colaborador.setUsuario(this); // asegura la relación
    }
}

    // Método de conveniencia para obtener nombre completo
    public String getNombreCompleto() {
        return primerNombre + " " + (segundoNombre != null ? segundoNombre + " " : "") +
                primerApellido + " " + (segundoApellido != null ? segundoApellido : "");
    }
    
    // Método para verificar si tiene un rol específico
    public boolean tieneRol(String nombreRol) {
        return rol != null && rol.getRol() != null && rol.getRol().equalsIgnoreCase(nombreRol);
    }
}