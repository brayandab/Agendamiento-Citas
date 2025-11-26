package com.eps.usuarios.dtos;

import lombok.Data;

/**
 * DTO utilizado para recibir las credenciales de inicio de sesión
 * desde el cliente (frontend).
 *
 * Este objeto es enviado al endpoint /usuarios/login y contiene
 * únicamente los datos necesarios para validar el acceso del usuario.
 */
@Data
public class LoginRequestDTO {

    /**
     * Correo electrónico del usuario que intenta iniciar sesión.
     */
    private String correo;

    /**
     * Contraseña ingresada por el usuario.
     * Esta contraseña será comparada contra la almacenada en la base de datos
     * usando el servicio de validación correspondiente.
     */
    private String password;
}
