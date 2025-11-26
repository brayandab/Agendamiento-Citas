package com.eps.agenda.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad que representa una franja de agenda asociada a un médico.
 * Incluye fecha, horas y disponibilidad del bloque horario.
 */
@Data
@Entity
@Table(name = "agenda")
public class Agenda {

    // ID autogenerado de la franja de agenda.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    // ID del médico al que pertenece la franja.
    private Long medicoId;

    // Fecha asignada a la franja.
    private LocalDate fecha;

    // Hora de inicio de la franja.
    private LocalTime horaInicio;

    // Hora de fin de la franja.
    private LocalTime horaFin;

    // Indica si la franja está disponible para agendar.
    private Boolean disponible;
}