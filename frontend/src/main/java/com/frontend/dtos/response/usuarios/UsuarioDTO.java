package com.frontend.dtos.response.usuarios;

import lombok.Data;

/**
 * DTO que representa la información de un usuario en el sistema.
 * Se utiliza en respuestas del backend hacia el frontend para mostrar
 * o manipular datos de usuarios, incluyendo pacientes y doctores.
 */
@Data
public class UsuarioDTO {

    /**
     * Identificador único del usuario.
     */
    private Long id;

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Apellido del usuario.
     */
    private String apellido;

    /**
     * Correo electrónico del usuario. Usado para login y notificaciones.
     */
    private String correo;

    /**
     * Contraseña del usuario. Generalmente encriptada y no se expone en frontend.
     */
    private String password;

    /**
     * Rol del usuario, puede ser "ADMIN", "DOCTOR" o "PACIENTE".
     * Define los permisos y el acceso dentro del sistema.
     */
    private String rol;

    /**
     * Indica si el usuario está activo o inactivo en el sistema.
     * Puede ser usado para bloquear el acceso sin eliminar el usuario.
     */
    private Boolean activo;
}
