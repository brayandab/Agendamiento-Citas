package com.frontend.clients;


import com.frontend.dtos.request.notificacion.NotificacionRequestDTO;
import com.frontend.dtos.response.notificacion.NotificacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "notificaciones",
        url = "http://localhost:8084"
)
public interface NotificacionesClient {

    @PostMapping("/notificaciones/enviar")
    NotificacionDTO enviarCorreo(@RequestBody NotificacionRequestDTO dto);
}
