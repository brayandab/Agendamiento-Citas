package com.frontend.dtos.response.agenda;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendaDTO {
    private Long agendaId; // <- importante, coincide con backend
    private Long medicoId;
    private LocalDate fecha;  // cambiar a LocalDate para evitar parsing de string
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean disponible;
}
