package com.eps.citas.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) que representa la agenda disponible de un médico.
 *
 * Esta clase se utiliza para transportar información sobre los horarios
 * que un médico tiene disponibles en un día específico. Se usa tanto en
 * consultas como en la creación/actualización de agendas dentro del sistema.
 *
 * Campos principales:
 * - id: Identificador único de la agenda.
 * - medicoId: ID del médico al que pertenece este bloque de horario.
 * - fecha: Día en el que aplica la agenda.
 * - horaInicio: Hora exacta en la que inicia la disponibilidad.
 * - horaFin: Hora exacta en la que termina la disponibilidad.
 * - disponible: Indica si el horario está libre (true) o si ya está ocupado/reservado (false).
 */
@Data
public class AgendaDTO {

    /** Identificador único de la agenda. */
    private Long id;

    /** ID del médico al que pertenece este horario de atención. */
    private Long medicoId;

    /** Fecha en la que aplica la disponibilidad. */
    private LocalDate fecha;

    /** Hora en la que inicia el intervalo de atención. */
    private LocalTime horaInicio;

    /** Hora en la que finaliza el intervalo de atención. */
    private LocalTime horaFin;

    /**
     * Indica si el horario está disponible.
     * true = el horario está libre y se puede agendar una cita.
     * false = el horario ya fue reservado.
     */
    private Boolean disponible;
}
