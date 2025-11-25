package com.eps.notificaciones.dtos;

import com.eps.notificaciones.model.NotificacionTipo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String to;
    private String namePatient;
    private String apptDate;
    private NotificacionTipo tipo;


    private Long citaId;
    private Long pacienteId;
    private String especialidad;
    private String nombreDoctor;
    private String hora;
}