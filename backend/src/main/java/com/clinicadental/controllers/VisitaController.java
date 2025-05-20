package com.clinicadental.controllers;

import com.clinicadental.models.Visita;
import com.clinicadental.repositories.VisitaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/visitas")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class VisitaController {

    private static final Logger logger = LoggerFactory.getLogger(VisitaController.class);

    private final VisitaRepository visitaRepository;

    public VisitaController(VisitaRepository visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Visita>> getAllVisitas() {
        logger.info("Obteniendo todas las visitas");
        return ResponseEntity.ok(visitaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visita> getVisitaById(@PathVariable Integer id) {
        logger.info("Obteniendo visita por ID: {}", id);
        return visitaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Visita> createVisita(@RequestBody Visita visita) {
        logger.info("Creando nueva visita para paciente: {}", 
                    visita.getPaciente() != null ? visita.getPaciente().getDni() : "desconocido");
        return ResponseEntity.ok(visitaRepository.save(visita));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visita> updateVisita(@PathVariable Integer id, @RequestBody Visita visitaDetails) {
        logger.info("Actualizando visita con ID: {}", id);
        return visitaRepository.findById(id)
                .map(visita -> {
                    visita.setPaciente(visitaDetails.getPaciente());
                    visita.setOdontologo(visitaDetails.getOdontologo());
                    visita.setTratamiento(visitaDetails.getTratamiento());
                    visita.setFechaHora(visitaDetails.getFechaHora());
                    visita.setMotivo(visitaDetails.getMotivo());
                    visita.setObservaciones(visitaDetails.getObservaciones());
                    visita.setEstado(visitaDetails.getEstado());
                    return ResponseEntity.ok(visitaRepository.save(visita));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisita(@PathVariable Integer id) {
        logger.info("Eliminando visita con ID: {}", id);
        if (visitaRepository.existsById(id)) {
            visitaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-paciente/{dni}")
    public ResponseEntity<List<Visita>> getVisitasByPaciente(@PathVariable String dni) {
        logger.info("Obteniendo visitas por paciente DNI: {}", dni);
        return ResponseEntity.ok(visitaRepository.findByPacienteDniAndEstado(dni, "PROGRAMADA"));
    }

    @GetMapping("/by-odontologo/{odontologoId}")
    public ResponseEntity<List<Visita>> getVisitasByOdontologo(@PathVariable Integer odontologoId) {
        logger.info("Obteniendo visitas por odontólogo ID: {}", odontologoId);
        return ResponseEntity.ok(visitaRepository.findByOdontologoIdAndEstado(odontologoId, "PROGRAMADA"));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Visita>> getVisitasByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        logger.info("Obteniendo visitas para la fecha: {}", fecha);
        LocalDateTime inicio = fecha.atTime(LocalTime.MIN);
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        
        List<Visita> visitas = visitaRepository.findByFechaHoraBetween(inicio, fin);
        logger.info("Se encontraron {} visitas para la fecha {}", visitas.size(), fecha);
        
        return ResponseEntity.ok(visitas);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<Visita>> getVisitasByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        logger.info("Obteniendo visitas entre {} y {}", inicio, fin);
        return ResponseEntity.ok(visitaRepository.findByFechaHoraBetween(inicio, fin));
    }
}
