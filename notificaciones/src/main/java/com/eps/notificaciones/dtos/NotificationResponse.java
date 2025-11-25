package com.eps.notificaciones.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private boolean enviado;
    private String mensaje;
    private Long notificacionId;
}