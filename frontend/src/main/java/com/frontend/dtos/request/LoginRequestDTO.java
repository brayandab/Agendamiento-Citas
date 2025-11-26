package com.frontend.dtos.request;

import lombok.Data;

/**
 * DTO utilizado para enviar las credenciales de inicio de sesión
 * al microservicio de usuarios.
 * Contiene únicamente el correo y la contraseña del usuario.
 */
@Data
public class LoginRequestDTO {

    /**
     * Correo electrónico del usuario.
     * Se utiliza para identificar al usuario en el login.
     */
    private String correo;

    /**
     * Contraseña del usuario.
     * Se utiliza para autenticar al usuario.
     */
    private String password;
}
