package com.frontend.dtos.response;

import lombok.Data;

/**
 * DTO que representa la respuesta del backend al intentar iniciar sesión.
 * Contiene la información básica del usuario que se utilizará en sesión
 * para personalizar la interfaz y controlar el acceso según rol.
 */
@Data
public class LoginResponseDTO {

    /**
     * Identificador único del usuario logueado.
     * Se utiliza para manejar la sesión y asociar acciones del usuario.
     */
    private Long id;

    /**
     * Nombre del usuario.
     * Se puede mostrar en la interfaz para personalizar la bienvenida.
     */
    private String nombre;

    /**
     * Apellido del usuario.
     * Se puede combinar con el nombre para mostrar el nombre completo.
     */
    private String apellido;

    /**
     * Correo electrónico del usuario.
     * Principalmente usado como identificador de inicio de sesión y notificaciones.
     */
    private String correo;

    /**
     * Rol del usuario en el sistema.
     * Valores típicos: "ADMIN", "DOCTOR", "PACIENTE".
     * Determina los permisos y accesos dentro de la aplicación.
     */
    private String rol;
}
