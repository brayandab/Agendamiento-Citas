package com.frontend.dtos.request.usuarios;

import lombok.Data;

/**
 * DTO utilizado para crear o actualizar un usuario en el sistema.
 * Contiene información básica de identificación y autenticación,
 * así como el rol que tendrá dentro de la aplicación.
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
     * Correo electrónico del usuario, utilizado también para login.
     */
    private String correo;

    /**
     * Contraseña del usuario para autenticación.
     */
    private String password;

    /**
     * Rol del usuario en el sistema.
     * Valor por defecto: "PACIENTE".
     */
    private String rol = "PACIENTE";
}
