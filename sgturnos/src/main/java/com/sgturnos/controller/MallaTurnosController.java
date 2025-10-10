package com.sgturnos.controller;

import com.sgturnos.model.MallaTurnos;
import com.sgturnos.repository.TurnoRepository;
import com.sgturnos.repository.UsuarioRepository;
import com.sgturnos.service.MallaTurnosService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/malla_turnos")
public class MallaTurnosController {

    private final MallaTurnosService mallaTurnosService;
    private final UsuarioRepository usuarioRepository;
    private final TurnoRepository turnoRepository;

    public MallaTurnosController(MallaTurnosService mallaTurnosService,
                                 UsuarioRepository usuarioRepository,
                                 TurnoRepository turnoRepository) {
        this.mallaTurnosService = mallaTurnosService;
        this.usuarioRepository = usuarioRepository;
        this.turnoRepository = turnoRepository;
    }

    // Listar mallas
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mallas", mallaTurnosService.listar());
        return "admin/listar_mallas";
    }

    // Formulario nueva malla
    @GetMapping("/nueva")
    public String nuevaMalla(Model model) {
        model.addAttribute("malla", new MallaTurnos());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("turnos", turnoRepository.findAll());
        return "admin/editar_malla";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        MallaTurnos malla = mallaTurnosService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Id inválido: " + id));
        model.addAttribute("malla", malla);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("turnos", turnoRepository.findAll());
        return "admin/editar_malla";
    }

    // Guardar malla (edición o nueva)
    @PostMapping("/guardar")
    public String guardarEdicion(@ModelAttribute("malla") MallaTurnos malla,
                                 @RequestParam(value = "archivo", required = false) MultipartFile archivo) throws IOException {
        if (archivo != null && !archivo.isEmpty()) {
            malla.setContenido(archivo.getBytes());
        }
        mallaTurnosService.guardarMalla(malla);
        return "redirect:/malla_turnos";
    }

    // Eliminar malla
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id) {
        mallaTurnosService.eliminar(id);
        return "redirect:/malla_turnos";
    }

    // Descargar malla desde DB
    @GetMapping("/descargar")
    public ResponseEntity<byte[]> descargarMalla(@RequestParam String mes,
                                                 @RequestParam String rol,
                                                 @RequestParam String tipo) {
        MallaTurnos malla = mallaTurnosService.obtenerUltimaVersion(mes, rol);
        if (malla == null || malla.getContenido() == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = tipo.equalsIgnoreCase("pdf") ?
                MediaType.APPLICATION_PDF :
                MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        String extension = tipo.equalsIgnoreCase("pdf") ? ".pdf" : ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=malla_" + mes + "_" + rol + extension)
                .contentType(mediaType)
                .body(malla.getContenido());
    }
    
    // 🔹 ENDPOINTS SOLO PARA PRUEBAS EN POSTMAN (JSON)
@GetMapping("/api")
@ResponseBody
public List<MallaTurnos> listarMallasJson() {
    return mallaTurnosService.listar();
}

@GetMapping("/api/{id}")
@ResponseBody
public ResponseEntity<?> obtenerMallaJson(@PathVariable("id") Long id) {
    return mallaTurnosService.buscarPorId(id)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(404)
                    .body("{ \"mensaje\": \"Malla no encontrada\" }"));
}

@PostMapping("/api/guardar")
@ResponseBody
public ResponseEntity<?> guardarMallaJson(@ModelAttribute MallaTurnos malla,
                                          @RequestParam(value = "archivo", required = false) MultipartFile archivo) throws IOException {
    if (archivo != null && !archivo.isEmpty()) {
        malla.setContenido(archivo.getBytes());
    }
    mallaTurnosService.guardarMalla(malla);
    return ResponseEntity.ok("{ \"mensaje\": \"Malla guardada exitosamente\" }");
}

@DeleteMapping("/api/eliminar/{id}")
@ResponseBody
public ResponseEntity<?> eliminarMallaJson(@PathVariable("id") Long id) {
    mallaTurnosService.eliminar(id);
    return ResponseEntity.ok("{ \"mensaje\": \"Malla eliminada exitosamente\" }");
}

}