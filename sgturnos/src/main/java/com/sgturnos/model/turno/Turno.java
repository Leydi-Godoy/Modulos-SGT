
 */
package com.sgturnos.model.turno;
 
import jakarta.persistence.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "turnos") // Map to a 'turnos' table in your DB
 

/**
 *
 * @author Edkiel
 */
public class Turno {
    
    
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    private TipoTurno tipoTurno; // Ej: MAÑANA, TARDE, NOCHE

    @ManyToOne // Many turns can belong to one user
    @JoinColumn(name = "usuario_id") // Foreign key column
    private Usuario usuario; // Assuming you have a Usuario entity

    // Getters and Setters
    // Constructor
    // ...
}
    
    
}
