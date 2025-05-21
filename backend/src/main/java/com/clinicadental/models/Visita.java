package com.clinicadental.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Visita")
public class Visita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_dni", nullable = false)
    @JsonBackReference
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odontologo_id", nullable = false)
    @JsonBackReference
    private Odontologo odontologo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tratamiento_id")
    private Tratamiento tratamiento;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 255)
    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoVisita estado = EstadoVisita.PROGRAMADA;

    public enum EstadoVisita {
        PROGRAMADA,
        REALIZADA,
        CANCELADA,
        NO_ASISTIO
    }
}
