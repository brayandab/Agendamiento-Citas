package com.eps.usuarios.dtos;

import com.eps.usuarios.models.enums.Especialidad;
import lombok.Data;

/**
 * DTO utilizado para recibir los datos necesarios para crear o actualizar
 * un registro de tipo {@link com.eps.usuarios.models.Doctor}.
 *
 * Este objeto solo contiene la información relevante para el controlador
 * y no expone directamente la entidad del modelo.
 */
@Data
public class DoctorRequestDTO {

    /**
     * Especialidad médica del doctor.
     * Se recibe como un valor del enum {@link Especialidad}.
     */
    private Especialidad especialidad;

    /**
     * Número de teléfono del doctor.
     */
    private Long telefono;

    /**
     * Años de experiencia en la práctica médica.
     */
    private int aniosExperiencia;

    /**
     * Identificación o nombre del consultorio donde atiende el doctor.
     */
    private String consultorio;

    /**
     * Horario general de atención del doctor (por ejemplo: "L-V 8am-4pm").
     */
    private String horarioAtencion;

    /**
     * ID del usuario asociado al doctor (relación 1 a 1 en la entidad Usuario).
     */
    private Long usuarioId;
}
