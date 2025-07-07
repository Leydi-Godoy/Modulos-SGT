
package com.sgturnos.service.solicitud;
import com.sgturnos.model.solicitud.Solicitud;
import com.sgturnos.model.solicitud.EstadoSolicitud;
import com.sgturnos.model.Usuario;
import com.sgturnos.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudService {
    
    @Autowired
    private SolicitudRepository solicitudRepository;

    // Method for users to create a new request
    public Solicitud crearSolicitud(Solicitud solicitud) {
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        return solicitudRepository.save(solicitud);
    }

    // Method for admins to approve/reject requests
    public Solicitud actualizarEstadoSolicitud(Long solicitudId, EstadoSolicitud nuevoEstado, Usuario revisor) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                                                  .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado(nuevoEstado);
        solicitud.setFechaResolucion(LocalDateTime.now());
        solicitud.setUsuarioRevisor(revisor); // Set the admin who reviewed it
        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> getMisSolicitudes(Usuario usuario) {
        return solicitudRepository.findByUsuarioSolicitante(usuario);
    }

    public List<Solicitud> getSolicitudesPendientes() {
        return solicitudRepository.findByEstado(EstadoSolicitud.PENDIENTE);
    }
    // ... more methods
    
}
