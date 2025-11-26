package com.frontend.dtos.request.agenda;

import lombok.Data;

/**
 * Data Transfer Object (DTO) para enviar información de la agenda de un doctor.
 * Se utiliza tanto para crear como para actualizar horarios en la agenda.
 */
@Data
public class AgendaRequestDTO {

    /** ID del médico al que corresponde la agenda */
    private Long medicoId;

    /** Fecha de la agenda en formato "yyyy-MM-dd" */
    private String fecha;

    /** Hora de inicio de la franja horaria en formato "HH:mm" */
    private String horaInicio;

    /** Hora de fin de la franja horaria en formato "HH:mm" */
    private String horaFin;

    /** Indica si la franja horaria está disponible para agendar */
    private boolean disponible;
}
