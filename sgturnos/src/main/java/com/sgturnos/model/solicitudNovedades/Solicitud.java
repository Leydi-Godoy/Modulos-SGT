
package com.sgturnos.model.solicitudNovedades;


import com.sgturnos.model.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "solicitudes")

public class Solicitud {
    
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_solicitante_id")
    private Usuario usuarioSolicitante; // The user making the request

    @Enumerated(EnumType.STRING)
    private TipoSolicitud tipoSolicitud; // Ej: PERMISO, CAMBIO_TURNO, INCAPACIDAD

    private String descripcion;
    private LocalDate fechaTurnoAfectado; // The date of the turn affected by the request

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado; // Ej: PENDIENTE, APROBADA, RECHAZADA

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaResolucion;

    @ManyToOne
    @JoinColumn(name = "usuario_revisor_id")
    private Usuario usuarioRevisor; // Admin who reviewed it

    // Getters and Setters
    // Constructor
    // ...
}    
    
    
}
