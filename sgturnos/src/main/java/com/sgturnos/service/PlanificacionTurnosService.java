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
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.util.regex.Pattern;


@Service
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

    // ===================== Listados =====================
    public List<Colaborador> listarColaboradores() {
        return colaboradorRepository.findAll();
    }

    public List<Horario> listarHorarios() {
        return horarioRepository.findAll();
    }

    public List<AsignacionTurno> listarAsignaciones() {
        return asignacionRepository.findAll();
    }

    // ===================== CRUD Asignaciones =====================
    public void guardarAsignacion(Long idColaborador, Long idTurno, LocalDate fecha, String observaciones) {
        Colaborador colaborador = colaboradorRepository.findById(idColaborador)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado: " + idColaborador));

        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado: " + idTurno));

        AsignacionTurno asignacion = new AsignacionTurno();
        asignacion.setId(new AsignacionTurnoPK(idTurno, idColaborador, fecha));
        asignacion.setColaborador(colaborador);
        asignacion.setTurno(turno);
        asignacion.setFecha(fecha);
        asignacion.setObservaciones(observaciones);

        asignacionRepository.save(asignacion);
    }

    public AsignacionTurno obtenerPorId(Long idTurno, Long idColaborador, LocalDate fecha) {
        return asignacionRepository.findById(new AsignacionTurnoPK(idTurno, idColaborador, fecha))
                .orElseThrow(() -> new RuntimeException(
                        "Asignación no encontrada (turno=" + idTurno + ", colaborador=" + idColaborador + ", fecha=" + fecha + ")"));
    }

    public void editarAsignacion(Long idTurno, Long idColaborador, LocalDate fecha, String observaciones) {
        AsignacionTurno a = obtenerPorId(idTurno, idColaborador, fecha);
        a.setObservaciones(observaciones);
        asignacionRepository.save(a);
    }

    public void eliminarAsignacion(Long idTurno, Long idColaborador, LocalDate fecha) {
        asignacionRepository.deleteById(new AsignacionTurnoPK(idTurno, idColaborador, fecha));
    }

  // ===================== Generación de malla mensual =====================
public List<AsignacionTurno> generarMalla(YearMonth mes) {
    List<Colaborador> colaboradores = listarColaboradores();
    if (colaboradores.isEmpty()) throw new RuntimeException("No hay colaboradores para generar la malla.");

    Map<String, Horario> horarioPorTipo = listarHorarios()
            .stream()
            .collect(Collectors.toMap(h -> safeUpper(h.getTipo()), h -> h, (a, b) -> a));

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

        for (Colaborador c : colaboradores) {
            List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador());
            if (!hist.isEmpty()) {
                AsignacionTurno ult = hist.get(hist.size() - 1);
                if (isTipo(ult.getTurno().getHorario(), "NOCHE")) {
                    Turno tLibre = getOrCreateTurno(H_LIBRE, fecha);
                    AsignacionTurno a = crearAsignacion(c, tLibre, fecha, "Posturno automático");
                    asignacionRepository.save(a);
                    resultado.add(a);
                    hist.add(cloneLite(a));
                    bloqueadosHoyPorPosturno.add(c.getIdColaborador());
                }
            }
        }

        asignarCuposPorRolParaTurno(fecha, "DIA", H_DIA,
                Map.of("MEDICO", 2, "ENFERMERO", 3, "AUXILIAR", 8, "TERAPIA", 2),
                colaboradores, historialPorColaborador, idxRol, bloqueadosHoyPorPosturno, resultado);

        asignarCuposPorRolParaTurno(fecha, "NOCHE", H_NOCHE,
                Map.of("MEDICO", 1, "ENFERMERO", 2, "AUXILIAR", 8, "TERAPIA", 1),
                colaboradores, historialPorColaborador, idxRol, bloqueadosHoyPorPosturno, resultado);

        Set<Long> yaAsignadosHoy = resultado.stream()
                .filter(a -> a.getFecha().equals(fecha))
                .map(a -> a.getColaborador().getIdColaborador())
                .collect(Collectors.toSet());

        for (Colaborador c : colaboradores) {
            if (!yaAsignadosHoy.contains(c.getIdColaborador())) {
                Turno tLibre = getOrCreateTurno(H_LIBRE, fecha);
                AsignacionTurno a = crearAsignacion(c, tLibre, fecha, "Libre");
                asignacionRepository.save(a);
                resultado.add(a);
                historialPorColaborador.get(c.getIdColaborador()).add(cloneLite(a));
            }
        }
    }

    intentarCompletarObjetivoHoras(mes, colaboradores, H_DIA, H_NOCHE, historialPorColaborador, resultado);
    asignarComiteEnPrimerLibre(mes, colaboradores, H_COMITE, resultado);

    try {
        generarArchivoPDF(resultado, mes.toString());
        generarArchivoExcel(resultado, mes.toString());
    } catch (IOException e) {
        log.error("Error al generar archivos de malla para {}", mes, e);
    }

    // ===================== Guardar en MallaTurnos =====================
    for (AsignacionTurno a : resultado) {
    Colaborador colaborador = a.getColaborador();
    Turno turno = a.getTurno();

    MallaTurnos malla = new MallaTurnos();
    malla.setColaborador(colaborador); // <-- CORRECTO
    malla.setTurno(turno);
    malla.setEstado("GENERADA");
    malla.setMesMalla(mes.toString());
    malla.setRol(colaborador.getRol().getRol());

    mallaTurnosRepository.save(malla);
}

return resultado;
}
    
   // ===================== Generar malla para un rol =====================
/**
 * Genera la malla solo para colaboradores cuyo rol coincide con `rol` (por ejemplo "ENFERMERO").
 * Reutiliza la lógica principal pero trabajando únicamente con ese subconjunto de colaboradores.
 */
public List<AsignacionTurno> generarMallaPorRol(YearMonth mes, String rol) {
    // Filtramos colaboradores por rol solicitado (rol puede ser "TODOS" para todos)
    List<Colaborador> todos = listarColaboradores();
    List<Colaborador> colaboradores;
    if (rol == null || rol.isBlank() || "TODOS".equalsIgnoreCase(rol)) {
        colaboradores = new ArrayList<>(todos);
    } else {
        colaboradores = todos.stream()
                .filter(c -> rolEquals(c, rol))
                .collect(Collectors.toList());
    }

    if (colaboradores.isEmpty()) {
        log.warn("No hay colaboradores para el rol: {}", rol);
        return new ArrayList<>(); // Retornamos lista vacía en lugar de romper
    }

    Map<String, Horario> horarioPorTipo = listarHorarios()
            .stream()
            .collect(Collectors.toMap(h -> safeUpper(h.getTipo()), h -> h, (a, b) -> a));

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

        // Asignación de posturno automático
        for (Colaborador c : colaboradores) {
            List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador());
            if (!hist.isEmpty()) {
                AsignacionTurno ult = hist.get(hist.size() - 1);
                if (isTipo(ult.getTurno().getHorario(), "NOCHE")) {
                    Turno tLibre = getOrCreateTurno(H_LIBRE, fecha);
                    AsignacionTurno a = crearAsignacion(c, tLibre, fecha, "Posturno automático");
                    asignacionRepository.save(a);
                    resultado.add(a);
                    hist.add(cloneLite(a));
                    bloqueadosHoyPorPosturno.add(c.getIdColaborador());
                }
            }
        }

        // Definir cupos solo para el rol solicitado si no es TODOS
        Map<String, Integer> cuposPorRol = new HashMap<>();
        if (rol == null || rol.isBlank() || "TODOS".equalsIgnoreCase(rol)) {
            cuposPorRol.put("MEDICO", 2);
            cuposPorRol.put("ENFERMERO", 3);
            cuposPorRol.put("AUXILIAR", 8);
            cuposPorRol.put("TERAPIA", 2);
        } else {
            int cupoDefault = switch (rol.toUpperCase()) {
                case "MEDICO" -> 2;
                case "ENFERMERO" -> 3;
                case "AUXILIAR" -> 8;
                case "TERAPIA" -> 2;
                default -> 0;
            };
            cuposPorRol.put(rol.toUpperCase(), cupoDefault);
        }

        // Asignación de DIA y NOCHE
        asignarCuposPorRolParaTurno(fecha, "DIA", H_DIA,
                cuposPorRol, colaboradores, historialPorColaborador, idxRol, bloqueadosHoyPorPosturno, resultado);

        asignarCuposPorRolParaTurno(fecha, "NOCHE", H_NOCHE,
                cuposPorRol, colaboradores, historialPorColaborador, idxRol, bloqueadosHoyPorPosturno, resultado);

        // Completar con LIBRE para quienes no tienen turno
        Set<Long> yaAsignadosHoy = resultado.stream()
                .filter(a -> a.getFecha().equals(fecha))
                .map(a -> a.getColaborador().getIdColaborador())
                .collect(Collectors.toSet());

        for (Colaborador c : colaboradores) {
            if (!yaAsignadosHoy.contains(c.getIdColaborador())) {
                Turno tLibre = getOrCreateTurno(H_LIBRE, fecha);
                AsignacionTurno a = crearAsignacion(c, tLibre, fecha, "Libre");
                asignacionRepository.save(a);
                resultado.add(a);
                historialPorColaborador.get(c.getIdColaborador()).add(cloneLite(a));
            }
        }
    }

    // Intentar completar objetivo de horas y asignar comités
    intentarCompletarObjetivoHoras(mes, colaboradores, H_DIA, H_NOCHE, historialPorColaborador, resultado);
    asignarComiteEnPrimerLibre(mes, colaboradores, H_COMITE, resultado);

    // Generación de archivos
    try {
        String mesBase = mes.toString(); // siempre "yyyy-MM"
String sufijo = (rol == null || rol.isBlank() ? "TODOS" : rol.toUpperCase());
String nombreArchivo = mesBase + "_" + sufijo;

generarArchivoPDF(resultado, nombreArchivo);
generarArchivoExcel(resultado, nombreArchivo);
    } catch (IOException e) {
        log.error("Error al generar archivos de malla por rol para {} - {}", mes, rol, e);
    }

    return resultado;
}

// ===================== Construir mallas por rol =====================
/**
 * Construye un mapa mallasPorRol: rol -> MallaDTO (dias + filas con turnos).
 * Útil para consumir desde la vista y mostrar una tabla por rol.
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
                .sorted(Comparator.comparing(c -> (c.getUsuario() != null ? (c.getUsuario().getPrimerNombre() + " " + c.getUsuario().getPrimerApellido()) : "")))
                .toList();

        List<MallaDTO.Row> filas = new ArrayList<>();

        for (Colaborador c : cols) {
            MallaDTO.Row fila = new MallaDTO.Row();
            fila.colaborador = (c.getUsuario() != null ? c.getUsuario().getPrimerNombre() + " " + c.getUsuario().getPrimerApellido() : ("Col " + c.getIdColaborador()));
            fila.turnos = new ArrayList<>();

            for (int d = 1; d <= ym.lengthOfMonth(); d++) {
                LocalDate fecha = ym.atDay(d);
                String turnoStr = grupo.stream()
                        .filter(a2 -> a2.getColaborador() != null
                                && Objects.equals(a2.getColaborador().getIdColaborador(), c.getIdColaborador())
                                && Objects.equals(a2.getFecha(), fecha))
                        .map(a2 -> a2.getTurno() != null && a2.getTurno().getHorario() != null ? a2.getTurno().getHorario().getTipo() : "LIBRE")
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

// ===================== Helpers =====================
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
            continue; // Evita romper la malla si no hay candidatos
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
                boolean yaTieneAlgoHoy = hist.stream().anyMatch(a -> a.getFecha().equals(fecha));
                if (!yaTieneAlgoHoy) {
                    Turno t = getOrCreateTurno(horarioTurno, fecha);
                    AsignacionTurno a = crearAsignacion(c, t, fecha, "Cobertura mínima " + tipoTurnoStr);
                    asignacionRepository.save(a);
                    resultado.add(a);
                    hist.add(cloneLite(a));
                    asignados++;
                }
            }
            intentos++;
        }

        if (asignados < cupo)
            log.warn("No se pudo cubrir cupo de {} en {} el {}. Asignados: {} / Requeridos: {}", rol, tipoTurnoStr, fecha, asignados, cupo);

        idxRol.put(rol, start % candidatos.size());
    }
}


    private boolean puedeAsignar(String tipoTurno, List<AsignacionTurno> hist) {
        return puedeAsignar(tipoTurno, hist, null);
    }

    private boolean puedeAsignar(String tipoTurno, List<AsignacionTurno> hist, LocalDate fechaObjetivo) {
        String tipo = safeUpper(tipoTurno);

        if ("NOCHE".equals(tipo) && hist.size() >= 2) {
            String t1 = getTipo(hist.get(hist.size() - 1));
            String t2 = getTipo(hist.get(hist.size() - 2));
            if ("NOCHE".equals(t1) && "NOCHE".equals(t2)) return false;
        }

        if ("DIA".equals(tipo) && !hist.isEmpty()) {
            String ult = getTipo(hist.get(hist.size() - 1));
            if ("NOCHE".equals(ult)) return false;
        }

        if (fechaObjetivo != null) {
            boolean yaTiene = hist.stream().anyMatch(a -> a.getFecha().equals(fechaObjetivo));
            if (yaTiene) return false;
        }

        return true;
    }

    private String getTipo(AsignacionTurno a) {
        return safeUpper(a.getTurno().getHorario().getTipo());
    }

    private boolean isTipo(Horario h, String esperado) {
        return safeUpper(h.getTipo()).equals(safeUpper(esperado));
    }

    private boolean rolEquals(Colaborador c, String rolEsperado) {
        String nombre = c.getRol() != null ? c.getRol().getRol() : null;
        return safeUpper(nombre).equals(safeUpper(rolEsperado));
    }

    private String safeUpper(String s) {
        return s == null ? "" : s.toUpperCase(Locale.ROOT).trim();
    }

    private Turno getOrCreateTurno(Horario horario, LocalDate fecha) {
        Optional<Turno> existente = turnoRepository.findAll().stream()
                .filter(t -> fecha.equals(t.getFechaIni()) && fecha.equals(t.getFechaFin())
                        && t.getHorario() != null
                        && Objects.equals(t.getHorario().getIdHorario(), horario.getIdHorario()))
                .findFirst();

        if (existente.isPresent()) return existente.get();

        Turno t = new Turno();
        t.setFechaIni(fecha);
        t.setFechaFin(fecha);
        t.setHorario(horario);
        return turnoRepository.save(t);
    }

    private AsignacionTurno crearAsignacion(Colaborador c, Turno t, LocalDate fecha, String obs) {
        AsignacionTurno a = new AsignacionTurno();
        a.setId(new AsignacionTurnoPK(t.getIdTurno(), c.getIdColaborador(), fecha));
        a.setColaborador(c);
        a.setTurno(t);
        a.setFecha(fecha);
        a.setObservaciones(obs);
        return a;
    }

    private AsignacionTurno cloneLite(AsignacionTurno a) {
        AsignacionTurno c = new AsignacionTurno();
        c.setColaborador(a.getColaborador());
        c.setTurno(a.getTurno());
        c.setFecha(a.getFecha());
        c.setObservaciones(a.getObservaciones());
        return c;
    }

    private Horario requireHorario(Map<String, Horario> porTipo, String tipo) {
        Horario h = porTipo.get(safeUpper(tipo));
        if (h == null) throw new RuntimeException("No existe HORARIO de tipo: " + tipo + " en la tabla 'horario'.");
        return h;
    }
    
    private YearMonth extractYearMonth(String input) {
    if (input == null) {
        throw new IllegalArgumentException("El parámetro 'input' es null");
    }

    Matcher m;
        m = Pattern.compile("(\\d{4}-\\d{2})").matcher(input);

    if (m.find()) {
        return YearMonth.parse(m.group(1)); // "2025-10"
    }

    throw new IllegalArgumentException("Formato inválido para mes: " + input);
}

    // ===================== Métodos faltantes funcionales =====================
    private void intentarCompletarObjetivoHoras(YearMonth mes, List<Colaborador> colaboradores,
                                                Horario H_DIA, Horario H_NOCHE,
                                                Map<Long, List<AsignacionTurno>> historialPorColaborador,
                                                List<AsignacionTurno> resultado) {
        // Lógica simple: si un colaborador no llega a HORAS_MES, se asignan turnos DIA hasta completarlo
        for (Colaborador c : colaboradores) {
            List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador());
            long turnos12h = hist.stream()
                    .filter(a -> isTipo(a.getTurno().getHorario(), "DIA") || isTipo(a.getTurno().getHorario(), "NOCHE"))
                    .count();
            while (turnos12h * HORAS_TURNO < HORAS_MES) {
                // Buscar primer día libre
                Optional<AsignacionTurno> libre = hist.stream().filter(a -> isTipo(a.getTurno().getHorario(), "LIBRE")).findFirst();
                if (libre.isEmpty()) break;
                LocalDate fecha = libre.get().getFecha();
                Turno t = getOrCreateTurno(H_DIA, fecha);
                AsignacionTurno a = crearAsignacion(c, t, fecha, "Completando objetivo horas");
                asignacionRepository.save(a);
                resultado.add(a);
                hist.add(cloneLite(a));
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
                String turnoStr = asignaciones.stream()
                        .filter(a -> a.getColaborador().getIdColaborador().equals(c.getIdColaborador()) && a.getFecha().equals(fecha))
                        .map(a -> a.getTurno().getHorario().getTipo())
                        .findFirst()
                        .orElse("LIBRE");
                Cell turnoCell = row.createCell(d);
                turnoCell.setCellValue(turnoStr);
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
                String turnoStr = asignaciones.stream()
                        .filter(a -> a.getColaborador() != null
                                && Objects.equals(a.getColaborador().getIdColaborador(), c.getIdColaborador())
                                && Objects.equals(a.getFecha(), fecha))
                        .map(a -> a.getTurno() != null && a.getTurno().getHorario() != null
                                ? a.getTurno().getHorario().getTipo()
                                : "LIBRE")
                        .findFirst()
                        .orElse("LIBRE");

                table.addCell(new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph(turnoStr))
                        .setTextAlignment(TextAlignment.CENTER));
            }
        }

        document.add(table);
    }
}
}