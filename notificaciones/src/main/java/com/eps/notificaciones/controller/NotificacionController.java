package com.eps.notificaciones.controller;

import com.eps.notificaciones.dtos.NotificationRequest;
import com.eps.notificaciones.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    private final NotificationService service;

    public NotificacionController(NotificationService service) {
        this.service = service;
    }

    @Operation(summary = "Enviar notificación de cita")
    @PostMapping("/enviar")
    public String enviarNotificacion(@RequestBody NotificationRequest request) {
        return service.generarMensaje(request);
    }
}
