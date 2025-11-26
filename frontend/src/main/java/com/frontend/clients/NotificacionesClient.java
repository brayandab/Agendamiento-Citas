package com.frontend.clients;

import com.frontend.dtos.request.notificacion.NotificacionRequestDTO;
import com.frontend.dtos.response.notificacion.NotificacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Cliente Feign para interactuar con el microservicio de Notificaciones.
 * Permite enviar correos electrónicos y otros tipos de notificaciones.
 */
@FeignClient(
        name = "notificaciones",          // Nombre lógico del cliente Feign
        url = "http://localhost:8084"     // URL base del microservicio de notificaciones
)
public interface NotificacionesClient {

    /**
     * Envía un correo electrónico o notificación a través del microservicio.
     *
     * @param dto Objeto NotificacionRequestDTO que contiene los datos del mensaje a enviar
     * @return Objeto NotificacionDTO con la información de la notificación enviada
     */
    @PostMapping("/notificaciones/enviar")
    NotificacionDTO enviarCorreo(@RequestBody NotificacionRequestDTO dto);
}
