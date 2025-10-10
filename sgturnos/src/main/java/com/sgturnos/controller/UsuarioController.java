package com.sgturnos.controller;

import com.sgturnos.model.Colaborador;
import com.sgturnos.model.Usuario;
import com.sgturnos.repository.RolRepository;
import com.sgturnos.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/nuevo")
    public String showForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/form";
    }

    @GetMapping
    public String listUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "admin/lista";
    }
    
    @PostMapping
public String saveUsuario(@ModelAttribute Usuario usuario,
                         @RequestParam(value = "terminos", required = false) String terminos,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request) { // ← Agrega esto
    
    System.out.println("🔥🔥🔥 CONTROLLER saveUsuario LLAMADO 🔥🔥🔥");
    
    // 🔥 DIAGNÓSTICO CRÍTICO - Agrega estas líneas:
    String idFromRequest = request.getParameter("idUsuario");
    System.out.println("🔵 ID desde request (String): " + idFromRequest);
    System.out.println("🔵 Tipo del ID desde request: " + (idFromRequest != null ? idFromRequest.getClass().getName() : "null"));
    System.out.println("🔵 ID en usuario objeto: " + usuario.getIdUsuario());
    System.out.println("🔵 Tipo del ID en usuario: " + (usuario.getIdUsuario() != null ? usuario.getIdUsuario().getClass().getName() : "null"));
    
    // 🔥 CONVERSIÓN SEGURA - Agrega esto:
    if (usuario.getIdUsuario() == null && idFromRequest != null) {
        try {
            usuario.setIdUsuario(Long.parseLong(idFromRequest));
            System.out.println("✅ ID convertido a Long: " + usuario.getIdUsuario());
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "El ID debe ser numérico");
            return "redirect:/usuarios/nuevo";
        }
    }

    // 🔹 VALIDACIÓN CRÍTICA: Asegurar que el ID no sea null
    if (usuario.getIdUsuario() == null) {
        redirectAttributes.addFlashAttribute("error", "El número de documento es obligatorio");
        return "redirect:/usuarios/nuevo";
    }

    // Buscar si ya existe en BD por idUsuario
    Usuario usuarioExistente = usuarioService.findById(usuario.getIdUsuario());

    if (usuarioExistente == null) {
        // Usuario nuevo → validar términos
        if (terminos == null || !terminos.equals("aceptado")) {
            redirectAttributes.addFlashAttribute("error", "Debe aceptar los términos y condiciones");
            return "redirect:/usuarios/nuevo";
        }

        // Encriptar contraseña si no está en BCrypt
        if (!esBCrypt(usuario.getContrasena())) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }

        // 🔹 Crear el colaborador automáticamente SOLO si no existe
        if (usuario.getColaborador() == null && usuario.getRol() != null) {
            Colaborador colaborador = new Colaborador();
            colaborador.setUsuario(usuario);
            colaborador.setRol(usuario.getRol());
            usuario.setColaborador(colaborador);
        }

    } else {
        // Usuario existente → actualizar
        if (!usuario.getContrasena().equals(usuarioExistente.getContrasena())) {
            if (!esBCrypt(usuario.getContrasena())) {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        } else {
            usuario.setContrasena(usuarioExistente.getContrasena());
        }

        // 🔹 Mantener coherencia con colaborador existente
        if (usuario.getColaborador() == null && usuarioExistente.getColaborador() != null) {
            usuario.setColaborador(usuarioExistente.getColaborador());
        }
    }

    // 🔹 AGREGAR TRY-CATCH para capturar errores de base de datos
    try {
        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al guardar en base de datos: " + e.getMessage());
        e.printStackTrace(); // 🔹 Esto mostrará el error en la consola
        return "redirect:/usuarios/nuevo";
    }

    return "redirect:/usuarios";
}

    private boolean esBCrypt(String contrasena) {
        return contrasena != null && contrasena.startsWith("$2a$");
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) {
            return "redirect:/usuarios";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el usuario. Verifique si tiene relaciones asociadas.");
        }
        return "redirect:/usuarios";
    }
    
    // ---------------------
// 🔹 ENDPOINTS JSON (para Postman)
// ---------------------

// Listar todos los usuarios
@GetMapping("/api")
@ResponseBody
public List<Usuario> listarUsuariosJson() {
    return usuarioService.findAll();
}

// Obtener un usuario por ID
@GetMapping("/api/{id}")
@ResponseBody
public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
    Usuario usuario = usuarioService.findById(id);
    return (usuario != null) ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
}

// Crear usuario
@PostMapping("/api")
@ResponseBody
public ResponseEntity<Usuario> crearUsuarioJson(@RequestBody Usuario usuario) {
    if (!esBCrypt(usuario.getContrasena())) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
    }
    Usuario nuevo = usuarioService.save(usuario);
    return ResponseEntity.ok(nuevo);
}

// Actualizar usuario
@PutMapping("/api/{id}")
@ResponseBody
public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
    Usuario usuario = usuarioService.findById(id);
    if (usuario == null) {
        return ResponseEntity.notFound().build();
    }

    usuario.setPrimerNombre(usuarioDetalles.getPrimerNombre());
    usuario.setSegundoNombre(usuarioDetalles.getSegundoNombre());
    usuario.setPrimerApellido(usuarioDetalles.getPrimerApellido());
    usuario.setSegundoApellido(usuarioDetalles.getSegundoApellido());
    usuario.setCorreo(usuarioDetalles.getCorreo());
    usuario.setRol(usuarioDetalles.getRol());

    if (usuarioDetalles.getContrasena() != null && !usuarioDetalles.getContrasena().isBlank()) {
        if (!esBCrypt(usuarioDetalles.getContrasena())) {
            usuario.setContrasena(passwordEncoder.encode(usuarioDetalles.getContrasena()));
        } else {
            usuario.setContrasena(usuarioDetalles.getContrasena());
        }
    }

    Usuario actualizado = usuarioService.save(usuario);
    return ResponseEntity.ok(actualizado);
}

// Eliminar usuario
@DeleteMapping("/api/{id}")
@ResponseBody
public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
    try {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    } catch (Exception e) {
        return ResponseEntity.status(500).build();
    }
}

}