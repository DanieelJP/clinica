package com.clinicadental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import com.clinicadental.models.Visita.EstadoVisita;

public class VisitaDTO {
    private Integer odontologo_id;
    private String paciente_dni;
    private LocalDateTime fechaHora;
    private String motivo;
    private String observaciones;
    private Integer tratamiento_id;
    private EstadoVisita estado;

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

    public EstadoVisita getEstado() {
        return estado;
    }

    public void setEstado(EstadoVisita estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "VisitaDTO{" +
                "odontologo_id=" + odontologo_id +
                ", paciente_dni='" + paciente_dni + '\'' +
                ", fechaHora=" + fechaHora +
                ", motivo='" + motivo + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", tratamiento_id=" + tratamiento_id +
                ", estado=" + estado +
                '}';
    }
} 