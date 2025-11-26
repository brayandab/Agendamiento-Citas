package com.frontend.dtos.response.usuarios;

import lombok.Data;

/**
 * DTO que representa la información de un doctor dentro del sistema.
 * Se utiliza principalmente en respuestas del backend hacia el frontend
 * para mostrar detalles de doctores, tanto para administración como para pacientes.
 */
@Data
public class DoctorDTO {

    /**
     * Identificador único del doctor.
     */
    private Long id;

    /**
     * Especialidad médica del doctor, ej. "Cardiología", "Pediatría".
     */
    private String especialidad;

    /**
     * Número de teléfono de contacto del doctor.
     */
    private Long telefono;

    /**
     * Años de experiencia profesional del doctor.
     */
    private int aniosExperiencia;

    /**
     * Consultorio donde atiende el doctor.
     */
    private String consultorio;

    /**
     * Horario de atención del doctor.
     * Puede ser un rango como "08:00-16:00".
     */
    private String horarioAtencion;

    /**
     * Objeto que contiene la información básica del usuario asociado al doctor
     * (nombre, apellido, correo, rol, etc.).
     * Se utiliza para relacionar la información del doctor con los datos de login
     * y otras funciones de usuario.
     */
    private UsuarioDTO usuario;
}
