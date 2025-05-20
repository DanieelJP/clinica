package com.clinicadental.models;

public enum DiaSemana {
    LUNES,
    MARTES,
    MIERCOLES,
    MIERCOLES_SIN_TILDE("MIERCOLES"),
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO;

    private final String value;

    DiaSemana() {
        this.value = this.name();
    }

    DiaSemana(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DiaSemana fromString(String text) {
        for (DiaSemana dia : DiaSemana.values()) {
            if (dia.name().equalsIgnoreCase(text) || dia.getValue().equalsIgnoreCase(text)) {
                return dia;
            }
        }
        throw new IllegalArgumentException("No se encontró el día de la semana: " + text);
    }
} 