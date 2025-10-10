package com.eps.citas.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CitaRequestDTO {
    private Long pacienteId;
    private Long medicoId;
    private String especialidad;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
}
