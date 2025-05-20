package com.clinicadental.services;

import com.clinicadental.models.*;
import com.clinicadental.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class VisitaService {
    private static final Logger logger = LoggerFactory.getLogger(VisitaService.class);
    
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

    @Transactional
    public Visita crearVisita(Visita visita, Integer odontologoId, String pacienteDni, Integer tratamientoId) {
        logger.info("Iniciando creación de visita para paciente: {}", pacienteDni);

        // Validar que el odontólogo existe
        Odontologo odontologo = odontologoRepository.findById(odontologoId)
            .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));
        visita.setOdontologo(odontologo);

        // Validar que el paciente existe
        Paciente paciente = pacienteRepository.findByDni(pacienteDni)
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        visita.setPaciente(paciente);

        // Validar que el horario está dentro del horario de trabajo del odontólogo
        LocalDateTime fechaHora = visita.getFechaHora();
        DiaSemana diaSemana = DiaSemana.valueOf(fechaHora.getDayOfWeek().toString());
        LocalTime hora = fechaHora.toLocalTime();

        boolean horarioValido = horarioRepository
            .findByOdontologoIdAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                odontologoId, diaSemana, hora, hora)
            .stream()
            .anyMatch(Horario::isDisponible);

        if (!horarioValido) {
            throw new RuntimeException("El odontólogo no trabaja en el horario seleccionado");
        }

        // Validar que no hay otra visita programada en el mismo horario
        boolean horarioOcupado = visitaRepository
            .findByFechaHoraBetween(
                fechaHora.minusMinutes(30),
                fechaHora.plusMinutes(30))
            .stream()
            .anyMatch(v -> v.getOdontologo().getId().equals(odontologoId));

        if (horarioOcupado) {
            throw new RuntimeException("El odontólogo ya tiene una visita programada en ese horario");
        }

        // Validar que el paciente es menor de edad y tiene un responsable asignado
        if (paciente.getFechaNacimiento().plusYears(18).isAfter(LocalDateTime.now().toLocalDate())) {
            if (paciente.getResponsables().isEmpty()) {
                throw new RuntimeException("El paciente es menor de edad y requiere un responsable asignado");
            }
        }

        // Si hay un tratamiento, validar que existe y asignarlo
        if (tratamientoId != null) {
            Tratamiento tratamiento = tratamientoRepository.findById(tratamientoId)
                .orElseThrow(() -> new RuntimeException("Tratamiento no encontrado"));
            visita.setTratamiento(tratamiento);
        }

        visita.setEstado("PROGRAMADA");
        Visita savedVisita = visitaRepository.save(visita);
        logger.info("Visita creada exitosamente con ID: {}", savedVisita.getId());
        
        return savedVisita;
    }

    @Transactional
    public Visita actualizarVisita(Integer id, Visita visitaDetails) {
        logger.info("Actualizando visita con ID: {}", id);
        
        return visitaRepository.findById(id)
            .map(visita -> {
                // Solo permitir actualizar ciertos campos
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
    }

    public List<Visita> obtenerVisitasPorOdontologo(Integer odontologoId) {
        logger.info("Obteniendo visitas para odontólogo: {}", odontologoId);
        return visitaRepository.findByOdontologoIdAndEstado(odontologoId, "PROGRAMADA");
    }

    public List<Visita> obtenerVisitasPorPaciente(String dni) {
        logger.info("Obteniendo visitas para paciente: {}", dni);
        return visitaRepository.findByPacienteDniAndEstado(dni, "PROGRAMADA");
    }

    public List<Visita> obtenerVisitasPorFecha(LocalDateTime fecha) {
        logger.info("Obteniendo visitas para fecha: {}", fecha);
        return visitaRepository.findByFechaHoraBetween(
            fecha.with(LocalTime.MIN),
            fecha.with(LocalTime.MAX)
        );
    }

    public Visita obtenerVisitaPorId(Integer id) {
        logger.info("Obteniendo visita con ID: {}", id);
        return visitaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Visita no encontrada"));
    }
} 