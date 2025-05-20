package com.clinicadental.repositories;

import com.clinicadental.models.Horario;
import com.clinicadental.models.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    
    @Query("SELECT h FROM Horario h WHERE h.odontologo.id = :odontologoId")
    List<Horario> findByOdontologoId(@Param("odontologoId") Integer odontologoId);
    
    @Query("SELECT h FROM Horario h WHERE h.odontologo.id = :odontologoId AND h.diaSemana = :diaSemana AND h.horaInicio <= :hora AND h.horaFin >= :hora")
    List<Horario> findByOdontologoIdAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
        @Param("odontologoId") Integer odontologoId,
        @Param("diaSemana") DiaSemana diaSemana,
        @Param("hora") LocalTime hora,
        @Param("hora") LocalTime hora2
    );
} 