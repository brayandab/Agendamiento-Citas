package com.eps.agenda.dtos;

import lombok.Data;

/**
 * DTO que representa los datos básicos del usuario relacionado
 * a un médico dentro del sistema.
 */
@Data
public class UsuarioDTO {

    // ID del usuario.
    private Long id;

    // Nombre del usuario.
    private String nombre;

    // Apellido del usuario.
    private String apellido;

    // Correo electrónico del usuario.
    private String correo;
}