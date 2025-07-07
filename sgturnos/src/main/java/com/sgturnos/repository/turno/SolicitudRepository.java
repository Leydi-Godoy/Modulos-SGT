

package com.sgturnos.repository.turno;

import com.sgturnos.model.solicitud.Solicitud;
import com.sgturnos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public class SolicitudRepository {
    
    List<Solicitud> findByUsuarioSolicitante(Usuario usuario);
    List<Solicitud> findByEstado(EstadoSolicitud estado);
    // Add more custom query methods
    
}
