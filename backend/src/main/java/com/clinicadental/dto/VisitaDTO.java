package com.clinicadental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;

public class VisitaDTO {
    @NotNull(message = "El ID del odontólogo es requerido")
    private Integer odontologo_id;

    @NotBlank(message = "El DNI del paciente es requerido")
    private String paciente_dni;

    @NotNull(message = "La fecha y hora son requeridas")
    @Future(message = "La fecha y hora deben ser futuras")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El motivo de la visita es requerido")
    private String motivo;

    private String observaciones;
    private Integer tratamiento_id;

    // Getters y Setters
    public Integer getOdontologo_id() {
        return odontologo_id;
    }

    public void setOdontologo_id(Integer odontologo_id) {
        this.odontologo_id = odontologo_id;
    }

    public String getPaciente_dni() {
        return paciente_dni;
    }

    public void setPaciente_dni(String paciente_dni) {
        this.paciente_dni = paciente_dni;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getTratamiento_id() {
        return tratamiento_id;
    }

    public void setTratamiento_id(Integer tratamiento_id) {
        this.tratamiento_id = tratamiento_id;
    }
} 