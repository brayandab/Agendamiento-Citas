package com.eps.agenda.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalTime;


/**
 * DTO utilizado para crear o registrar una nueva franja de agenda.
 * Contiene la información necesaria para construir un bloque horario
 * que podrá ser asignado a un médico.
 */
@Data
public class AgendarCreateDTO {


    // ID del médico al que pertenece la franja de agenda.
    private Long medicoId;


    /**
     * Fecha de la cita o franja.
     * Formato forzado a "yyyy-MM-dd".
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;


    // Hora de inicio de la franja (acepta HH:mm o HH:mm:ss).
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "[HH:mm:ss][HH:mm]")
    private LocalTime horaInicio;


    // Hora de fin de la franja (acepta HH:mm o HH:mm:ss).
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "[HH:mm:ss][HH:mm]")
    private LocalTime horaFin;


    // Indica si la franja está disponible para agendamiento.
    private Boolean disponible;
}