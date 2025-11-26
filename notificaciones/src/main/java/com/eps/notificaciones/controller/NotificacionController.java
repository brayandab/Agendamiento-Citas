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

/**
 * Controlador encargado de gestionar el envío y la consulta de notificaciones
 * relacionadas con citas médicas. Expone endpoints REST que permiten:
 *
 * <ul>
 *     <li>Enviar una notificación (correo) al paciente.</li>
 *     <li>Listar todas las notificaciones enviadas.</li>
 *     <li>Consultar notificaciones asociadas a un paciente específico.</li>
 *     <li>Consultar notificaciones asociadas a una cita médica.</li>
 *     <li>Listar notificaciones que hayan fallado en el envío.</li>
 * </ul>
 *
 * Este controlador delega la lógica principal de envío de notificaciones al
 * servicio {@link NotificationService} y las consultas a la base de datos al
 * repositorio {@link NotificacionRepository}.
 *
 * Los endpoints están documentados con Swagger para facilitar su exploración.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificationService service;
    private final NotificacionRepository repository;

    /**
     * Constructor que inyecta el servicio de notificaciones y el repositorio.
     *
     * @param service servicio encargado de procesar y enviar notificaciones
     * @param repository repositorio de acceso a datos de notificaciones
     */
    public NotificacionController(NotificationService service, NotificacionRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    /**
     * Endpoint principal para enviar una notificación al paciente.
     * El cuerpo de la petición debe incluir todos los datos necesarios
     * como correo, fecha de la cita, tipo de notificación, etc.
     *
     * @param request cuerpo de la solicitud con datos de la notificación
     * @return una respuesta indicando si el envío fue exitoso o fallido
     */
    @Operation(summary = "Enviar notificación de cita")
    @PostMapping("/enviar")
    public ResponseEntity<NotificationResponse> enviarNotificacion(@RequestBody NotificationRequest request) {
        NotificationResponse response = service.enviarNotificacion(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todas las notificaciones almacenadas en la base de datos.
     *
     * @return lista de todas las notificaciones
     */
    @GetMapping
    public ResponseEntity<List<Notificacion>> listarNotificaciones() {
        return ResponseEntity.ok(repository.findAll());
    }

    /**
     * Obtiene todas las notificaciones enviadas a un paciente específico.
     *
     * @param pacienteId ID del paciente
     * @return lista de notificaciones asociadas al paciente
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Notificacion>> notificacionesPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(repository.findByPacienteId(pacienteId));
    }

    /**
     * Obtiene todas las notificaciones relacionadas con una cita específica.
     *
     * @param citaId ID de la cita
     * @return lista de notificaciones asociadas a la cita médica
     */
    @GetMapping("/cita/{citaId}")
    public ResponseEntity<List<Notificacion>> notificacionesPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(repository.findByCitaId(citaId));
    }

    /**
     * Obtiene todas las notificaciones que no pudieron enviarse correctamente
     * y quedaron registradas con estado "FALLIDO".
     *
     * @return lista de notificaciones fallidas
     */
    @GetMapping("/fallidas")
    public ResponseEntity<List<Notificacion>> notificacionesFallidas() {
        return ResponseEntity.ok(repository.findByEstado("FALLIDO"));
    }
}