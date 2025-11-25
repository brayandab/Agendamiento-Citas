package com.eps.citas.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendaDTO {
    private Long id;
    private Long medicoId;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean disponible;
}