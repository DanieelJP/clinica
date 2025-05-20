package com.clinicadental.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "dni")
@Entity
@Table(name = "Paciente")
public class Paciente {
    @Id
    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(length = 50)
    private String obraSocial;

    @Column(length = 50)
    private String mutua;

    @Column(name = "tipo_pago", nullable = false, length = 20)
    private String tipoPago;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Responsable> responsables = new HashSet<>();

    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    private Set<Visita> visitas = new HashSet<>();

    public Paciente() {
    }
}
