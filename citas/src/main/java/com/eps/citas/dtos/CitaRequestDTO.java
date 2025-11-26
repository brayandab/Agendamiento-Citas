package com.eps.citas.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) utilizado para la creación de nuevas citas médicas.
 *
 * Este DTO transporta toda la información necesaria para registrar una cita
 * en el sistema. Es enviado desde el frontend hacia el microservicio de citas.
 *
 * Contiene datos del paciente, del médico, la fecha y hora de la cita,
 * así como información adicional para envío de notificaciones por correo.
 */
@Data
public class CitaRequestDTO {

    /** ID del paciente que solicita la cita. */
    private Long pacienteId;

    /** ID del médico con quien se agendará la cita. */
    private Long medicoId;

    /** Especialidad médica asociada a la cita (ejemplo: cardiología, pediatría). */
    private String especialidad;

    /**
     * Fecha de la cita.
     * Se serializa en formato "yyyy-MM-dd".
     * Ejemplo: 2025-03-18
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    /**
     * Hora de la cita.
     * Acepta formatos "HH:mm" o "HH:mm:ss".
     * Ejemplos válidos: 14:30, 09:00:00
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "[HH:mm:ss][HH:mm]")
    private LocalTime hora;

    /** Motivo o descripción breve de la consulta médica. */
    private String motivo;

    /** Correo electrónico del paciente (usado para notificaciones). */
    private String emailPaciente;

    /** Nombre completo del paciente (útil para el envío de correos). */
    private String nombrePaciente;

    /** Nombre del doctor asignado a la cita (también usado para correos). */
    private String nombreDoctor;
}
