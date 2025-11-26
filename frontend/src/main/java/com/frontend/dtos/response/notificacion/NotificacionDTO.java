package com.frontend.dtos.response.notificacion;

import lombok.Data;

/**
 * DTO que representa la respuesta del microservicio de notificaciones
 * luego de intentar enviar un correo o notificación.
 * Se utiliza para informar al frontend si la operación fue exitosa o si hubo errores.
 */
@Data
public class NotificacionDTO {

    /**
     * Estado de la operación de notificación.
     * Por ejemplo: "SUCCESS" si el correo se envió correctamente,
     * o "FAIL" si ocurrió algún error.
     */
    private String status;

    /**
     * Mensaje descriptivo de la operación de notificación.
     * Puede contener información adicional sobre el éxito o la causa de error.
     */
    private String message;
}
