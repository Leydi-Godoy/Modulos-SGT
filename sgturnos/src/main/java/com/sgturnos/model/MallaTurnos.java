package com.sgturnos.model;

import java.time.LocalDate;

public class MallaTurnos {

    private Long idTurno;       // id_turno
    private Long idUsuario;      // id_usuario
    private LocalDate fecha;     
    private String tipoTurno;    // DIA / NOCHE / LIBRE
    private String comentario;   // observaciones adicionales

    public MallaTurnos() {}

    public MallaTurnos(Long idTurno, Long idUsuario, LocalDate fecha, String tipoTurno, String comentario) {
        this.idTurno = idTurno;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.tipoTurno = tipoTurno;
        this.comentario = comentario;
    }

    public Long getIdTurno() { return idTurno; }
    public void setIdTurno(Long idTurno) { this.idTurno = idTurno; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getTipoTurno() { return tipoTurno; }
    public void setTipoTurno(String tipoTurno) { this.tipoTurno = tipoTurno; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}