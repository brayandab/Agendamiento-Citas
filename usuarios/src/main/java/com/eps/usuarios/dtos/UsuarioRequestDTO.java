package com.eps.usuarios.dtos;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    private String nombre;

    private String apellido;

    private String correo;

    private String password;

    private String rol;

}
