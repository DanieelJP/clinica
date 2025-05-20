package com.clinicadental.controllers;

import com.clinicadental.models.Horario;
import com.clinicadental.models.DiaSemana;
import com.clinicadental.services.HorarioService;
import com.clinicadental.dto.HorarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/horarios", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, 
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
             allowCredentials = "true")
public class HorarioController {
    private static final Logger logger = LoggerFactory.getLogger(HorarioController.class);
    private final ObjectMapper objectMapper;
    private final HorarioService horarioService;
    private static final DateTimeFormatter[] TIME_FORMATTERS = {
        DateTimeFormatter.ofPattern("HH:mm"),
        DateTimeFormatter.ofPattern("HH:mm:ss")
    };

    public HorarioController(HorarioService horarioService, ObjectMapper objectMapper) {
        this.horarioService = horarioService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Horario> crearHorario(@RequestBody HorarioDTO horarioDTO) {
        try {
            String horarioJson = objectMapper.writeValueAsString(horarioDTO);
            logger.info("Recibida solicitud para crear horario. Datos recibidos: {}", horarioJson);
            
            Horario horario = new Horario();
            horario.setDia(horarioDTO.getDia());
            
            // Convertir el string a enum DiaSemana
            try {
                DiaSemana diaSemana = DiaSemana.valueOf(horarioDTO.getDiaSemana());
                horario.setDiaSemana(diaSemana);
            } catch (IllegalArgumentException e) {
                logger.error("Error al convertir diaSemana: {}", horarioDTO.getDiaSemana());
                throw new RuntimeException("Día de la semana inválido: " + horarioDTO.getDiaSemana());
            }
            
            // Intentar parsear las horas con diferentes formatos
            try {
                LocalTime horaInicio = null;
                LocalTime horaFin = null;
                
                for (DateTimeFormatter formatter : TIME_FORMATTERS) {
                    try {
                        horaInicio = LocalTime.parse(horarioDTO.getHoraInicio(), formatter);
                        horaFin = LocalTime.parse(horarioDTO.getHoraFin(), formatter);
                        break;
                    } catch (DateTimeParseException e) {
                        continue;
                    }
                }
                
                if (horaInicio == null || horaFin == null) {
                    throw new DateTimeParseException("No se pudo parsear el formato de hora", 
                        horarioDTO.getHoraInicio(), 0);
                }
                
                horario.setHoraInicio(horaInicio);
                horario.setHoraFin(horaFin);
            } catch (DateTimeParseException e) {
                logger.error("Error al parsear horas: inicio={}, fin={}", 
                    horarioDTO.getHoraInicio(), horarioDTO.getHoraFin());
                throw new RuntimeException("Formato de hora inválido. Use HH:mm o HH:mm:ss", e);
            }
            
            horario.setDisponible(horarioDTO.isDisponible());
            
            Horario savedHorario = horarioService.crearHorario(horario, horarioDTO.getOdontologo_id());
            String savedHorarioJson = objectMapper.writeValueAsString(savedHorario);
            logger.info("Horario creado exitosamente: {}", savedHorarioJson);
            
            return ResponseEntity.ok(savedHorario);
        } catch (JsonProcessingException e) {
            logger.error("Error al procesar JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar los datos del horario", e);
        } catch (Exception e) {
            logger.error("Error al crear horario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear el horario: " + e.getMessage(), e);
        }
    }

    @GetMapping("/odontologo/{odontologoId}")
    public ResponseEntity<List<Horario>> obtenerHorariosOdontologo(@PathVariable Integer odontologoId) {
        logger.info("Recibida solicitud para obtener horarios del odontólogo: {}", odontologoId);
        return ResponseEntity.ok(horarioService.obtenerHorariosOdontologo(odontologoId));
    }

    @GetMapping("/verificar-disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @RequestParam Integer odontologoId,
            @RequestParam DiaSemana diaSemana,
            @RequestParam String hora) {
        logger.info("Verificando disponibilidad para odontólogo: {} en día: {} a hora: {}", 
            odontologoId, diaSemana, hora);
        
        LocalTime horaLocal = LocalTime.parse(hora);
        boolean disponible = horarioService.verificarDisponibilidad(odontologoId, diaSemana, horaLocal);
        
        return ResponseEntity.ok(disponible);
    }

    @PutMapping("/{horarioId}/disponibilidad")
    public ResponseEntity<Void> actualizarDisponibilidad(
            @PathVariable Integer horarioId,
            @RequestParam boolean disponible) {
        logger.info("Actualizando disponibilidad del horario: {} a: {}", horarioId, disponible);
        horarioService.actualizarDisponibilidad(horarioId, disponible);
        return ResponseEntity.ok().build();
    }
} 