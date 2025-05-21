package com.clinicadental.repositories;

import com.clinicadental.models.Visita;
import com.clinicadental.models.Paciente;
import com.clinicadental.models.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Integer> {
    @Query("SELECT v FROM Visita v LEFT JOIN FETCH v.paciente LEFT JOIN FETCH v.odontologo LEFT JOIN FETCH v.tratamiento WHERE v.paciente = :paciente")
    List<Visita> findByPaciente(@Param("paciente") Paciente paciente);

    @Query("SELECT v FROM Visita v LEFT JOIN FETCH v.paciente LEFT JOIN FETCH v.odontologo LEFT JOIN FETCH v.tratamiento WHERE v.odontologo = :odontologo")
    List<Visita> findByOdontologo(@Param("odontologo") Odontologo odontologo);

    @Query("SELECT v FROM Visita v LEFT JOIN FETCH v.paciente LEFT JOIN FETCH v.odontologo LEFT JOIN FETCH v.tratamiento WHERE v.estado = :estado")
    List<Visita> findByEstado(@Param("estado") Visita.EstadoVisita estado);

    @Query("SELECT v FROM Visita v LEFT JOIN FETCH v.paciente LEFT JOIN FETCH v.odontologo LEFT JOIN FETCH v.tratamiento WHERE v.fechaHora BETWEEN :inicio AND :fin")
    List<Visita> findByFechaHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT v FROM Visita v LEFT JOIN FETCH v.paciente LEFT JOIN FETCH v.odontologo LEFT JOIN FETCH v.tratamiento WHERE v.paciente.dni = :dni AND v.estado = :estado")
    List<Visita> findByPacienteDniAndEstado(@Param("dni") String dni, @Param("estado") Visita.EstadoVisita estado);

    @Query("SELECT v FROM Visita v LEFT JOIN FETCH v.paciente LEFT JOIN FETCH v.odontologo LEFT JOIN FETCH v.tratamiento WHERE v.odontologo.id = :odontologoId AND v.estado = :estado")
    List<Visita> findByOdontologoIdAndEstado(@Param("odontologoId") Integer odontologoId, @Param("estado") Visita.EstadoVisita estado);
}
