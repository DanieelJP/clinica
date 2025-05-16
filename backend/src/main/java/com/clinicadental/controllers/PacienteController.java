package com.clinicadental.controllers;

import com.clinicadental.models.Paciente;
import com.clinicadental.repositories.PacienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    public PacienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> getAllPacientes() {
        return ResponseEntity.ok(pacienteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable Integer id) {
        return pacienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Paciente> createPaciente(@RequestBody Paciente paciente) {
        return ResponseEntity.ok(pacienteRepository.save(paciente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable Integer id, @RequestBody Paciente pacienteDetails) {
        return pacienteRepository.findById(id)
                .map(paciente -> {
                    paciente.setNombre(pacienteDetails.getNombre());
                    paciente.setApellidos(pacienteDetails.getApellidos());
                    paciente.setFechaNacimiento(pacienteDetails.getFechaNacimiento());
                    paciente.setTelefono(pacienteDetails.getTelefono());
                    paciente.setCp(pacienteDetails.getCp());
                    paciente.setDni(pacienteDetails.getDni());
                    paciente.setMutua(pacienteDetails.getMutua());
                    paciente.setTipoPago(pacienteDetails.getTipoPago());
                    return ResponseEntity.ok(pacienteRepository.save(paciente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Integer id) {
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Paciente>> searchPacientes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellidos,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String mutua) {
        
        List<Paciente> pacientes = pacienteRepository.findAll();
        if (nombre != null) {
            pacientes = pacienteRepository.findByNombreContainingIgnoreCase(nombre);
        }
        if (apellidos != null) {
            pacientes = pacienteRepository.findByApellidosContainingIgnoreCase(apellidos);
        }
        if (dni != null) {
            pacientes = pacienteRepository.findByDni(dni);
        }
        if (telefono != null) {
            pacientes = pacienteRepository.findByTelefono(telefono);
        }
        if (mutua != null) {
            pacientes = pacienteRepository.findByMutua(mutua);
        }
        return ResponseEntity.ok(pacientes);
    }
}
