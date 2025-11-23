package com.frontend.dtos.request.cita;

import lombok.Data;

@Data
public class CitaRequestDTO {
    private Long pacienteId;
    private Long medicoId;
    private String especialidad;
    private String fecha;
    private String hora;
    private String motivo;
}