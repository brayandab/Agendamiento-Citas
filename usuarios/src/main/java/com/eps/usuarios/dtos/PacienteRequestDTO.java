package com.eps.usuarios.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;


@Data
public class PacienteRequestDTO {
    private String documento;
    private String telefono;
    private String direccion;
    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;
    private String eps;
    private Long usuarioId;
}
