package com.frontend.dtos.response.agenda;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO que representa la información de una agenda creada por un doctor.
 * Se utiliza tanto para mostrar horarios disponibles a pacientes como
 * para gestión de horarios por parte del doctor.
 */
@Data
public class AgendaDTO {

    /**
     * Identificador único de la agenda.
     */
    private Long id;

    /**
     * Identificador del doctor al que pertenece esta agenda.
     * Se utiliza para filtrar la agenda de un doctor específico.
     */
    private Long medicoId;

    /**
     * Fecha del turno.
     * Usada para mostrar disponibilidad de días específicos.
     */
    private LocalDate fecha;

    /**
     * Hora de inicio del turno.
     * Se utiliza para calcular franjas horarias disponibles y crear citas.
     */
    private LocalTime horaInicio;

    /**
     * Hora de fin del turno.
     * Se utiliza junto con horaInicio para definir la duración del turno.
     */
    private LocalTime horaFin;

    /**
     * Indica si el horario está disponible para agendar citas.
     * True si puede ser reservado por pacientes; false si está bloqueado.
     */
    private Boolean disponible;
}
