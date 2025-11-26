package com.eps.notificaciones.dtos;

import com.eps.notificaciones.model.NotificacionTipo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para recibir los datos necesarios para enviar una notificación
 * relacionada con una cita médica. Este objeto actúa como el cuerpo del request
 * recibido por el endpoint de envío de notificaciones.
 *
 * Contiene información del paciente, la cita y el tipo de notificación
 * que se desea enviar.
 *
 * Campos principales:
 * <ul>
 *     <li><b>to</b>: correo electrónico del paciente.</li>
 *     <li><b>namePatient</b>: nombre del paciente que recibirá la notificación.</li>
 *     <li><b>apptDate</b>: fecha de la cita en formato String.</li>
 *     <li><b>tipo</b>: tipo de notificación (recordatorio, cancelación, etc.).</li>
 *     <li><b>citaId</b>: identificador de la cita relacionada.</li>
 *     <li><b>pacienteId</b>: identificador del paciente.</li>
 *     <li><b>especialidad</b>: especialidad de la cita médica.</li>
 *     <li><b>nombreDoctor</b>: nombre del médico asignado.</li>
 *     <li><b>hora</b>: hora de la cita en formato String.</li>
 * </ul>
 *
 * Este DTO es utilizado por el servicio {@link com.eps.notificaciones.service.NotificationService}
 * para construir y enviar la notificación correspondiente.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    /** Correo del paciente destinatario de la notificación. */
    private String to;

    /** Nombre del paciente que recibirá el mensaje. */
    private String namePatient;

    /** Fecha de la cita en formato texto (yyyy-MM-dd). */
    private String apptDate;

    /** Tipo de notificación a enviar (recordatorio, confirmación, cancelación, etc.). */
    private NotificacionTipo tipo;

    /** ID de la cita asociada a la notificación. */
    private Long citaId;

    /** ID del paciente al que pertenece la cita. */
    private Long pacienteId;

    /** Especialidad médica de la cita (ejemplo: cardiología, pediatría). */
    private String especialidad;

    /** Nombre del doctor asignado a la cita. */
    private String nombreDoctor;

    /** Hora de la cita en formato texto (HH:mm). */
    private String hora;
}
