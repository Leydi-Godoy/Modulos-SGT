

package com.sgturnos.controller.api;

import com.sgturnos.model.solicitud.Solicitud;
import com.sgturnos.model.solicitud.EstadoSolicitud;
import com.sgturnos.model.Usuario; // Assuming this is your User entity for security context
import com.sgturnos.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // For getting current logged-in user
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudApiController {
    
    @Autowired
    private SolicitudService solicitudService;

    // Endpoint for a user to create a new request
    @PostMapping
    // @PreAuthorize("hasAnyRole('USER', 'MEDICO', 'ENFERMERA')") // Example security
    public ResponseEntity<Solicitud> createSolicitud(@RequestBody Solicitud solicitud,
                                                     @AuthenticationPrincipal Usuario currentUser) { // Get current logged-in user
        solicitud.setUsuarioSolicitante(currentUser); // Assign the current user to the request
        Solicitud newSolicitud = solicitudService.crearSolicitud(solicitud);
        return ResponseEntity.ok(newSolicitud);
    }

    // Endpoint for a user to view their own requests
    @GetMapping("/my-requests")
    // @PreAuthorize("hasAnyRole('USER', 'MEDICO', 'ENFERMERA')")
    public List<Solicitud> getMyRequests(@AuthenticationPrincipal Usuario currentUser) {
        return solicitudService.getMisSolicitudes(currentUser);
    }

    // Endpoint for admin to view pending requests
    @GetMapping("/pending")
    // @PreAuthorize("hasRole('ADMIN')")
    public List<Solicitud> getPendingRequests() {
        return solicitudService.getSolicitudesPendientes();
    }

    // Endpoint for admin to approve/reject a request
    @PutMapping("/{id}/status")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Solicitud> updateSolicitudStatus(@PathVariable Long id,
                                                           @RequestParam EstadoSolicitud status,
                                                           @AuthenticationPrincipal Usuario currentUser) {
        Solicitud updatedSolicitud = solicitudService.actualizarEstadoSolicitud(id, status, currentUser);
        return ResponseEntity.ok(updatedSolicitud);
    }
    
    
}
