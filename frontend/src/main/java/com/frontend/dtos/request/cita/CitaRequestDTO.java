package com.frontend.dtos.request.cita;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO para el envío de información al crear o modificar una cita médica.
 * Contiene todos los datos necesarios tanto para el registro de la cita
 * como para la generación de notificaciones al paciente y al doctor.
 */
@Data
public class CitaRequestDTO {

    /**
     * ID del paciente que solicita la cita.
     */
    private Long pacienteId;

    /**
     * ID del doctor con el que se agenda la cita.
     */
    private Long medicoId;

    /**
     * Especialidad médica relacionada con la cita.
     */
    private String especialidad;

    /**
     * Fecha de la cita.
     * Se formatea en JSON como cadena en el patrón "yyyy-MM-dd".
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    /**
     * Hora de la cita.
     * Se permite recibir formatos con o sin segundos en JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "[HH:mm:ss][HH:mm]")
    private LocalTime hora;

    /**
     * Motivo o descripción de la cita.
     */
    private String motivo;

    // ✅ Campos adicionales para notificaciones

    /**
     * Correo electrónico del paciente para enviar notificaciones.
     */
    private String emailPaciente;

    /**
     * Nombre completo del paciente.
     */
    private String nombrePaciente;

    /**
     * Nombre completo del doctor asignado a la cita.
     */
    private String nombreDoctor;
}
