package com.frontend.dtos.response.agenda;
import lombok.Data;

@Data
public class AgendaDTO {
    private Long id;
    private Long medicoId;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private boolean disponible;
}