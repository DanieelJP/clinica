package com.clinicadental.repositories;

import com.clinicadental.models.Visita;
import com.clinicadental.models.Paciente;
import com.clinicadental.models.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Integer> {
    List<Visita> findByPaciente(Paciente paciente);
    List<Visita> findByOdontologo(Odontologo odontologo);
    List<Visita> findByEstado(String estado);
    List<Visita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Visita> findByPacienteIdAndEstado(Integer pacienteId, String estado);
    List<Visita> findByOdontologoIdAndEstado(Integer odontologoId, String estado);
}
