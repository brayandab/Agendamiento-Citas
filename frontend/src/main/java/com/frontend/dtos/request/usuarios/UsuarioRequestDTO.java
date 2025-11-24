package com.frontend.dtos.request.usuarios;

import lombok.Data;

@Data
public class UsuarioRequestDTO {

    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private String rol = "PACIENTE";
}
