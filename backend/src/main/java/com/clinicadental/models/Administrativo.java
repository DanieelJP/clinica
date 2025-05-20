package com.clinicadental.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Administrativo")
@PrimaryKeyJoinColumn(name = "usuario_id")
@DiscriminatorValue("Administrativo")
public class Administrativo extends Usuario {
    @Column(nullable = false, length = 50)
    private String departamento;
    // Los administrativos no necesitan campos adicionales específicos
    // pero podrían tener permisos especiales para gestionar citas y pacientes
} 