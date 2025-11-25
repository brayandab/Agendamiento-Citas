package com.frontend.dtos.request.notificacion;

import lombok.Data;

@Data
public class NotificacionRequestDTO {
    private String to;
    private String namePatient;
    private String apptDate;
    private String tipo;
}