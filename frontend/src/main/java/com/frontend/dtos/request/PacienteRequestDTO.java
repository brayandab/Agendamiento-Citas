package com.frontend.dtos.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PacienteRequestDTO {
    private String documento;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String eps;

    private UsuarioRequestDTO usuario;
}
