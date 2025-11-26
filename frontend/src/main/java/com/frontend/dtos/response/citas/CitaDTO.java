package com.frontend.dtos.response.citas;

import lombok.Data;

/**
 * DTO que representa la información de una cita médica.
 * Se utiliza para mostrar citas a pacientes y doctores,
 * así como para realizar operaciones de gestión de citas
 * como creación, cancelación y consulta.
 */
@Data
public class CitaDTO {

    /**
     * Identificador único de la cita.
     * Permite referenciar la cita en operaciones de modificación o cancelación.
     */
    private Long id;

    /**
     * Identificador del paciente que agenda la cita.
     * Se usa para filtrar las citas de un paciente específico.
     */
    private Long pacienteId;

    /**
     * Identificador del doctor asignado a la cita.
     * Se usa para filtrar las citas de un doctor específico.
     */
    private Long medicoId;

    /**
     * Especialidad médica asociada a la cita.
     * Permite filtrar o clasificar citas por tipo de atención.
     */
    private String especialidad;

    /**
     * Fecha de la cita en formato de cadena (yyyy-MM-dd).
     * Se utiliza para mostrar en el calendario o en listados de citas.
     */
    private String fecha;

    /**
     * Hora de la cita en formato de cadena (HH:mm).
     * Se utiliza para mostrar la franja horaria al paciente y al doctor.
     */
    private String hora;

    /**
     * Estado de la cita (por ejemplo: "Pendiente", "Confirmada", "Cancelada").
     * Se usa para controlar acciones disponibles y notificaciones.
     */
    private String estado;
}
