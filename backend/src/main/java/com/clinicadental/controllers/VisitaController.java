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
    public ResponseEntity<?> updateVisita(@PathVariable Integer id, @RequestBody VisitaDTO visitaDTO) {
        try {
            logger.info("Recibida solicitud para actualizar visita con ID: {}", id);
            logger.info("Datos recibidos: {}", visitaDTO);
            
            Visita visita = new Visita();
            
            // Actualizar todos los campos que pueden ser modificados
            if (visitaDTO.getFechaHora() != null) {
                visita.setFechaHora(visitaDTO.getFechaHora());
            }
            if (visitaDTO.getMotivo() != null) {
                visita.setMotivo(visitaDTO.getMotivo());
            }
            if (visitaDTO.getObservaciones() != null) {
                visita.setObservaciones(visitaDTO.getObservaciones());
            }
            if (visitaDTO.getTratamiento_id() != null) {
                visita.setTratamiento(visitaService.obtenerVisitaPorId(id).getTratamiento());
            }
            if (visitaDTO.getEstado() != null) {
                visita.setEstado(visitaDTO.getEstado());
            }
            
            Visita updatedVisita = visitaService.actualizarVisita(id, visita);
            return ResponseEntity.ok(updatedVisita);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar visita: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar la visita: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstadoVisita(
            @PathVariable Integer id,
            @RequestParam Visita.EstadoVisita estado) {
        try {
            logger.info("Actualizando estado de visita {} a {}", id, estado);
            Visita visita = new Visita();
            visita.setEstado(estado);
            Visita updatedVisita = visitaService.actualizarVisita(id, visita);
            return ResponseEntity.ok(updatedVisita);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar estado de visita: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/odontologo/{odontologoId}")
    public ResponseEntity<?> getVisitasByOdontologo(@PathVariable Integer odontologoId) {
        try {
            logger.info("Recibida solicitud para obtener visitas del odontólogo: {}", odontologoId);
            List<Visita> visitas = visitaService.obtenerVisitasPorOdontologo(odontologoId);
            return ResponseEntity.ok(visitas);
        } catch (RuntimeException e) {
            logger.error("Error al obtener visitas del odontólogo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/paciente/{dni}")
    public ResponseEntity<?> getVisitasByPaciente(@PathVariable String dni) {
        try {
            logger.info("Recibida solicitud para obtener visitas del paciente: {}", dni);
            List<Visita> visitas = visitaService.obtenerVisitasPorPaciente(dni);
            return ResponseEntity.ok(visitas);
        } catch (RuntimeException e) {
            logger.error("Error al obtener visitas del paciente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<?> getVisitasByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            logger.info("Recibida solicitud para obtener visitas de la fecha: {}", fecha);
            List<Visita> visitas = visitaService.obtenerVisitasPorFecha(fecha.atStartOfDay());
            return ResponseEntity.ok(visitas);
        } catch (RuntimeException e) {
            logger.error("Error al obtener visitas por fecha: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVisitaById(@PathVariable Integer id) {
        try {
            logger.info("Recibida solicitud para obtener visita con ID: {}", id);
            Visita visita = visitaService.obtenerVisitaPorId(id);
            return ResponseEntity.ok(visita);
        } catch (RuntimeException e) {
            logger.error("Error al obtener visita: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVisita(@PathVariable Integer id) {
        try {
            logger.info("Recibida solicitud para eliminar visita con ID: {}", id);
            visitaService.eliminarVisita(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar visita: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
