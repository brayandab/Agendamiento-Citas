package com.eps.notificaciones.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta tras intentar enviar una notificación.
 * Indica si el envío fue exitoso y contiene un mensaje descriptivo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    /**
     * Bandera que indica si la notificación fue enviada correctamente.
     */
    private boolean enviado;

    /**
     * Mensaje que describe el resultado del envío.
     */
    private String mensaje;

    /**
     * Identificador único generado para la notificación almacenada.
     */
    private Long notificacionId;
}
