package com.clinicadental.repositories;

import com.clinicadental.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);
    List<Paciente> findByApellidosContainingIgnoreCase(String apellidos);
    List<Paciente> findByDni(String dni);
    List<Paciente> findByTelefono(String telefono);
    List<Paciente> findByMutua(String mutua);
}
