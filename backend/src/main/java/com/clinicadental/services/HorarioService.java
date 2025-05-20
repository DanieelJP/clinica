package com.clinicadental.services;

import com.clinicadental.models.Horario;
import com.clinicadental.models.DiaSemana;
import com.clinicadental.models.Odontologo;
import com.clinicadental.repositories.HorarioRepository;
import com.clinicadental.repositories.OdontologoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalTime;
import java.util.List;

@Service
public class HorarioService {
    private static final Logger logger = LoggerFactory.getLogger(HorarioService.class);
    private final HorarioRepository horarioRepository;
    private final OdontologoRepository odontologoRepository;
    private final ObjectMapper objectMapper;

    public HorarioService(HorarioRepository horarioRepository, 
                         OdontologoRepository odontologoRepository,
                         ObjectMapper objectMapper) {
        this.horarioRepository = horarioRepository;
        this.odontologoRepository = odontologoRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Horario crearHorario(Horario horario, Integer odontologoId) {
        try {
            logger.info("Iniciando creación de horario para odontólogo: {}", odontologoId);
            
            // Buscar el odontólogo
            Odontologo odontologo = odontologoRepository.findById(odontologoId)
                .orElseThrow(() -> {
                    logger.error("Odontólogo no encontrado con ID: {}", odontologoId);
                    return new RuntimeException("Odontólogo no encontrado");
                });
            
            logger.info("Odontólogo encontrado: {}", odontologo.getUsername());
            
            // Validar el horario
            if (horario.getHoraInicio() == null || horario.getHoraFin() == null) {
                logger.error("Horas inválidas: inicio={}, fin={}", 
                    horario.getHoraInicio(), horario.getHoraFin());
                throw new RuntimeException("Las horas de inicio y fin son requeridas");
            }
            
            if (horario.getHoraInicio().isAfter(horario.getHoraFin())) {
                logger.error("Hora de inicio posterior a hora de fin: inicio={}, fin={}", 
                    horario.getHoraInicio(), horario.getHoraFin());
                throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
            }
            
            // Asignar el odontólogo al horario
            horario.setOdontologo(odontologo);
            
            // Guardar el horario
            Horario savedHorario = horarioRepository.save(horario);
            
            String savedHorarioJson = objectMapper.writeValueAsString(savedHorario);
            logger.info("Horario creado exitosamente: {}", savedHorarioJson);
            
            return savedHorario;
        } catch (JsonProcessingException e) {
            logger.error("Error al procesar JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar los datos del horario", e);
        } catch (Exception e) {
            logger.error("Error al crear horario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear el horario: " + e.getMessage(), e);
        }
    }

    public List<Horario> obtenerHorariosOdontologo(Integer odontologoId) {
        logger.info("Obteniendo horarios para odontólogo: {}", odontologoId);
        return horarioRepository.findByOdontologoId(odontologoId);
    }

    public boolean verificarDisponibilidad(Integer odontologoId, DiaSemana diaSemana, LocalTime hora) {
        logger.info("Verificando disponibilidad para odontólogo: {} en día: {} a hora: {}", 
            odontologoId, diaSemana, hora);
        return horarioRepository.findByOdontologoIdAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
            odontologoId, diaSemana, hora, hora)
            .stream()
            .anyMatch(Horario::isDisponible);
    }

    @Transactional
    public void actualizarDisponibilidad(Integer horarioId, boolean disponible) {
        logger.info("Actualizando disponibilidad del horario: {} a: {}", horarioId, disponible);
        Horario horario = horarioRepository.findById(horarioId)
            .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setDisponible(disponible);
        horarioRepository.save(horario);
    }
} 