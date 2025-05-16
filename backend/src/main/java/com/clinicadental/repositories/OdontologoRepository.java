package com.clinicadental.repositories;

import com.clinicadental.models.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OdontologoRepository extends JpaRepository<Odontologo, Integer> {
    Optional<Odontologo> findByMatricula(String matricula);
    boolean existsByMatricula(String matricula);
    
    // Usando JPQL para buscar por ID (que es el mismo que usuario_id en la base de datos)
    @Query("SELECT o FROM Odontologo o WHERE o.id = :usuarioId")
    Optional<Odontologo> findByUsuarioId(@Param("usuarioId") Integer usuarioId);
}
