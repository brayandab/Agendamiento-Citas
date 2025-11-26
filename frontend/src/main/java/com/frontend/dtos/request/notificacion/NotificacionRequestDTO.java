package com.frontend.dtos.request.notificacion;

import lombok.Data;

/**
 * DTO utilizado para enviar información al microservicio de notificaciones.
 * Contiene los datos necesarios para generar y enviar correos electrónicos
 * a los pacientes sobre sus citas médicas u otros eventos relevantes.
 */
@Data
public class NotificacionRequestDTO {

    /**
     * Dirección de correo electrónico del destinatario.
     */
    private String to;

    /**
     * Nombre del paciente al que se dirige la notificación.
     */
    private String namePatient;

    /**
     * Fecha de la cita o evento relevante que se notificará.
     */
    private String apptDate;

    /**
     * Tipo de notificación (por ejemplo: "Cita Creada", "Cita Cancelada").
     */
    private String tipo;
}
