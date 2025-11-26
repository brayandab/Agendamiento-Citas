package com.eps.agenda.dtos;

import lombok.Data;

/**
 * DTO que representa la información básica de un médico.
 * Incluye datos generales y la relación con el usuario asociado.
 */
@Data
public class DoctorDTO {

    // ID del doctor.
    private Long id;

    // Especialidad médica del doctor.
    private String especialidad;

    // Número de teléfono del doctor.
    private Long telefono;

    // Años de experiencia profesional.
    private Integer aniosExperiencia;

    // Consultorio asignado al doctor.
    private String consultorio;

    // Horario de atención del médico.
    private String horarioAtencion;

    // Usuario asociado al doctor.
    private UsuarioDTO usuario;
}