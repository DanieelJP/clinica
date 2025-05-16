package com.clinicadental.controllers;

import com.clinicadental.models.Tratamiento;
import com.clinicadental.repositories.TratamientoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoController {

    private final TratamientoRepository tratamientoRepository;

    public TratamientoController(TratamientoRepository tratamientoRepository) {
        this.tratamientoRepository = tratamientoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Tratamiento>> getAllTratamientos() {
        return ResponseEntity.ok(tratamientoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tratamiento> getTratamientoById(@PathVariable Integer id) {
        return tratamientoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tratamiento> createTratamiento(@RequestBody Tratamiento tratamiento) {
        return ResponseEntity.ok(tratamientoRepository.save(tratamiento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tratamiento> updateTratamiento(@PathVariable Integer id, @RequestBody Tratamiento tratamientoDetails) {
        return tratamientoRepository.findById(id)
                .map(tratamiento -> {
                    tratamiento.setNombre(tratamientoDetails.getNombre());
                    tratamiento.setPrecio(tratamientoDetails.getPrecio());
                    tratamiento.setDescripcion(tratamientoDetails.getDescripcion());
                    tratamiento.setDuracionMinutos(tratamientoDetails.getDuracionMinutos());
                    return ResponseEntity.ok(tratamientoRepository.save(tratamiento));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTratamiento(@PathVariable Integer id) {
        if (tratamientoRepository.existsById(id)) {
            tratamientoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Tratamiento>> searchTratamientos(@RequestParam String nombre) {
        return ResponseEntity.ok(tratamientoRepository.findByNombreContaining(nombre));
    }
}
