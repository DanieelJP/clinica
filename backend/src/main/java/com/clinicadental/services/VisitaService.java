package com.clinicadental.services;

import com.clinicadental.models.*;
import com.clinicadental.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VisitaService {
    private static final Logger logger = LoggerFactory.getLogger(VisitaService.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    private final VisitaRepository visitaRepository;
    private final HorarioRepository horarioRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final TratamientoRepository tratamientoRepository;

    public VisitaService(
            VisitaRepository visitaRepository,
            HorarioRepository horarioRepository,
            PacienteRepository pacienteRepository,
            OdontologoRepository odontologoRepository,
            TratamientoRepository tratamientoRepository) {
        this.visitaRepository = visitaRepository;
        this.horarioRepository = horarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.tratamientoRepository = tratamientoRepository;
    }

    private DiaSemana convertirDiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiaSemana.LUNES;
            case TUESDAY -> DiaSemana.MARTES;
            case WEDNESDAY -> DiaSemana.MIERCOLES;
            case THURSDAY -> DiaSemana.JUEVES;
            case FRIDAY -> DiaSemana.VIERNES;
            case SATURDAY -> DiaSemana.SABADO;
            case SUNDAY -> DiaSemana.DOMINGO;
        };
    }

    @Transactional
    public Visita crearVisita(Visita visita, Integer odontologoId, String pacienteDni, Integer tratamientoId) {
        logger.info("Creando nueva visita para paciente {} con odontólogo {}", pacienteDni, odontologoId);

        try {
            // Validar que el odontólogo existe
            Odontologo odontologo = odontologoRepository.findById(odontologoId)
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));
            visita.setOdontologo(odontologo);

            // Validar que el paciente existe
            List<Paciente> pacientes = pacienteRepository.findByDni(pacienteDni);
            if (pacientes.isEmpty()) {
                throw new RuntimeException("Paciente no encontrado");
            }
            Paciente paciente = pacientes.get(0);
            visita.setPaciente(paciente);

            // Validar que el horario está dentro del horario de trabajo del odontólogo
            LocalDateTime fechaHora = visita.getFechaHora();
            logger.warn("DEBUG - Fecha y hora recibida en crearVisita: {}", fechaHora);
            DiaSemana diaSemana = convertirDiaSemana(fechaHora.getDayOfWeek());
            LocalTime hora = fechaHora.toLocalTime();
            logger.warn("DEBUG - Hora local extraída en crearVisita: {}", hora);

            // Obtener todos los horarios del odontólogo para el día seleccionado
            List<Horario> horarios = horarioRepository
                .findByOdontologoIdAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                    odontologoId, diaSemana, hora, hora);

            if (horarios.isEmpty()) {
                String mensaje = String.format(
                    "Validación de horario: El odontólogo no tiene horario programado para el %s a las %s (Hora validada: %s)",
                    diaSemana,
                    hora.format(TIME_FORMATTER),
                    hora.format(TIME_FORMATTER)
                );
                logger.error(mensaje);
                throw new RuntimeException(mensaje);
            }

            boolean horarioValido = horarios.stream()
                .anyMatch(Horario::isDisponible);

            if (!horarioValido) {
                String mensaje = String.format(
                    "Validación de horario: El odontólogo no está disponible el %s a las %s (Hora validada: %s)",
                    diaSemana,
                    hora.format(TIME_FORMATTER),
                    hora.format(TIME_FORMATTER)
                );
                logger.error(mensaje);
                throw new RuntimeException(mensaje);
            }

            // Validar que no hay otra visita programada en el mismo horario
            List<Visita> visitasExistentes = visitaRepository
                .findByFechaHoraBetween(
                    fechaHora.minusMinutes(30),
                    fechaHora.plusMinutes(30));

            boolean horarioOcupado = visitasExistentes.stream()
                .anyMatch(v -> v.getOdontologo().getId().equals(odontologoId));

            if (horarioOcupado) {
                String mensaje = String.format(
                    "Validación de horario: El odontólogo ya tiene una visita programada el %s a las %s (Hora validada: %s)",
                    diaSemana,
                    hora.format(TIME_FORMATTER),
                    hora.format(TIME_FORMATTER)
                );
                logger.warn(mensaje);
                throw new RuntimeException(mensaje);
            }

            // Si hay un tratamiento, validar que existe y asignarlo
            if (tratamientoId != null) {
                Tratamiento tratamiento = tratamientoRepository.findById(tratamientoId)
                    .orElseThrow(() -> new RuntimeException("Tratamiento no encontrado"));
                visita.setTratamiento(tratamiento);
            }

            visita.setEstado(Visita.EstadoVisita.PROGRAMADA);
            Visita savedVisita = visitaRepository.save(visita);
            logger.info("Visita creada exitosamente con ID: {}", savedVisita.getId());
            
            return savedVisita;
        } catch (Exception e) {
            logger.error("Error al crear visita: {}", e.getMessage());
            throw new RuntimeException("Error al crear la visita: " + e.getMessage());
        }
    }

    @Transactional
    public Visita actualizarVisita(Integer id, Visita visitaDetails) {
        logger.info("Actualizando visita con ID: {}", id);
        
        try {
            return visitaRepository.findById(id)
                .map(visita -> {
                    // Actualizar todos los campos que pueden ser modificados
                    if (visitaDetails.getFechaHora() != null) {
                        visita.setFechaHora(visitaDetails.getFechaHora());
                    }
                    if (visitaDetails.getMotivo() != null) {
                        visita.setMotivo(visitaDetails.getMotivo());
                    }
                    if (visitaDetails.getObservaciones() != null) {
                        visita.setObservaciones(visitaDetails.getObservaciones());
                    }
                    if (visitaDetails.getTratamiento() != null) {
                        visita.setTratamiento(visitaDetails.getTratamiento());
                    }
                    if (visitaDetails.getEstado() != null) {
                        visita.setEstado(visitaDetails.getEstado());
                    }
                    
                    return visitaRepository.save(visita);
                })
                .orElseThrow(() -> new RuntimeException("Visita no encontrada"));
        } catch (Exception e) {
            logger.error("Error al actualizar visita: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar la visita: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Visita> obtenerVisitasPorOdontologo(Integer odontologoId) {
        logger.info("Obteniendo visitas para odontólogo: {}", odontologoId);
        try {
            List<Visita> visitas = visitaRepository.findByOdontologoIdAndEstado(odontologoId, Visita.EstadoVisita.PROGRAMADA);
            return visitas;
        } catch (Exception e) {
            logger.error("Error al obtener visitas del odontólogo: {}", e.getMessage());
            throw new RuntimeException("Error al obtener las visitas del odontólogo: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Visita> obtenerVisitasPorPaciente(String dni) {
        logger.info("Obteniendo visitas para paciente: {}", dni);
        try {
            List<Visita> visitas = visitaRepository.findByPacienteDniAndEstado(dni, Visita.EstadoVisita.PROGRAMADA);
            return visitas;
        } catch (Exception e) {
            logger.error("Error al obtener visitas del paciente: {}", e.getMessage());
            throw new RuntimeException("Error al obtener las visitas del paciente: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Visita> obtenerVisitasPorFecha(LocalDateTime fecha) {
        logger.info("Obteniendo visitas para fecha: {}", fecha);
        try {
            List<Visita> visitas = visitaRepository.findByFechaHoraBetween(
                fecha.with(LocalTime.MIN),
                fecha.with(LocalTime.MAX)
            );
            return visitas;
        } catch (Exception e) {
            logger.error("Error al obtener visitas por fecha: {}", e.getMessage());
            throw new RuntimeException("Error al obtener las visitas por fecha: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Visita obtenerVisitaPorId(Integer id) {
        logger.info("Obteniendo visita con ID: {}", id);
        try {
            return visitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visita no encontrada"));
        } catch (Exception e) {
            logger.error("Error al obtener visita por ID: {}", e.getMessage());
            throw new RuntimeException("Error al obtener la visita: " + e.getMessage());
        }
    }

    @Transactional
    public void eliminarVisita(Integer id) {
        logger.info("Eliminando visita con ID: {}", id);
        try {
            if (!visitaRepository.existsById(id)) {
                throw new RuntimeException("Visita no encontrada");
            }
            visitaRepository.deleteById(id);
            logger.info("Visita eliminada exitosamente");
        } catch (Exception e) {
            logger.error("Error al eliminar visita: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar la visita: " + e.getMessage());
        }
    }
} 