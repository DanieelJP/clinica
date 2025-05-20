package com.clinicadental.controllers;

import com.clinicadental.models.Visita;
import com.clinicadental.services.VisitaService;
import com.clinicadental.dto.VisitaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/visitas")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, 
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
             allowCredentials = "true")
@Validated
public class VisitaController {
    private static final Logger logger = LoggerFactory.getLogger(VisitaController.class);
    private final VisitaService visitaService;

    public VisitaController(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    @PostMapping
    public ResponseEntity<?> createVisita(@Valid @RequestBody VisitaDTO visitaDTO) {
        try {
            logger.info("Recibida solicitud para crear visita");
            Visita visita = new Visita();
            visita.setFechaHora(visitaDTO.getFechaHora());
            visita.setMotivo(visitaDTO.getMotivo());
            visita.setObservaciones(visitaDTO.getObservaciones());
            
            Visita savedVisita = visitaService.crearVisita(visita, 
                visitaDTO.getOdontologo_id(), 
                visitaDTO.getPaciente_dni(),
                visitaDTO.getTratamiento_id());
                
            return ResponseEntity.ok(savedVisita);
        } catch (RuntimeException e) {
            logger.error("Error al crear visita: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVisita(@PathVariable Integer id, @Valid @RequestBody VisitaDTO visitaDTO) {
        try {
            logger.info("Recibida solicitud para actualizar visita con ID: {}", id);
            Visita visita = new Visita();
            visita.setObservaciones(visitaDTO.getObservaciones());
            
            Visita updatedVisita = visitaService.actualizarVisita(id, visita);
            return ResponseEntity.ok(updatedVisita);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar visita: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/odontologo/{odontologoId}")
    public ResponseEntity<List<Visita>> getVisitasByOdontologo(@PathVariable Integer odontologoId) {
        logger.info("Recibida solicitud para obtener visitas del odontólogo: {}", odontologoId);
        return ResponseEntity.ok(visitaService.obtenerVisitasPorOdontologo(odontologoId));
    }

    @GetMapping("/paciente/{dni}")
    public ResponseEntity<List<Visita>> getVisitasByPaciente(@PathVariable String dni) {
        logger.info("Recibida solicitud para obtener visitas del paciente: {}", dni);
        return ResponseEntity.ok(visitaService.obtenerVisitasPorPaciente(dni));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Visita>> getVisitasByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        logger.info("Recibida solicitud para obtener visitas de la fecha: {}", fecha);
        return ResponseEntity.ok(visitaService.obtenerVisitasPorFecha(fecha.atStartOfDay()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVisitaById(@PathVariable Integer id) {
        try {
            logger.info("Recibida solicitud para obtener visita con ID: {}", id);
            return ResponseEntity.ok(visitaService.obtenerVisitaPorId(id));
        } catch (RuntimeException e) {
            logger.error("Error al obtener visita: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
