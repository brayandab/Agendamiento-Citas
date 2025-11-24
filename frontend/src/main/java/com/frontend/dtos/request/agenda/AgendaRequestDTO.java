package com.frontend.dtos.request.agenda;

import lombok.Data;

@Data
public class AgendaRequestDTO {
    private Long medicoId;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private boolean disponible;
}
