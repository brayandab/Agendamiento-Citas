package com.frontend.dtos.request.usuarios;

import lombok.Data;

/**
 * DTO utilizado para crear o actualizar información de un doctor.
 * Contiene los datos profesionales y de contacto que se requieren
 * para registrar al doctor en el sistema.
 */
@Data
public class DoctorRequestDTO {

    /**
     * Especialidad médica del doctor (ej. "Cardiología", "Pediatría").
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
     * Consultorio o ubicación física donde atiende el doctor.
     */
    private String consultorio;

    /**
     * Horario de atención del doctor (ej. "08:00-16:00").
     */
    private String horarioAtencion;

    /**
     * ID del usuario asociado a este doctor en el sistema.
     */
    private Long usuarioId;
}
