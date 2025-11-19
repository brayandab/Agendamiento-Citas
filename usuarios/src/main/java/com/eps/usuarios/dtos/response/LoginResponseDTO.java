package com.eps.usuarios.dtos.response;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String rol;
}