package com.eps.agenda.dtos;


import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 * DTO de respuesta que representa una franja de agenda
 * junto con información del médico asociado.
 */
@Data
public class AgendaResponseDTO {


    // ID de la franja de agenda.
    private Long id;


    // ID del médico dueño de la franja.
    private Long medicoId;


    // Fecha correspondiente a la franja.
    private LocalDate fecha;


    // Hora de inicio de la franja.
    private LocalTime horaInicio;


    // Hora de fin de la franja.
    private LocalTime horaFin;


    // Indica si la franja está disponible.
    private Boolean disponible;


    // Información detallada del médico asociado.
    private DoctorDTO doctor;
}