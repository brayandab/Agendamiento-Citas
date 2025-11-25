package com.frontend.dtos.response.usuarios;

import lombok.Data;

@Data
public class PacienteDTO {
    private Long id;
    private String documento;
    private String telefono;
    private String direccion;
    private String fechaNacimiento;
    private String eps;

    private Long usuarioId;
}
