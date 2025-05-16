package com.clinicadental.controllers;

import com.clinicadental.models.Odontologo;
import com.clinicadental.repositories.OdontologoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/odontologos")
public class OdontologoController {

    private final OdontologoRepository odontologoRepository;

    public OdontologoController(OdontologoRepository odontologoRepository) {
        this.odontologoRepository = odontologoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Odontologo>> getAllOdontologos() {
        return ResponseEntity.ok(odontologoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Odontologo> getOdontologoById(@PathVariable Integer id) {
        return odontologoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Odontologo> createOdontologo(@RequestBody Odontologo odontologo) {
        return ResponseEntity.ok(odontologoRepository.save(odontologo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Odontologo> updateOdontologo(@PathVariable Integer id, @RequestBody Odontologo odontologoDetails) {
        return odontologoRepository.findById(id)
                .map(odontologo -> {
                    odontologo.setMatricula(odontologoDetails.getMatricula());
                    odontologo.setEspecialidad(odontologoDetails.getEspecialidad());
                    return ResponseEntity.ok(odontologoRepository.save(odontologo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOdontologo(@PathVariable Integer id) {
        if (odontologoRepository.existsById(id)) {
            odontologoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-matricula")
    public ResponseEntity<Odontologo> getOdontologoByMatricula(@RequestParam String matricula) {
        return odontologoRepository.findByMatricula(matricula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
