package com.eps.citas.controllers;

import com.eps.citas.dtos.CitaRequestDTO;
import com.eps.citas.models.Cita;
import com.eps.citas.services.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST encargado de gestionar todas las operaciones relacionadas con las citas
 * médicas del sistema. Incluye creación, consulta, cancelación y eliminación.
 *
 * Maneja validaciones básicas y devuelve respuestas estructuradas, incluyendo mensajes
 * de error personalizados y códigos HTTP según el tipo de excepción.
 *
 * Base path: /citas
 */
@RestController
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    /**
     * Obtiene la lista completa de citas registradas en el sistema.
     *
     * @return Lista de objetos {@link Cita}
     */
    @GetMapping
    public List<Cita> listar() {
        return citaService.listarCitas();
    }

    /**
     * Crea una nueva cita médica.
     *
     * Validaciones incluidas:
     *  - Email del paciente no puede ser nulo ni vacío.
     *  - Nombre del paciente no puede ser nulo ni vacío.
     *
     * Manejo de errores:
     *  - 400 BAD_REQUEST si faltan campos.
     *  - 409 CONFLICT si la fecha/hora no están disponibles.
     *  - 404 NOT_FOUND si no se encuentra un médico o paciente.
     *  - 500 INTERNAL_SERVER_ERROR para errores generales.
     *
     * @param dto Datos necesarios para crear la cita
     * @return La cita creada o un mensaje de error estructurado
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CitaRequestDTO dto) {
        try {
            // Validaciones manuales
            if (dto.getEmailPaciente() == null || dto.getEmailPaciente().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "El email del paciente es requerido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            if (dto.getNombrePaciente() == null || dto.getNombrePaciente().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "El nombre del paciente es requerido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            Cita guardada = citaService.guardarCita(dto);
            return ResponseEntity.ok(guardada);

        } catch (RuntimeException e) {

            String mensaje = e.getMessage();
            HttpStatus status;

            // Mapeo del error a código HTTP
            if (mensaje.contains("no está disponible")) {
                status = HttpStatus.CONFLICT; // 409
            } else if (mensaje.contains("No se encontró")) {
                status = HttpStatus.NOT_FOUND; // 404
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
            }

            Map<String, String> error = new HashMap<>();
            error.put("mensaje", mensaje);

            return ResponseEntity.status(status).body(error);
        }
    }

    /**
     * Busca una cita por su ID.
     *
     * @param id Identificador de la cita
     * @return Objeto {@link Cita} correspondiente
     */
    @GetMapping("/{id}")
    public Cita buscarPorId(@PathVariable Long id) {
        return citaService.buscarPorId(id);
    }

    /**
     * Obtiene todas las citas asociadas a un paciente específico.
     *
     * @param pacienteId ID del paciente
     * @return Lista de citas del paciente
     */
    @GetMapping("/paciente/{pacienteId}")
    public List<Cita> buscarPorPaciente(@PathVariable Long pacienteId) {
        return citaService.buscarPorPacienteId(pacienteId);
    }

    /**
     * Obtiene todas las citas asociadas a un médico específico.
     *
     * @param medicoId ID del médico
     * @return Lista de citas del médico
     */
    @GetMapping("/medico/{medicoId}")
    public List<Cita> buscarPorMedico(@PathVariable Long medicoId) {
        return citaService.buscarPorMedicoId(medicoId);
    }

    /**
     * Cancela una cita por su ID.
     *
     * Comportamiento:
     *  - Si recibe emailPaciente y nombrePaciente, se envía una notificación personalizada.
     *  - Si no, simplemente cancela la cita sin enviar aviso.
     *
     * Posibles errores:
     *  - 400 BAD_REQUEST si intenta cancelarse con menos de 12 horas de anticipación.
     *  - 404 NOT_FOUND si la cita no existe.
     *  - 500 INTERNAL_SERVER_ERROR para fallas generales.
     *
     * @param id ID de la cita
     * @param emailPaciente Email del paciente (opcional)
     * @param nombrePaciente Nombre del paciente (opcional)
     * @return Cita cancelada o mensaje de error
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarCita(
            @PathVariable Long id,
            @RequestParam(required = false) String emailPaciente,
            @RequestParam(required = false) String nombrePaciente
    ) {
        try {
            Cita cancelada;

            if (emailPaciente != null && nombrePaciente != null) {
                cancelada = citaService.cancelarCita(id, emailPaciente, nombrePaciente);
            } else {
                cancelada = citaService.cancelarCita(id);
            }

            return ResponseEntity.ok(cancelada);

        } catch (RuntimeException e) {
            String mensaje = e.getMessage();
            HttpStatus status;

            if (mensaje.contains("12 horas")) {
                status = HttpStatus.BAD_REQUEST;
            } else if (mensaje.contains("no encontrada")) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            Map<String, String> error = new HashMap<>();
            error.put("mensaje", mensaje);

            return ResponseEntity.status(status).body(error);
        }
    }

    /**
     * Elimina una cita definitivamente del sistema.
     *
     * @param id ID de la cita
     */
    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable Long id) {
        citaService.eliminarPorId(id);
    }
}
