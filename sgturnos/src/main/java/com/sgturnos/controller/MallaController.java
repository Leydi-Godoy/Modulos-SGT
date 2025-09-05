package com.sgturnos.controller;

import java.io.IOException;
import java.nio.file.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class MallaController {

    @Value("${mallas.path}")
    private String mallasPath;

   private Path obtenerArchivo(String nombreArchivo) throws IOException {
    Path carpeta = Paths.get(mallasPath).toAbsolutePath().normalize();
    Path archivo = carpeta.resolve(nombreArchivo);
    System.out.println("Buscando archivo en: " + archivo); // <--- esto verifica la ruta
    if (!Files.exists(carpeta)) {
        Files.createDirectories(carpeta);
    }
    return archivo;
}

   @GetMapping("/descargar_malla_pdf")
public ResponseEntity<Resource> descargarMallaPdf(@RequestParam("mes") String mes,
                                                  @RequestParam("rol") String rol) throws IOException {
    String sufijo = (rol == null || rol.isBlank() ? "TODOS" : rol.toUpperCase());
    Path archivo = obtenerArchivo("malla_" + mes + "_" + sufijo + ".pdf");

    if (!Files.exists(archivo)) {
        return ResponseEntity.notFound().build();
    }

    Resource resource = new UrlResource(archivo.toUri());

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFileName() + "\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
}

@GetMapping("/descargar_malla_excel")
public ResponseEntity<Resource> descargarMallaExcel(@RequestParam("mes") String mes,
                                                    @RequestParam("rol") String rol) throws IOException {
    String sufijo = (rol == null || rol.isBlank() ? "TODOS" : rol.toUpperCase());
    Path archivo = obtenerArchivo("malla_" + mes + "_" + sufijo + ".xlsx");

    if (!Files.exists(archivo)) {
        return ResponseEntity.notFound().build();
    }

    Resource resource = new UrlResource(archivo.toUri());

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFileName() + "\"")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource);
}
}