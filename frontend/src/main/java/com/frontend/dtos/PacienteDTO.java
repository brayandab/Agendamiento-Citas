package com.frontend.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PacienteDTO {
    private Long id;
    private String documento;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String eps;

    private UsuarioDTO usuario; // ← relación 1 a 1
}
