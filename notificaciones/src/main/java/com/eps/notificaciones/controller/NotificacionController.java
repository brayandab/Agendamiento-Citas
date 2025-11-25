package com.eps.notificaciones.controller;

import com.eps.notificaciones.dtos.NotificationRequest;
import com.eps.notificaciones.dtos.NotificationResponse;
import com.eps.notificaciones.model.Notificacion;
import com.eps.notificaciones.repository.NotificacionRepository;
import com.eps.notificaciones.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificationService service;
    private final NotificacionRepository repository;

    public NotificacionController(NotificationService service, NotificacionRepository repository) {
        this.service = service;
        this.repository = repository;
    }


      //Endpoint principal para enviar notificaciones

    @Operation(summary = "Enviar notificación de cita")
    @PostMapping("/enviar")
    public ResponseEntity<NotificationResponse> enviarNotificacion(@RequestBody NotificationRequest request) {
        NotificationResponse response = service.enviarNotificacion(request);
        return ResponseEntity.ok(response);
    }


      //Obtener todas las notificaciones

    @GetMapping
    public ResponseEntity<List<Notificacion>> listarNotificaciones() {
        return ResponseEntity.ok(repository.findAll());
    }


     // Obtener notificaciones por paciente

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Notificacion>> notificacionesPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(repository.findByPacienteId(pacienteId));
    }


     //Obtener notificaciones por cita

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<List<Notificacion>> notificacionesPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(repository.findByCitaId(citaId));
    }


      //Obtener notificaciones fallidas

    @GetMapping("/fallidas")
    public ResponseEntity<List<Notificacion>> notificacionesFallidas() {
        return ResponseEntity.ok(repository.findByEstado("FALLIDO"));
    }
}