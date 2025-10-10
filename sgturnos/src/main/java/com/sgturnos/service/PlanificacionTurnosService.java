package com.sgturnos.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.sgturnos.model.*;
import com.sgturnos.repository.AsignacionTurnoRepository;
import com.sgturnos.repository.HorarioRepository;
import com.sgturnos.repository.TurnoRepository;
import com.sgturnos.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Para Excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Para PDF
import java.util.regex.Matcher;
import java.time.YearMonth;
import com.sgturnos.model.MallaTurnos;
import com.sgturnos.repository.MallaTurnosRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.geom.PageSize;
import static com.itextpdf.kernel.pdf.PdfName.A;
import static com.itextpdf.kernel.pdf.PdfName.R;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.stream.Collector;


@Service
@Transactional
public class PlanificacionTurnosService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private AsignacionTurnoRepository asignacionRepository;
    
    @Autowired
    private MallaTurnosRepository mallaTurnosRepository;

    private static final int HORAS_MES = 192;
    private static final int HORAS_TURNO = 12;
    private static final Logger log = LoggerFactory.getLogger(PlanificacionTurnosService.class);

    // ===================== CRUD Colaborador =====================
    public Colaborador crearColaborador(Colaborador c) {
        return colaboradorRepository.save(c);
    }

    public Optional<Colaborador> obtenerColaborador(Long idColaborador) {
        return colaboradorRepository.findById(idColaborador);
    }

    public List<Colaborador> listarColaboradores() {
        return colaboradorRepository.findAll();
    }

    public Colaborador actualizarColaborador(Long idColaborador, Colaborador datos) {
    Colaborador existente = colaboradorRepository.findById(idColaborador)
            .orElseThrow(() -> new RuntimeException("Colaborador no encontrado con id: " + idColaborador));

    // Actualizar rol y usuario
    existente.setRol(datos.getRol());
    existente.setUsuario(datos.getUsuario());

    return colaboradorRepository.save(existente);
}

    public void eliminarColaborador(Long idColaborador) {
        colaboradorRepository.deleteById(idColaborador);
    }

    // ===================== CRUD Horario =====================
    public Horario crearHorario(Horario h) {
        return horarioRepository.save(h);
    }

    public List<Horario> listarHorarios() {
        return horarioRepository.findAll();
    }

    public Horario actualizarHorario(String idHorario, Horario datos) {
        Horario existente = horarioRepository.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con id: " + idHorario));
        existente.setHoraInicio(datos.getHoraInicio());
        existente.setHoraFin(datos.getHoraFin());
        existente.setTipo(datos.getTipo());
        return horarioRepository.save(existente);
    }

    public void eliminarHorario(String idHorario) {
        horarioRepository.deleteById(idHorario);
    }

    // ===================== CRUD Turno =====================
    public Turno crearTurno(Turno t) {
        return turnoRepository.save(t);
    }

    public List<Turno> listarTurnos() {
        return turnoRepository.findAll();
    }

    public Turno actualizarTurno(Long idTurno, Turno datos) {
        Turno existente = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con id: " + idTurno));
        existente.setFechaIni(datos.getFechaIni());
        existente.setFechaFin(datos.getFechaFin());
        existente.setHorario(datos.getHorario());
        return turnoRepository.save(existente);
    }

    public void eliminarTurno(Long idTurno) {
        turnoRepository.deleteById(idTurno);
    }
    
    // ===================== CRUD MallaTurnos =====================
    public MallaTurnos crearMalla(MallaTurnos m) {
        return mallaTurnosRepository.save(m);
    }

    public Optional<MallaTurnos> obtenerMalla(Long idMalla) {
        return mallaTurnosRepository.findById(idMalla);
    }

    public List<MallaTurnos> listarMallas() {
        return mallaTurnosRepository.findAll();
    }

    public MallaTurnos actualizarMalla(Long idMalla, MallaTurnos datos) {
        MallaTurnos existente = mallaTurnosRepository.findById(idMalla)
                .orElseThrow(() -> new RuntimeException("Malla no encontrada con id: " + idMalla));
        existente.setEstado(datos.getEstado());
        existente.setMesMalla(datos.getMesMalla());
        existente.setRol(datos.getRol());
        existente.setContenido(datos.getContenido());
        existente.setFechaCreacion(datos.getFechaCreacion());
        return mallaTurnosRepository.save(existente);
    }

    public void eliminarMalla(Long idMalla) {
        mallaTurnosRepository.deleteById(idMalla);
    }

    // ===================== CRUD Asignaciones =====================
    public AsignacionTurno crearAsignacion(AsignacionTurno asignacion) {
        return asignacionRepository.save(asignacion);
    }

    public List<AsignacionTurno> listarAsignaciones() {
        return asignacionRepository.findAll();
    }

    public AsignacionTurno actualizarAsignacion(AsignacionTurnoPK id, AsignacionTurno datos) {
        AsignacionTurno existente = asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con id: " + id));
        existente.setColaborador(datos.getColaborador());
        existente.setTurno(datos.getTurno());
        existente.setFecha(datos.getFecha());
        existente.setObservaciones(datos.getObservaciones());
        existente.setMallaTurnos(datos.getMallaTurnos());
        return asignacionRepository.save(existente);
    }

    public void eliminarAsignacion(AsignacionTurnoPK id) {
        asignacionRepository.deleteById(id);
    }
   


    // ===================== Lógica avanzada (pendiente) =====================
    // Aquí irían tus métodos de generación de mallas, validaciones, exportar a PDF/Excel, etc.
    // public List<AsignacionTurno> generarMalla(YearMonth mes) { ... }
/**
 * Obtiene una asignación de turno por su clave compuesta (turno + colaborador + fecha).
 */
public AsignacionTurno obtenerPorId(Long idTurno, Long idColaborador, LocalDate fecha) {
    return asignacionRepository.findById(new AsignacionTurnoPK(idTurno, idColaborador, fecha))
            .orElseThrow(() -> new RuntimeException(
                    "Asignación no encontrada (turno=" + idTurno + ", colaborador=" + idColaborador + ", fecha=" + fecha + ")"));
}

/**
 * Edita una asignación de turno cambiando las observaciones.
 */
public void editarAsignacion(Long idTurno, Long idColaborador, LocalDate fecha, String observaciones) {
    AsignacionTurno a = obtenerPorId(idTurno, idColaborador, fecha);
    a.setObservaciones(observaciones);
    asignacionRepository.save(a);
}

/**
 * Elimina una asignación por su clave compuesta.
 */
public void eliminarAsignacion(Long idTurno, Long idColaborador, LocalDate fecha) {
    asignacionRepository.deleteById(new AsignacionTurnoPK(idTurno, idColaborador, fecha));
}

// ===============================================================
// 🔹 Método público para guardar una asignación con malla asociada
// ===============================================================

// ✅ Usado por el Controller
public void guardarAsignacion(Long idColaborador, Long idTurno, LocalDate fecha, String observaciones) {
    guardarAsignacion(idColaborador, idTurno, fecha, observaciones, null);
}

// ✅ Usado internamente por la generación automática de malla
public void guardarAsignacion(Long idColaborador, Long idTurno, LocalDate fecha, String observaciones, MallaTurnos malla) {

    // 🔸 Primero obtenemos las entidades asociadas
    Colaborador colaborador = colaboradorRepository.findById(idColaborador)
            .orElseThrow(() -> new RuntimeException("Colaborador no encontrado: " + idColaborador));
    Turno turno = turnoRepository.findById(idTurno)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado: " + idTurno));

    // 🔸 Creamos el objeto clave compuesta (PK)
    AsignacionTurnoPK pk = new AsignacionTurnoPK(idTurno, idColaborador, fecha);

    // 🔸 Construimos la asignación completa
    AsignacionTurno asignacion = new AsignacionTurno();
    asignacion.setId(pk); // 👈 clave embebida obligatoria
    asignacion.setColaborador(colaborador);
    asignacion.setTurno(turno);
    asignacion.setFecha(fecha);
    asignacion.setObservaciones(observaciones);
    asignacion.setMallaTurnos(malla);

    // 🔸 Guardamos
    asignacionRepository.save(asignacion);
}

// ===================== Generación de malla mensual =====================
public List<AsignacionTurno> generarMalla(YearMonth mes) {
    List<Colaborador> colaboradores = listarColaboradores();
    if (colaboradores.isEmpty()) throw new RuntimeException("No hay colaboradores para generar la malla.");

    Map<String, Horario> horarioPorTipo = listarHorarios()
            .stream()
            .collect(Collectors.toMap(h -> safeUpper(h.getTipo().name()), h -> h, (a, b) -> a));

    Horario H_DIA = requireHorario(horarioPorTipo, "DIA");
    Horario H_NOCHE = requireHorario(horarioPorTipo, "NOCHE");
    Horario H_LIBRE = requireHorario(horarioPorTipo, "LIBRE");
    Horario H_COMITE = requireHorario(horarioPorTipo, "COMITE");

    Map<String, Integer> idxRol = new HashMap<>();
    Map<Long, List<AsignacionTurno>> historialPorColaborador = new HashMap<>();
    colaboradores.forEach(c -> historialPorColaborador.put(c.getIdColaborador(), new ArrayList<>()));

    List<AsignacionTurno> resultado = new ArrayList<>();

    for (int d = 1; d <= mes.lengthOfMonth(); d++) {
        LocalDate fecha = mes.atDay(d);
        Set<Long> bloqueadosHoyPorPosturno = new HashSet<>();

          // Asignación de posturno
        for (Colaborador c : colaboradores) {
            List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador());
            if (!hist.isEmpty()) {
                AsignacionTurno ult = hist.get(hist.size() - 1);
                if (ult.getTurno().getHorario().getTipo() == Horario.TipoHorario.NOCHE) {
                    Turno tLibre = getOrCreateTurno(H_LIBRE, fecha);
                    AsignacionTurno a = crearAsignacion(c, tLibre, fecha, "Posturno automático");
if (a.getTurno() == null || a.getColaborador() == null) {
    log.error("Asignacion generada con null -> colaborador={} turno={} fecha={}", c, a.getTurno(), fecha);
    continue; // ignora esta asignación
}
                    asignacionRepository.save(a);
                    resultado.add(a);
                    hist.add(cloneLite(a));
                    bloqueadosHoyPorPosturno.add(c.getIdColaborador());
                }
            }
        }

        // Asignar cupos mínimos por rol
        asignarCuposPorRolParaTurno(fecha, "DIA", H_DIA,
                Map.of("MEDICO", 2, "ENFERMERO", 3, "AUXILIAR", 8, "TERAPIA", 2),
                colaboradores, historialPorColaborador, idxRol, bloqueadosHoyPorPosturno, resultado);

        asignarCuposPorRolParaTurno(fecha, "NOCHE", H_NOCHE,
                Map.of("MEDICO", 1, "ENFERMERO", 2, "AUXILIAR", 8, "TERAPIA", 2),
                colaboradores, historialPorColaborador, idxRol, bloqueadosHoyPorPosturno, resultado);

        Set<Long> yaAsignadosHoy = resultado.stream()
                .filter(a -> a.getFecha().equals(fecha))
                .map(a -> a.getColaborador().getIdColaborador())
                .collect(Collectors.toSet());

       for (Colaborador c : colaboradores) {
    if (!yaAsignadosHoy.contains(c.getIdColaborador())) {
        Turno tLibre = getOrCreateTurno(H_LIBRE, fecha);
        AsignacionTurno a = crearAsignacion(c, tLibre, fecha, "Libre");

        // ✅ Validación de seguridad
        if (a.getTurno() == null || a.getColaborador() == null) {
            log.error("Asignacion generada con null -> colaborador={} turno={} fecha={}", c, a.getTurno(), fecha);
            continue; // omite esta asignación y sigue con el siguiente colaborador
        }
        
                resultado.add(a);
                historialPorColaborador.get(c.getIdColaborador()).add(cloneLite(a));
            }
        }
    }

   intentarCompletarObjetivoHoras(mes, colaboradores, H_DIA, H_NOCHE, historialPorColaborador, resultado);
    asignarComiteEnPrimerLibre(mes, colaboradores, H_COMITE, resultado);

// Guardar en MallaTurnos (una sola malla por mes y rol)
Map<String, MallaTurnos> mallasPorRol = new HashMap<>();

for (AsignacionTurno a : resultado) {
    Colaborador c = a.getColaborador();
    String rolFinal = (c.getRol() != null ? safeUpper(c.getRol().getRol()) : "SIN_ROL");

    // Clave única: mes + rol
    String clave = mes.toString() + "_" + rolFinal;

    MallaTurnos malla = mallasPorRol.get(clave);
    if (malla == null) {
        malla = new MallaTurnos();
        malla.setEstado(MallaTurnos.EstadoMalla.GENERADA);
        malla.setMesMalla(mes.toString());
        malla.setRol(rolFinal);
        mallaTurnosRepository.save(malla);

        mallasPorRol.put(clave, malla);
    }

    // Cada asignación queda asociada a la malla de su rol
    a.setMallaTurnos(malla);
}

// Guardar todas las asignaciones
asignacionRepository.saveAll(resultado);

try {
    generarArchivoPDF(resultado, mes.toString());
    generarArchivoExcel(resultado, mes.toString());
} catch (IOException e) {
    log.error("Error al generar archivos de malla para {}", mes, e);
}

return resultado;

}

// ===================== Generar malla para un rol =====================
public List<AsignacionTurno> generarMallaPorRol(YearMonth mes, String rol) {
    List<Colaborador> todos = listarColaboradores();
    // trabajar siempre con una copia mutable
    List<Colaborador> colaboradores = new ArrayList<>(todos);

    // Si se especifica rol, filtrar la lista
    if (rol != null && !rol.isBlank() && !"TODOS".equalsIgnoreCase(rol)) {
        colaboradores = colaboradores.stream()
                .filter(c -> c.getRol() != null
                        && c.getRol().getRol() != null
                        && c.getRol().getRol().equalsIgnoreCase(rol))
                .collect(Collectors.toList());

        // 🔹 Ajustar cantidad de colaboradores necesarios automáticamente según rol
        int cupoDia = switch (rol.toUpperCase()) {
            case "TERAPIA" -> 2;
            case "MEDICO" -> 2;
            case "ENFERMERO" -> 3;
            case "AUXILIAR" -> 8;
            default -> 0;
        };

        int cupoNoche = switch (rol.toUpperCase()) {
            case "TERAPIA" -> 2;
            case "MEDICO" -> 1;
            case "ENFERMERO" -> 2;
            case "AUXILIAR" -> 8;
            default -> 0;
        };

        if (cupoDia > 0 && cupoNoche > 0) {
            int horasPorTurno = 12;
            int horasPorColaborador = 192;

            int totalTurnos = (cupoDia + cupoNoche) * mes.lengthOfMonth();
            int necesarios = (int) Math.ceil((totalTurnos * horasPorTurno) / (double) horasPorColaborador);

            Collections.shuffle(colaboradores);
            if (colaboradores.size() > necesarios) {
                colaboradores = colaboradores.subList(0, necesarios);
            }

            log.info("🔹 Se usarán {} {}(es) para cubrir el mes de {} ({} turnos de 12h)",
                    colaboradores.size(), rol.toUpperCase(), mes, totalTurnos);
        }
    }

    if (colaboradores.isEmpty()) {
        log.warn("No hay colaboradores para el rol: {}", rol);
        return new ArrayList<>();
    }

    Map<String, Horario> horarioPorTipo = listarHorarios()
            .stream()
            .collect(Collectors.toMap(h -> safeUpper(h.getTipo().name()), h -> h, (a, b) -> a));

    Horario H_DIA = requireHorario(horarioPorTipo, "DIA");
    Horario H_NOCHE = requireHorario(horarioPorTipo, "NOCHE");
    Horario H_LIBRE = requireHorario(horarioPorTipo, "LIBRE");
    Horario H_COMITE = requireHorario(horarioPorTipo, "COMITE");

    Map<Long, List<AsignacionTurno>> historialPorColaborador = new HashMap<>();
    Map<Long, Integer> horasAsignadas = new HashMap<>(); // Control de 192h por colaborador
    colaboradores.forEach(c -> {
        historialPorColaborador.put(c.getIdColaborador(), new ArrayList<>());
        horasAsignadas.put(c.getIdColaborador(), 0);
    });

    List<AsignacionTurno> resultado = new ArrayList<>();
    Random rnd = new Random();

    // Cupos por rol según turno (mapa maestro, se usa para inicializar disponibles)
    Map<String, Integer> cuposDia = Map.of(
            "TERAPIA", 2,
            "MEDICO", 2,
            "ENFERMERO", 3,
            "AUXILIAR", 8
    );
    Map<String, Integer> cuposNoche = Map.of(
            "TERAPIA", 2,
            "MEDICO", 1,
            "ENFERMERO", 2,
            "AUXILIAR", 8
    );

    for (int d = 1; d <= mes.lengthOfMonth(); d++) {
        LocalDate fecha = mes.atDay(d);

        // Clonar cupos para el día
        Map<String, Integer> disponiblesDia = new HashMap<>(cuposDia);
        Map<String, Integer> disponiblesNoche = new HashMap<>(cuposNoche);

        for (Colaborador c : colaboradores) {
            List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador());
            String ultimo = hist.isEmpty()
                    ? "LIBRE"
                    : Optional.ofNullable(hist.get(hist.size() - 1).getTurno())
                            .map(Turno::getHorario)
                            .map(Horario::getTipo)
                            .map(Object::toString)
                            .orElse("LIBRE");

            String penultimo = hist.size() < 2
                    ? "LIBRE"
                    : Optional.ofNullable(hist.get(hist.size() - 2).getTurno())
                            .map(Turno::getHorario)
                            .map(Horario::getTipo)
                            .map(Object::toString)
                            .orElse("LIBRE");

            List<Horario> posibles = new ArrayList<>();
            posibles.add(H_LIBRE); // siempre posible al inicio

            // Nombre del rol del colaborador en MAYÚSCULAS (seguro contra nulls)
            String rolNombre = (c.getRol() != null && c.getRol().getRol() != null)
                    ? c.getRol().getRol().toUpperCase(Locale.ROOT)
                    : "SIN_ROL";

            // Determinar posibles turnos de DÍA
            if (disponiblesDia.getOrDefault(rolNombre, 0) > 0 &&
                    !(ultimo.equals("DIA") && penultimo.equals("DIA")) &&
                    horasAsignadas.get(c.getIdColaborador()) + 12 <= 192) {
                posibles.add(H_DIA);
            }

            // Determinar posibles turnos de NOCHE
            if (disponiblesNoche.getOrDefault(rolNombre, 0) > 0 &&
                    !(ultimo.equals("NOCHE") && penultimo.equals("NOCHE")) &&
                    horasAsignadas.get(c.getIdColaborador()) + 12 <= 192) {
                posibles.add(H_NOCHE);
            }

            // ❌ Nunca permitir NOCHE → DÍA
            if ("NOCHE".equals(ultimo) && posibles.contains(H_DIA)) {
                posibles.remove(H_DIA);
            }

            // ❌ Evitar más de 2 días libres seguidos
            if ("LIBRE".equals(ultimo) && "LIBRE".equals(penultimo) && posibles.contains(H_LIBRE)) {
                posibles.remove(H_LIBRE);
            }

            // Asegurar que no quede vacío (fallback a LIBRE)
            if (posibles.isEmpty()) {
                posibles.add(H_LIBRE);
            }

            // Elegir turno aleatorio válido
            Horario turnoHoy = posibles.get(rnd.nextInt(posibles.size()));
            AsignacionTurno a = crearAsignacion(c, getOrCreateTurno(turnoHoy, fecha), fecha,
                    turnoHoy == H_LIBRE ? "Libre" : "Asignación dinámica");

            resultado.add(a);
            historialPorColaborador.get(c.getIdColaborador()).add(cloneLite(a));

            // Actualizar horas y cupos (usar rolNombre como clave)
            if (turnoHoy == H_DIA) {
                horasAsignadas.put(c.getIdColaborador(), horasAsignadas.get(c.getIdColaborador()) + 12);
                disponiblesDia.put(rolNombre, disponiblesDia.getOrDefault(rolNombre, 0) - 1);
            } else if (turnoHoy == H_NOCHE) {
                horasAsignadas.put(c.getIdColaborador(), horasAsignadas.get(c.getIdColaborador()) + 12);
                disponiblesNoche.put(rolNombre, disponiblesNoche.getOrDefault(rolNombre, 0) - 1);
            }
        }
    }

    // Asignar comités en primeros días libres según disponibilidad y descontar 3h
    asignarComiteEnPrimerLibreConHoras(mes, colaboradores, H_COMITE, resultado, horasAsignadas);

    // Generación de archivos
    try {
        String mesBase = mes.toString();
        String sufijo = (rol == null || rol.isBlank() ? "TODOS" : rol.toUpperCase());
        String nombreArchivo = mesBase + "_" + sufijo;

        generarArchivoPDF(resultado, nombreArchivo);
        generarArchivoExcel(resultado, nombreArchivo);
    } catch (IOException e) {
        log.error("Error al generar archivos de malla por rol para {} - {}", mes, rol, e);
    }

    // ===================== Guardar registro de malla =====================
    String rolFinal = (rol == null || rol.isBlank() || "TODOS".equalsIgnoreCase(rol))
            ? "TODOS"
            : rol.toUpperCase();

    // Variables de ejemplo para estado
    boolean aprobada = false;
    boolean editada = false;

    try {
        // Serializar solo los datos esenciales de la malla, no todo el objeto AsignacionTurno
        ArrayList<Map<String, Object>> mallaSimple = resultado.stream()
                .<Map<String, Object>>map(a -> Map.of(
                        "colaboradorId", a.getColaborador().getIdColaborador(),
                        "turnoId", a.getTurno().getIdTurno(),
                        "fecha", a.getFecha(),
                        "observaciones", a.getObservaciones()
                ))
                .collect(Collectors.toCollection(ArrayList::new));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(mallaSimple);   // serializamos solo los datos esenciales
        oos.close();

        MallaTurnos mallaGeneral = new MallaTurnos();
        mallaGeneral.setMesMalla(mes.toString());
        mallaGeneral.setRol(rolFinal);

        MallaTurnos.EstadoMalla estadoMalla;
        if (aprobada) {
            estadoMalla = MallaTurnos.EstadoMalla.ENVIADA;
        } else if (editada) {
            estadoMalla = MallaTurnos.EstadoMalla.EDITADA;
        } else {
            estadoMalla = MallaTurnos.EstadoMalla.GENERADA;
        }

        mallaGeneral.setEstado(estadoMalla);
        mallaGeneral.setContenido(baos.toByteArray());
        mallaTurnosRepository.save(mallaGeneral);

    } catch (IOException e) {
        log.error("Error al serializar y guardar la malla para {} - {}", mes, rol, e);
    }

    // 👉 Crear y asociar UNA malla por rol/mes
    MallaTurnos malla = new MallaTurnos();
    malla.setEstado(MallaTurnos.EstadoMalla.GENERADA);
    malla.setMesMalla(mes.toString());
    malla.setRol(rolFinal);
    mallaTurnosRepository.save(malla);

    // Asociar esta malla a todas las asignaciones generadas
    for (AsignacionTurno a : resultado) {
        a.setMallaTurnos(malla);
    }

    // ⚡ Guardar todas las asignaciones de golpe
    asignacionRepository.saveAll(resultado);

    return resultado;
}

// ===================== Construir mallas por rol =====================
/**
 * Construye un mapa mallasPorRol: rol -> MallaDTO (días + filas con turnos).
 *
 * @param asignaciones lista de AsignacionTurno usada para construir las mallas
 * @param ym           YearMonth que indica el mes para la malla
 * @return mapa (rol -> MallaDTO) con la malla por cada rol
 */
    public Map<String, MallaDTO> armarMallasPorRol(List<AsignacionTurno> asignaciones, YearMonth ym) {
    Map<String, MallaDTO> salida = new LinkedHashMap<>();

    // Agrupar asignaciones por rol del colaborador
    Map<String, List<AsignacionTurno>> byRol = asignaciones.stream()
            .filter(a -> a.getColaborador() != null)
            .collect(Collectors.groupingBy(a -> {
                String r = a.getColaborador().getRol() != null ? a.getColaborador().getRol().getRol() : "SIN_ROL";
                return safeUpper(r);
            }));

    List<Integer> dias = new ArrayList<>();
    for (int d = 1; d <= ym.lengthOfMonth(); d++) dias.add(d);

    for (Map.Entry<String, List<AsignacionTurno>> e : byRol.entrySet()) {
        String rol = e.getKey();
        List<AsignacionTurno> grupo = e.getValue();

        // Lista de colaboradores únicos para este rol
       List<Colaborador> cols = grupo.stream()
        .map(AsignacionTurno::getColaborador)
        .distinct()
        .sorted(Comparator.comparing(Colaborador::getIdColaborador))
        .toList();


        List<MallaDTO.Row> filas = new ArrayList<>();

        for (Colaborador c : cols) {
    MallaDTO.Row fila = new MallaDTO.Row();
    // Usar directamente el Id del colaborador
    fila.colaborador = c.getUsuario().getPrimerNombre() + " " + c.getUsuario().getPrimerApellido(); 
    fila.turnos = new ArrayList<>();

    for (int d = 1; d <= ym.lengthOfMonth(); d++) {
        LocalDate fecha = ym.atDay(d);
        String turnoStr = grupo.stream()
        .filter(a2 -> a2.getColaborador() != null
                && Objects.equals(a2.getColaborador().getIdColaborador(), c.getIdColaborador())
                && Objects.equals(a2.getFecha(), fecha))
        .map(a2 -> a2.getTurno() != null && a2.getTurno().getHorario() != null
                ? a2.getTurno().getHorario().getTipo().name()  // 👈 .name() pasa enum → String
                : "LIBRE")
        .findFirst()
        .orElse("LIBRE");
        fila.turnos.add(turnoStr);
    }
    filas.add(fila);
}

MallaDTO dto = new MallaDTO();
dto.dias = dias;
dto.filas = filas;
salida.put(rol, dto);

    }

    return salida;
}

// ===================== DTO simple para la vista =====================
/** DTO pequeño para pasar a la vista: dias + filas (colaborador + lista de turnos por día) */
public static class MallaDTO {
    public List<Integer> dias;
    public List<Row> filas;

    public static class Row {
        public String colaborador;
        public List<String> turnos;
    }
}

// ===================== MÉTODOS AUXILIARES =====================

private String rolUpper(Colaborador c) {
    return (c.getRol() == null || c.getRol().getRol() == null) 
           ? "SIN_ROL" 
           : c.getRol().getRol().toUpperCase();
}

private void asignarComiteEnPrimerLibreConHoras(
        YearMonth mes,
        List<Colaborador> colaboradores,
        Horario H_COMITE,
        List<AsignacionTurno> resultado,
        Map<Long, Integer> horasAsignadas) {

    for (Colaborador c : colaboradores) {
        List<AsignacionTurno> hist = resultado.stream()
                .filter(a -> a.getColaborador().getIdColaborador() == c.getIdColaborador())
                .sorted(Comparator.comparing(AsignacionTurno::getFecha))
                .toList();

        for (AsignacionTurno a : hist) {
            if ("LIBRE".equals(a.getTurno().getHorario().getTipo()) &&
                horasAsignadas.get(c.getIdColaborador()) + 3 <= HORAS_MES) {

                // Reemplazar turno libre por COMITÉ
                AsignacionTurno comite = crearAsignacion(
                        c,
                        getOrCreateTurno(H_COMITE, a.getFecha()),
                        a.getFecha(),
                        "Comité"
                );

                if (comite.getTurno() == null || comite.getColaborador() == null) {
                    log.error("Asignacion Comité generada con null -> colaborador={} turno={} fecha={}", c, comite.getTurno(), a.getFecha());
                    continue; // pasa al siguiente colaborador
                }
                
                resultado.remove(a);
                resultado.add(comite);

                // Actualizar historial y horas
                hist.remove(a);
                hist.add(cloneLite(comite));
                horasAsignadas.put(c.getIdColaborador(), horasAsignadas.get(c.getIdColaborador()) + 3);
                break; // solo un comité por colaborador en el mes por esta regla
            }
        }
    }
}

  

// ===================== Helpers =====================

// ------------------------
// Helpers robustos para tipos
// ------------------------

/**
 * Devuelve el tipo de una AsignacionTurno como String en mayúsculas ("DIA","NOCHE","LIBRE","COMITE").
 * Seguro contra nulls.
 */
private String getTipo(AsignacionTurno a) {
    if (a == null) return "LIBRE";
    Turno t = a.getTurno();
    if (t == null) return "LIBRE";
    Horario h = t.getHorario();
    if (h == null || h.getTipo() == null) return "LIBRE";
    return h.getTipo().name();
}


/**
 * Comprueba si un Horario corresponde al tipo esperado (comparación por nombre del enum).
 * Seguro contra nulls.
 */
private boolean isTipo(Horario h, String esperado) {
    if (h == null || h.getTipo() == null || esperado == null) return false;
    return h.getTipo().name().equalsIgnoreCase(esperado.trim());
}

/**
 * Comprueba si una AsignacionTurno corresponde al tipo esperado (ej: "NOCHE").
 * Usa internamente isTipo(Horario,...).
 */
private boolean isTipo(AsignacionTurno a, String esperado) {
    if (a == null || a.getTurno() == null) return false;
    return isTipo(a.getTurno().getHorario(), esperado);
}

/** Asigna cupos mínimos por rol para un turno específico */
private void asignarCuposPorRolParaTurno(LocalDate fecha, String tipoTurnoStr, Horario horarioTurno,
                                         Map<String, Integer> cuposMinimos, List<Colaborador> colaboradores,
                                         Map<Long, List<AsignacionTurno>> historialPorColaborador,
                                         Map<String, Integer> idxRol,
                                         Set<Long> bloqueadosHoy,
                                         List<AsignacionTurno> resultado) {

    for (Map.Entry<String, Integer> entry : cuposMinimos.entrySet()) {
        String rol = entry.getKey();
        int cupo = entry.getValue();

        List<Colaborador> candidatos = colaboradores.stream()
                .filter(c -> rolEquals(c, rol))
                .collect(Collectors.toList());

        if (candidatos.isEmpty()) {
            log.warn("No hay colaboradores con rol {} para cubrir {} el {}. Se omite.", rol, tipoTurnoStr, fecha);
            continue;
        }

        int start = idxRol.getOrDefault(rol, 0);
        int asignados = 0;
        int intentos = 0;

        while (asignados < cupo && intentos < candidatos.size() * 2) {
            Colaborador c = candidatos.get(start % candidatos.size());
            start++;

            if (bloqueadosHoy.contains(c.getIdColaborador())) {
                intentos++;
                continue;
            }

            List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador());

            if (puedeAsignar(tipoTurnoStr, hist)) {
                boolean yaTieneHoy = hist.stream().anyMatch(a -> a.getFecha().equals(fecha));
                if (!yaTieneHoy) {
                    Turno t = getOrCreateTurno(horarioTurno, fecha);
                    AsignacionTurno a = crearAsignacion(c, t, fecha, "Cobertura mínima " + tipoTurnoStr);
                    resultado.add(a);
                    hist.add(cloneLite(a));
                    asignados++;
                }
            }
            intentos++;
        }

        if (asignados < cupo) {
            log.warn("No se pudo cubrir cupo de {} en {} el {}. Asignados: {} / Requeridos: {}", rol, tipoTurnoStr, fecha, asignados, cupo);
        }

        idxRol.put(rol, start % candidatos.size());
    }
}

/** Verifica si se puede asignar un turno según reglas de descanso y consecutividad */
private boolean puedeAsignar(String tipoTurno, List<AsignacionTurno> hist) {
    return puedeAsignar(tipoTurno, hist, null);
}

private boolean puedeAsignar(String tipoTurno, List<AsignacionTurno> hist, LocalDate fechaObjetivo) {
    String tipo = safeUpper(tipoTurno);

    int size = hist.size();

    // ===== Regla 1: No más de 2 noches consecutivas =====
    if ("NOCHE".equals(tipo) && size >= 2) {
        String t1 = getTipo(hist.get(size - 1));
        String t2 = getTipo(hist.get(size - 2));
        if ("NOCHE".equals(t1) && "NOCHE".equals(t2)) return false;
    }

    // ===== Regla 2: No se puede asignar día inmediatamente después de noche =====
    if ("DIA".equals(tipo) && size >= 1) {
        String ultimo = getTipo(hist.get(size - 1));
        if ("NOCHE".equals(ultimo)) return false;
    }

    // ===== Regla 3: Validar fecha objetivo =====
    if (fechaObjetivo != null) {
        boolean yaTiene = hist.stream().anyMatch(a -> a.getFecha().equals(fechaObjetivo));
        if (yaTiene) return false;
    }

    return true;
}

/** Convierte string a mayúsculas seguro */
private String safeUpper(String s) {
    return s == null ? "" : s.toUpperCase(Locale.ROOT).trim();
}

/** Compara el rol de un colaborador con el rol esperado */
private boolean rolEquals(Colaborador c, String rolEsperado) {
    String nombre = c.getRol() != null ? c.getRol().getRol() : null;
    return safeUpper(nombre).equals(safeUpper(rolEsperado));
}

/** Obtiene un turno existente o crea uno nuevo */
private Turno getOrCreateTurno(Horario horario, LocalDate fecha) {
    if (horario == null) {
        log.error("Se intentó crear un turno con horario null en fecha: {}", fecha);
        throw new IllegalArgumentException("Horario no puede ser null al crear un turno para fecha: " + fecha);
    }

    Optional<Turno> found = turnoRepository.findAll().stream()
            .filter(t -> fecha.equals(t.getFechaIni()) && fecha.equals(t.getFechaFin())
                    && t.getHorario() != null
                    && Objects.equals(t.getHorario().getIdHorario(), horario.getIdHorario()))
            .findFirst();

    if (found.isPresent()) return found.get();

    Turno t = new Turno();
    t.setFechaIni(fecha);
    t.setFechaFin(fecha);
    t.setHorario(horario);

    // Si tu TurnoRepository extiende JpaRepository, usa saveAndFlush para asegurar el ID inmediatamente.
    // Si no, save() normalmente funciona; ajusta según tu repo.
    try {
        return ((org.springframework.data.jpa.repository.JpaRepository<Turno, Long>) turnoRepository).saveAndFlush(t);
    } catch (ClassCastException ex) {
        return turnoRepository.save(t);
    }
}

/** Crea una asignación completa (asegurando que el Turno tenga id) */
private AsignacionTurno crearAsignacion(Colaborador c, Turno t, LocalDate fecha, String obs) {
    if (c == null) {
        log.error("crearAsignacion: colaborador null para fecha {}", fecha);
        throw new IllegalArgumentException("Colaborador no puede ser null");
    }
    if (t == null) {
        log.error("crearAsignacion: turno null para colaborador {} fecha {}", c.getIdColaborador(), fecha);
        throw new IllegalArgumentException("Turno no puede ser null");
    }

    // asegurar que el Turno esté persistido y tenga id
    if (t.getIdTurno() == null) {
        try {
            t = ((org.springframework.data.jpa.repository.JpaRepository<Turno, Long>) turnoRepository).saveAndFlush(t);
        } catch (ClassCastException ex) {
            t = turnoRepository.save(t);
        }
    }

    AsignacionTurno a = new AsignacionTurno();
    a.setId(new AsignacionTurnoPK(t.getIdTurno(), c.getIdColaborador(), fecha));
    a.setColaborador(c);
    a.setTurno(t);
    a.setFecha(fecha);
    a.setObservaciones(obs);
    return a;
}


/** Clona una asignación de forma ligera */
private AsignacionTurno cloneLite(AsignacionTurno a) {
    return crearAsignacion(a.getColaborador(), a.getTurno(), a.getFecha(), a.getObservaciones());
}

/** Obtiene un horario por tipo, lanza excepción si no existe */
private Horario requireHorario(Map<String, Horario> porTipo, String tipo) {
    Horario h = porTipo.get(safeUpper(tipo));
    if (h == null) throw new RuntimeException("No existe HORARIO de tipo: " + tipo);
    return h;
}

/** Extrae año y mes de un string en formato yyyy-MM */
private YearMonth extractYearMonth(String input) {
    if (input == null) throw new IllegalArgumentException("El parámetro 'input' es null");

    Matcher m = Pattern.compile("(\\d{4}-\\d{2})").matcher(input);
    if (m.find()) return YearMonth.parse(m.group(1));

    throw new IllegalArgumentException("Formato inválido para mes: " + input);
}


    // ===================== Métodos faltantes funcionales =====================
    private void intentarCompletarObjetivoHoras(YearMonth mes, List<Colaborador> colaboradores,
                                            Horario H_DIA, Horario H_NOCHE,
                                            Map<Long, List<AsignacionTurno>> historialPorColaborador,
                                            List<AsignacionTurno> resultado) {
    for (Colaborador c : colaboradores) {
        List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador());
        if (hist == null) continue;

        long turnos12h = hist.stream()
                .filter(a -> a.getTurno() != null
                        && a.getTurno().getHorario() != null
                        && (isTipo(a.getTurno().getHorario(), "DIA") || isTipo(a.getTurno().getHorario(), "NOCHE")))
                .count();

        int safety = 0; // to avoid infinite loops
        while (turnos12h * HORAS_TURNO < HORAS_MES && safety++ < 500) {
            Optional<AsignacionTurno> libreOpt = hist.stream()
                    .filter(a -> a.getTurno() != null
                            && a.getTurno().getHorario() != null
                            && isTipo(a.getTurno().getHorario(), "LIBRE"))
                    .findFirst();
            if (libreOpt.isEmpty()) break;

            AsignacionTurno libre = libreOpt.get();
            LocalDate fecha = libre.getFecha();

            // respeta reglas antes de reemplazar
            if (!puedeAsignar("DIA", hist, fecha)) {
                // si no se puede poner DIA en esa fecha, elimina ese libre temporal y busca otro
                // (evita quedarse pegado en el mismo libre)
                hist.remove(libre);
                continue;
            }

            Turno t = getOrCreateTurno(H_DIA, fecha);
            AsignacionTurno a = crearAsignacion(c, t, fecha, "Completando objetivo horas");
            if (a.getTurno() == null || a.getColaborador() == null) {
                log.error("intentarCompletarObjetivoHoras: asignación inválida colaborador={} fecha={}", c.getIdColaborador(), fecha);
                break;
            }

            asignacionRepository.save(a);
            resultado.add(a);

            // Reemplazar el libre en el historial por la nueva asignación (mantener orden)
            int idx = hist.indexOf(libre);
            if (idx >= 0) hist.set(idx, cloneLite(a));
            else hist.add(cloneLite(a));

            turnos12h++;
        }
    }
}


    private void asignarComiteEnPrimerLibre(YearMonth mes, List<Colaborador> colaboradores,
                                            Horario H_COMITE, List<AsignacionTurno> resultado) {
        for (Colaborador c : colaboradores) {
            List<AsignacionTurno> hist = resultado.stream()
                    .filter(a -> a.getColaborador().getIdColaborador().equals(c.getIdColaborador()))
                    .toList();
            Optional<AsignacionTurno> libre = hist.stream().filter(a -> isTipo(a.getTurno().getHorario(), "LIBRE")).findFirst();
            libre.ifPresent(a -> {
                Turno t = getOrCreateTurno(H_COMITE, a.getFecha());
                AsignacionTurno nuevo = crearAsignacion(c, t, a.getFecha(), "Comité asignado");
                asignacionRepository.save(nuevo);
                resultado.add(nuevo);
            });
        }
    }


 // ===================== Exportar tipo calendario =====================
public void generarArchivoExcel(List<AsignacionTurno> asignaciones, String mes) throws IOException {
    Path ruta = Paths.get("mallas");
    if (!Files.exists(ruta)) Files.createDirectories(ruta);

    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Malla " + mes);
        YearMonth ym;
try {
    ym = extractYearMonth(mes);
} catch (Exception ex) {
    throw new RuntimeException("generarArchivoExcel: formato de mes inválido: '" + mes + "'", ex);
}
int diasMes = ym.lengthOfMonth();

        // Estilo para encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Estilo para celdas normales
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        // Obtener lista de colaboradores únicos ordenados
        List<Colaborador> colaboradores = asignaciones.stream()
                .map(AsignacionTurno::getColaborador)
                .distinct()
                .sorted(Comparator.comparing(c -> c.getUsuario().getPrimerNombre() + " " + c.getUsuario().getPrimerApellido()))
                .toList();

        // === Encabezado ===
        Row header = sheet.createRow(0);
        Cell colabHeader = header.createCell(0);
        colabHeader.setCellValue("Colaborador");
        colabHeader.setCellStyle(headerStyle);

        for (int d = 1; d <= diasMes; d++) {
            Cell cell = header.createCell(d);
            cell.setCellValue(d);
            cell.setCellStyle(headerStyle);
        }

        // === Filas por colaborador ===
        int rowIdx = 1;
        for (Colaborador c : colaboradores) {
            Row row = sheet.createRow(rowIdx++);
            Cell colabCell = row.createCell(0);
            colabCell.setCellValue(c.getUsuario().getPrimerNombre() + " " + c.getUsuario().getPrimerApellido());
            colabCell.setCellStyle(cellStyle);

            for (int d = 1; d <= diasMes; d++) {
                LocalDate fecha = ym.atDay(d);
                Horario.TipoHorario turno = asignaciones.stream()
        .filter(a -> a.getColaborador().getIdColaborador().equals(c.getIdColaborador()) 
                  && a.getFecha().equals(fecha))
        .map(a -> a.getTurno().getHorario().getTipo())
        .findFirst()
        .orElse(Horario.TipoHorario.LIBRE); // 👈 usamos el enum, no string

Cell turnoCell = row.createCell(d);
turnoCell.setCellValue(turno.name()); // 👈 convierte enum a texto ("DIA", "NOCHE", "LIBRE")
turnoCell.setCellStyle(cellStyle);
            }
        }

        // Ajustar ancho de columnas
        for (int i = 0; i <= diasMes; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(ruta.resolve("malla_" + mes + ".xlsx").toFile())) {
            workbook.write(fileOut);
        }
    }
    
    // Guardar contenido en base de datos (opcional)
byte[] datosExcel = Files.readAllBytes(ruta.resolve("malla_" + mes + ".xlsx"));
MallaTurnos malla = new MallaTurnos();
malla.setMesMalla(mes);
malla.setEstado(MallaTurnos.EstadoMalla.GENERADA);
malla.setRol("GENERAL");
malla.setContenido(datosExcel);
malla.setFechaCreacion(LocalDateTime.now());
mallaTurnosRepository.save(malla);

}
public void generarArchivoPDF(List<AsignacionTurno> asignaciones, String mes) throws IOException {
    Path ruta = Paths.get("mallas");
    if (!Files.exists(ruta)) Files.createDirectories(ruta);

    PdfWriter writer = new PdfWriter(ruta.resolve("malla_" + mes + ".pdf").toFile());
    PdfDocument pdfDoc = new PdfDocument(writer);

    // Usamos A3 horizontal para más espacio
    try (Document document = new Document(pdfDoc, PageSize.A3.rotate())) {
        document.setMargins(10, 10, 10, 10);

        // Fuente compacta
        PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

        // Título
        document.add(new Paragraph("Malla de Turnos - " + mes)
                .setFont(font)
                .setBold()
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(12));

       YearMonth ym;
try {
    ym = extractYearMonth(mes);
} catch (Exception ex) {
    throw new RuntimeException("generarArchivoPdf: formato de mes inválido: '" + mes + "'", ex);
}
int diasMes = ym.lengthOfMonth();


        // Colaboradores ordenados
        List<Colaborador> colaboradores = asignaciones.stream()
                .map(AsignacionTurno::getColaborador)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(c -> (c.getUsuario() != null
                        ? (c.getUsuario().getPrimerNombre() + " " + c.getUsuario().getPrimerApellido())
                        : "")))
                .toList();

        // Ancho columnas
        float[] columnWidths = new float[diasMes + 1];
        columnWidths[0] = 3f;
        for (int i = 1; i <= diasMes; i++) columnWidths[i] = 1f;

        Table table = new Table(UnitValue.createPercentArray(columnWidths))
                .useAllAvailableWidth()
                .setFont(font)
                .setFontSize(6); // fuente reducida

        // === Encabezado ===
        table.addHeaderCell(new com.itextpdf.layout.element.Cell()
                .add(new Paragraph("Colaborador"))
                .setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));

        for (int d = 1; d <= diasMes; d++) {
            table.addHeaderCell(new com.itextpdf.layout.element.Cell()
                    .add(new Paragraph(String.valueOf(d)))
                    .setBold()
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setRotationAngle(Math.toRadians(90))); // rotado
        }

        // === Filas ===
        for (Colaborador c : colaboradores) {
            String nombre = (c.getUsuario() != null)
                    ? c.getUsuario().getPrimerNombre() + " " + c.getUsuario().getPrimerApellido()
                    : ("Colaborador " + (c.getIdColaborador() != null ? c.getIdColaborador() : ""));

            table.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new Paragraph(nombre)));

            for (int d = 1; d <= diasMes; d++) {
                LocalDate fecha = ym.atDay(d);
                Horario.TipoHorario turno = asignaciones.stream()
        .filter(a -> a.getColaborador() != null
                  && Objects.equals(a.getColaborador().getIdColaborador(), c.getIdColaborador())
                  && Objects.equals(a.getFecha(), fecha))
        .map(a -> {
            if (a.getTurno() != null && a.getTurno().getHorario() != null && a.getTurno().getHorario().getTipo() != null) {
                return a.getTurno().getHorario().getTipo();
            }
            return Horario.TipoHorario.LIBRE;
        })
        .findFirst()
        .orElse(Horario.TipoHorario.LIBRE);

    // convertir enum a string para mostrar en el PDF
    String turnoStr = turno.name(); // "DIA", "NOCHE", "LIBRE", "COMITE"
    table.addCell(new com.itextpdf.layout.element.Cell()
            .add(new Paragraph(turnoStr))
            .setTextAlignment(TextAlignment.CENTER));
}
        }

        document.add(table);
    }
    
    // Guardar PDF en base de datos
byte[] datosPdf = Files.readAllBytes(ruta.resolve("malla_" + mes + ".pdf"));
MallaTurnos malla = new MallaTurnos();
malla.setMesMalla(mes);
malla.setEstado(MallaTurnos.EstadoMalla.GENERADA);
malla.setRol("GENERAL");
malla.setContenido(datosPdf);
malla.setFechaCreacion(LocalDateTime.now());
mallaTurnosRepository.save(malla);
}
}