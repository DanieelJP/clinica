package com.clinicadental.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Odontologo")
@PrimaryKeyJoinColumn(name = "usuario_id")
@DiscriminatorValue("Odontologo")
public class Odontologo extends Usuario {
    @Column(nullable = false, unique = true, length = 50)
    private String matricula;

    @Column(nullable = false, length = 50)
    private String especialidad;

    @OneToMany(mappedBy = "odontologo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Horario> horarios = new HashSet<>();

    @OneToMany(mappedBy = "odontologo", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Visita> visitas = new HashSet<>();
}
