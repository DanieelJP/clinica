package com.clinicadental.dto;

import com.clinicadental.models.DiaSemana;
import java.time.LocalTime;

public class HorarioDTO {
    private Integer odontologo_id;
    private String dia;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private boolean disponible;

    // Constructor vacío
    public HorarioDTO() {}

    // Getters y Setters
    public Integer getOdontologo_id() {
        return odontologo_id;
    }

    public void setOdontologo_id(Integer odontologo_id) {
        this.odontologo_id = odontologo_id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
} 