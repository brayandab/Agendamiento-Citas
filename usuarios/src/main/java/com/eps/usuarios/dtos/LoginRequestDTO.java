package com.eps.usuarios.dtos;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String correo;
    private String password;

}
