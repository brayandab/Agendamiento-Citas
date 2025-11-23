package com.frontend.dtos.response.citas;

import lombok.Data;

@Data
public class CitaDTO {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private String especialidad;
    private String fecha;
    private String hora;
    private String motivo;
}