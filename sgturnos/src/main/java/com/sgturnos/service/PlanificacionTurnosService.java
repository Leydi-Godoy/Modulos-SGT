package com.sgturnos.service;

import com.sgturnos.model.*;
import com.sgturnos.repository.AsignacionTurnoRepository;
import com.sgturnos.repository.HorarioRepository;
import com.sgturnos.repository.TurnoRepository;
import com.sgturnos.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanificacionTurnosService {

    @Autowired
    private ColaboradorRepository colaboradorRepository; // Cambiado de empleadoRepository

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private AsignacionTurnoRepository asignacionRepository;

    private static final int HORAS_MES = 192;   // objetivo mensual por colaborador
    private static final int HORAS_TURNO = 12;  // duración de turnos DIA/NOCHE

    // ===================== Listados básicos =====================

    public List<Colaborador> listarColaboradores() { return colaboradorRepository.findAll(); } // Cambiado

    public List<Horario> listarHorarios() { return horarioRepository.findAll(); }

    public List<AsignacionTurno> listarAsignaciones() { return asignacionRepository.findAll(); }

    // ===================== CRUD simple de asignaciones =====================

    public void guardarAsignacion(Long idColaborador, Long idTurno, LocalDate fecha, String observaciones) { // Cambiado
        Colaborador colaborador = colaboradorRepository.findById(idColaborador) // Cambiado
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado: " + idColaborador)); // Cambiado

        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado: " + idTurno));

        AsignacionTurno asignacion = new AsignacionTurno();
        asignacion.setId(new AsignacionTurnoPK(idTurno, idColaborador, fecha)); // Cambiado
        asignacion.setColaborador(colaborador); // Cambiado
        asignacion.setTurno(turno);
        asignacion.setFecha(fecha);
        asignacion.setObservaciones(observaciones);

        asignacionRepository.save(asignacion);
    }

    public AsignacionTurno obtenerPorId(Long idTurno, Long idColaborador, LocalDate fecha) { // Cambiado
        return asignacionRepository.findById(new AsignacionTurnoPK(idTurno, idColaborador, fecha)) // Cambiado
                .orElseThrow(() -> new RuntimeException(
                        "Asignación no encontrada (turno=" + idTurno + ", colaborador=" + idColaborador + ", fecha=" + fecha + ")")); // Cambiado
    }

    public void editarAsignacion(Long idTurno, Long idColaborador, LocalDate fecha, String observaciones) { // Cambiado
        AsignacionTurno a = obtenerPorId(idTurno, idColaborador, fecha); // Cambiado
        a.setFecha(fecha);
        a.setObservaciones(observaciones);
        asignacionRepository.save(a);
    }

    public void eliminarAsignacion(Long idTurno, Long idColaborador, LocalDate fecha) { // Cambiado
        asignacionRepository.deleteById(new AsignacionTurnoPK(idTurno, idColaborador, fecha)); // Cambiado
    }

    // ===================== Generación de malla mensual =====================

    public List<AsignacionTurno> generarMalla(YearMonth mes) {
        // Validar colaboradores
        List<Colaborador> colaboradores = listarColaboradores(); // Cambiado
        if (colaboradores.isEmpty()) throw new RuntimeException("No hay colaboradores para generar la malla."); // Cambiado

        // Cargar horarios por tipo (DIA/NOCHE/LIBRE/COMITE)
        Map<String, Horario> horarioPorTipo = listarHorarios()
                .stream()
                .collect(Collectors.toMap(
                        h -> safeUpper(h.getTipo()),
                        h -> h,
                        (a, b) -> a
                ));
        Horario H_DIA    = requireHorario(horarioPorTipo, "DIA");
        Horario H_NOCHE  = requireHorario(horarioPorTipo, "NOCHE");
        Horario H_LIBRE  = requireHorario(horarioPorTipo, "LIBRE");
        Horario H_COMITE = requireHorario(horarioPorTipo, "COMITE");

        // Indices de fairness por rol (round-robin)
        Map<String, Integer> idxRol = new HashMap<>();

        // Historial en memoria del mes para validar reglas por colaborador
        Map<Long, List<AsignacionTurno>> historialPorColaborador = new HashMap<>(); // Cambiado
        colaboradores.forEach(c -> historialPorColaborador.put(c.getIdColaborador(), new ArrayList<>())); // Cambiado

        // Resultado total
        List<AsignacionTurno> resultado = new ArrayList<>();

        // ====== Generación día por día ======
        for (int d = 1; d <= mes.lengthOfMonth(); d++) {
            LocalDate fecha = mes.atDay(d);

            // 0) Aplicar posturno
            Set<Long> bloqueadosHoyPorPosturno = new HashSet<>(); // Cambiado
            for (Colaborador c : colaboradores) { // Cambiado
                List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador()); // Cambiado
                if (!hist.isEmpty()) {
                    AsignacionTurno ult = hist.get(hist.size() - 1);
                    if (isTipo(ult.getTurno().getHorario(), "NOCHE")) {
                        Turno tLibre = getOrCreateTurno(H_LIBRE, fecha);
                        AsignacionTurno a = crearAsignacion(c, tLibre, fecha, "Posturno automático"); // Cambiado
                        asignacionRepository.save(a);
                        resultado.add(a);
                        hist.add(cloneLite(a));
                        bloqueadosHoyPorPosturno.add(c.getIdColaborador()); // Cambiado
                    }
                }
            }

            // 1) Dotación mínima del DÍA
            asignarCuposPorRolParaTurno(fecha, "DIA", H_DIA,
                    Map.of("MEDICO", 2, "ENFERMERO", 3, "AUXILIAR", 8, "TERAPIA", 2),
                    colaboradores, historialPorColaborador, idxRol, bloqueadosHoyPorPosturno, resultado); // Cambiado

            // 2) Dotación mínima de la NOCHE
            asignarCuposPorRolParaTurno(fecha, "NOCHE", H_NOCHE,
                    Map.of("MEDICO", 1, "ENFERMERO", 2, "AUXILIAR", 8, "TERAPIA", 1),
                    colaboradores, historialPorColaborador, idxRol, bloqueadosHoyPorPosturno, resultado); // Cambiado

            // 3) Relleno con LIBRE
            Set<Long> yaAsignadosHoy = resultado.stream() // Cambiado
                    .filter(a -> a.getFecha().equals(fecha))
                    .map(a -> a.getColaborador().getIdColaborador()) // Cambiado
                    .collect(Collectors.toSet());

            for (Colaborador c : colaboradores) { // Cambiado
                if (!yaAsignadosHoy.contains(c.getIdColaborador())) { // Cambiado
                    Turno tLibre = getOrCreateTurno(H_LIBRE, fecha);
                    AsignacionTurno a = crearAsignacion(c, tLibre, fecha, "Libre"); // Cambiado
                    asignacionRepository.save(a);
                    resultado.add(a);
                    historialPorColaborador.get(c.getIdColaborador()).add(cloneLite(a)); // Cambiado
                }
            }
        }

        // Top-up
        intentarCompletarObjetivoHoras(mes, colaboradores, H_DIA, H_NOCHE, historialPorColaborador, resultado); // Cambiado

        // COMITE
        asignarComiteEnPrimerLibre(mes, colaboradores, H_COMITE, resultado); // Cambiado

        return resultado;
    }

    // ===================== Helpers de asignación =====================

    private void asignarCuposPorRolParaTurno(LocalDate fecha,
                                             String tipoTurnoStr,
                                             Horario horarioTurno,
                                             Map<String, Integer> cuposMinimos,
                                             List<Colaborador> colaboradores, // Cambiado
                                             Map<Long, List<AsignacionTurno>> historialPorColaborador, // Cambiado
                                             Map<String, Integer> idxRol,
                                             Set<Long> bloqueadosHoy, // Cambiado
                                             List<AsignacionTurno> resultado) {

        for (Map.Entry<String, Integer> entry : cuposMinimos.entrySet()) {
            String rol = entry.getKey();
            int cupo = entry.getValue();

            List<Colaborador> candidatos = colaboradores.stream() // Cambiado
                    .filter(c -> rolEquals(c, rol)) // Cambiado
                    .collect(Collectors.toList());

            if (candidatos.isEmpty()) {
                throw new RuntimeException("No hay colaboradores con rol " + rol + " para cubrir " + tipoTurnoStr + " el " + fecha); // Cambiado
            }

            int start = idxRol.getOrDefault(rol, 0);
            int asignados = 0;
            int intentos = 0;

            while (asignados < cupo && intentos < candidatos.size() * 2) {
                Colaborador c = candidatos.get(start % candidatos.size()); // Cambiado
                start++;

                if (bloqueadosHoy.contains(c.getIdColaborador())) { // Cambiado
                    intentos++;
                    continue;
                }

                List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador()); // Cambiado

                if (puedeAsignar(tipoTurnoStr, hist)) {
                    boolean yaTieneAlgoHoy = hist.stream().anyMatch(a -> a.getFecha().equals(fecha));
                    if (!yaTieneAlgoHoy) {
                        int turnos12h = (int) hist.stream()
                                .filter(a -> isTipo(a.getTurno().getHorario(), "DIA") || isTipo(a.getTurno().getHorario(), "NOCHE"))
                                .count();
                        if (turnos12h < HORAS_MES / HORAS_TURNO || cupo > 0) {
                            Turno t = getOrCreateTurno(horarioTurno, fecha);
                            AsignacionTurno a = crearAsignacion(c, t, fecha, "Cobertura mínima " + tipoTurnoStr); // Cambiado
                            asignacionRepository.save(a);
                            resultado.add(a);
                            hist.add(cloneLite(a));
                            asignados++;
                        }
                    }
                }

                intentos++;
            }

            if (asignados < cupo) {
                throw new RuntimeException("No se pudo cubrir cupo de " + rol + " en " + tipoTurnoStr + " el " + fecha +
                        ". Asignados: " + asignados + " / Requeridos: " + cupo);
            }

            idxRol.put(rol, start % candidatos.size());
        }
    }

    private void intentarCompletarObjetivoHoras(YearMonth mes,
                                                List<Colaborador> colaboradores, // Cambiado
                                                Horario H_DIA,
                                                Horario H_NOCHE,
                                                Map<Long, List<AsignacionTurno>> historialPorColaborador, // Cambiado
                                                List<AsignacionTurno> resultado) {
        for (Colaborador c : colaboradores) { // Cambiado
            List<AsignacionTurno> hist = historialPorColaborador.get(c.getIdColaborador()); // Cambiado
            int actuales = (int) hist.stream()
                    .filter(a -> isTipo(a.getTurno().getHorario(), "DIA") || isTipo(a.getTurno().getHorario(), "NOCHE"))
                    .count();

            while (actuales < HORAS_MES / HORAS_TURNO) {
                boolean asignado = false;

                for (int d = 1; d <= mes.lengthOfMonth() && !asignado; d++) {
                    LocalDate fecha = mes.atDay(d);
                    Optional<AsignacionTurno> asignHoy = hist.stream().filter(a -> a.getFecha().equals(fecha)).findFirst();
                    if (asignHoy.isEmpty()) continue;

                    AsignacionTurno aHoy = asignHoy.get();
                    if (!isTipo(aHoy.getTurno().getHorario(), "LIBRE")) continue;

                    if (puedeAsignar("DIA", hist, fecha)) {
                        Turno tDia = getOrCreateTurno(H_DIA, fecha);
                        reemplazarAsignacion(c, aHoy, tDia, "Top-up a 192h (DIA)", resultado, hist); // Cambiado
                        actuales++;
                        asignado = true;
                        break;
                    }

                    if (puedeAsignar("NOCHE", hist, fecha)) {
                        Turno tNoche = getOrCreateTurno(H_NOCHE, fecha);
                        reemplazarAsignacion(c, aHoy, tNoche, "Top-up a 192h (NOCHE)", resultado, hist); // Cambiado
                        actuales++;
                        asignado = true;
                        break;
                    }
                }

                if (!asignado) break;
            }
        }
    }

    private void asignarComiteEnPrimerLibre(YearMonth mes,
                                            List<Colaborador> colaboradores, // Cambiado
                                            Horario H_COMITE,
                                            List<AsignacionTurno> resultado) {
        for (Colaborador c : colaboradores) { // Cambiado
            Optional<AsignacionTurno> libre = resultado.stream()
                    .filter(a -> a.getColaborador().getIdColaborador().equals(c.getIdColaborador())) // Cambiado
                    .filter(a -> a.getFecha().getYear() == mes.getYear() && a.getFecha().getMonth() == mes.getMonth())
                    .filter(a -> isTipo(a.getTurno().getHorario(), "LIBRE"))
                    .sorted(Comparator.comparing(AsignacionTurno::getFecha))
                    .findFirst();

            if (libre.isPresent()) {
                AsignacionTurno aLibre = libre.get();
                LocalDate fecha = aLibre.getFecha();
                Turno tComite = getOrCreateTurno(H_COMITE, fecha);

                asignacionRepository.delete(aLibre);
                resultado.remove(aLibre);

                AsignacionTurno aComite = crearAsignacion(c, tComite, fecha, "Capacitación (COMITE 3h)"); // Cambiado
                asignacionRepository.save(aComite);
                resultado.add(aComite);
            }
        }
    }

    // ===================== Reglas =====================

    private boolean puedeAsignar(String tipoTurno, List<AsignacionTurno> hist) {
        return puedeAsignar(tipoTurno, hist, null);
    }

    private boolean puedeAsignar(String tipoTurno, List<AsignacionTurno> hist, LocalDate fechaObjetivo) {
        String tipo = safeUpper(tipoTurno);

        if ("NOCHE".equals(tipo)) {
            int size = hist.size();
            if (size >= 2) {
                String t1 = getTipo(hist.get(size - 1));
                String t2 = getTipo(hist.get(size - 2));
                if ("NOCHE".equals(t1) && "NOCHE".equals(t2)) return false;
            }
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

    private boolean rolEquals(Colaborador c, String rolEsperado) { // Cambiado
        String nombre = c.getRol() != null ? c.getRol().getRol() : null;
        return safeUpper(nombre).equals(safeUpper(rolEsperado));
    }

    private String safeUpper(String s) {
        return s == null ? "" : s.toUpperCase(Locale.ROOT).trim();
    }

    // ===================== Persistencia Turno y Asignación =====================

    private Turno getOrCreateTurno(Horario horario, LocalDate fecha) {
        Optional<Turno> existente = turnoRepository
                .findAll()
                .stream()
                .filter(t -> fecha.equals(t.getFechaIni())
                        && fecha.equals(t.getFechaFin())
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

    private AsignacionTurno crearAsignacion(Colaborador c, Turno t, LocalDate fecha, String obs) { // Cambiado
        AsignacionTurno a = new AsignacionTurno();
        a.setId(new AsignacionTurnoPK(t.getIdTurno(), c.getIdColaborador(), fecha)); // Cambiado
        a.setColaborador(c); // Cambiado
        a.setTurno(t);
        a.setFecha(fecha);
        a.setObservaciones(obs);
        return a;
    }

    private void reemplazarAsignacion(Colaborador c, // Cambiado
                                      AsignacionTurno anterior,
                                      Turno nuevoTurno,
                                      String obs,
                                      List<AsignacionTurno> resultado,
                                      List<AsignacionTurno> histColaborador) { // Cambiado
        asignacionRepository.delete(anterior);
        resultado.remove(anterior);
        histColaborador.removeIf(x -> x.getFecha().equals(anterior.getFecha()));

        AsignacionTurno nueva = crearAsignacion(c, nuevoTurno, anterior.getFecha(), obs); // Cambiado
        asignacionRepository.save(nueva);
        resultado.add(nueva);
        histColaborador.add(cloneLite(nueva));
    }

    private AsignacionTurno cloneLite(AsignacionTurno a) {
        AsignacionTurno c = new AsignacionTurno();
        c.setColaborador(a.getColaborador()); // Cambiado
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
}