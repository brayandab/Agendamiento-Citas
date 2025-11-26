package com.eps.usuarios.dtos;

import lombok.Data;

/**
 * DTO utilizado para la creación y actualización de usuarios dentro del sistema.
 * Representa la información básica necesaria para registrar un usuario que luego
 * puede estar asociado a roles específicos como paciente o doctor.
 */
@Data
public class UsuarioRequestDTO {

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Apellido del usuario.
     */
    private String apellido;

    /**
     * Correo electrónico del usuario.
     * Debe ser único en el sistema y se utiliza para iniciar sesión.
     */
    private String correo;

    /**
     * Contraseña del usuario en texto plano.
     * Será encriptada antes de guardarse en la base de datos.
     */
    private String password;

    /**
     * Rol asignado al usuario (ej. "PACIENTE", "DOCTOR", "ADMIN").
     * Determina permisos y accesos dentro del sistema.
     */
    private String rol;
}
