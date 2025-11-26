package com.eps.agenda.controllers;

import com.eps.agenda.dtos.AgendaResponseDTO;
import com.eps.agenda.dtos.AgendarCreateDTO;
import com.eps.agenda.models.Agenda;
import com.eps.agenda.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar la agenda médica.
 *
 * Expone endpoints para listar, crear, actualizar, eliminar y gestionar
 * la disponibilidad (bloqueo/desbloqueo) de franjas horarias.
 */
@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    /**
     * Obtiene la lista completa de agendas.
     * @return lista de todas las agendas.
     */
    @GetMapping
    public List<Agenda> listar() {
        return agendaService.listar();
    }

    /**
     * Obtiene una agenda por su ID.
     * @param id identificador de la agenda.
     * @return respuesta con la agenda encontrada o 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Agenda> obtenerPorId(@PathVariable Long id) {
        return agendaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva agenda con los datos enviados.
     * @param dto datos para crear la agenda.
     * @return agenda creada.
     */
    @PostMapping
    public ResponseEntity<Agenda> crear(@RequestBody AgendarCreateDTO dto) {
        return ResponseEntity.ok(agendaService.crear(dto));
    }

    /**
     * Actualiza una agenda existente.
     * @param id identificador de la agenda.
     * @param dto datos nuevos.
     * @return agenda actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Agenda> actualizar(@PathVariable Long id, @RequestBody AgendarCreateDTO dto) {
        return ResponseEntity.ok(agendaService.actualizar(id, dto));
    }

    /**
     * Elimina una agenda por su ID.
     * @param id identificador de la agenda.
     * @return respuesta sin contenido si se elimina correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        agendaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene las agendas junto con los datos del doctor asociado.
     * @return lista de agendas con información del doctor.
     */
    @GetMapping("/con-doctor")
    public List<AgendaResponseDTO> listarConDoctor() {
        return agendaService.listarConDoctor();
    }

    /**
     * Lista agendas pertenecientes a un médico específico.
     * @param doctorId ID del médico.
     * @return lista de agendas del médico.
     */
    @GetMapping("/doctor/{doctorId}")
    public List<AgendaResponseDTO> listarPorMedico(@PathVariable Long doctorId) {
        return agendaService.listarPorMedico(doctorId);
    }

    /**
     * Lista agendas del médico sin detalles adicionales.
     * @param doctorId ID del médico.
     * @return lista simple de agendas.
     */
    @GetMapping("/doctor/{doctorId}/simple")
    public List<AgendaResponseDTO> findByDoctorSimple(@PathVariable Long doctorId) {
        return agendaService.findByDoctorId(doctorId);
    }

    /**
     * Lista agendas por médico y fecha.
     * @param doctorId ID del médico.
     * @param fecha fecha en formato YYYY-MM-DD.
     * @return lista filtrada.
     */
    @GetMapping("/doctor/{doctorId}/fecha/{fecha}")
    public List<AgendaResponseDTO> listarPorMedicoYFecha(
            @PathVariable Long doctorId,
            @PathVariable String fecha
    ) {
        LocalDate f = LocalDate.parse(fecha);
        return agendaService.listarPorMedicoYFecha(doctorId, f);
    }

    /**
     * Crea múltiples franjas horarias en una sola petición.
     * @param dto datos de creación.
     * @return lista de agendas creadas.
     */
    @PostMapping("/franjas")
    public ResponseEntity<List<Agenda>> crearFranjas(@RequestBody AgendarCreateDTO dto) {
        List<Agenda> creadas = agendaService.crearFranjas(dto);
        return ResponseEntity.ok(creadas);
    }

    /**
     * Bloquea una agenda específica identificada por su ID.
     * Maneja errores personalizados para indicar estado de conflicto,
     * inexistencia o error interno.
     * @param id ID de la agenda.
     * @return agenda bloqueada o mensaje de error.
     */
    @PutMapping("/{id}/bloquear")
    public ResponseEntity<?> bloquearAgenda(@PathVariable Long id) {
        try {
            Agenda bloqueada = agendaService.bloquearAgenda(id);
            return ResponseEntity.ok(bloqueada);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            String mensaje = e.getMessage();

            if (mensaje.contains("no encontrada")) {
                error.put("mensaje", mensaje);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } else if (mensaje.contains("ya está ocupada")) {
                error.put("mensaje", mensaje);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            } else {
                error.put("mensaje", "Error al bloquear la agenda");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }
    }

    /**
     * Bloquea una agenda buscando por médico, fecha y hora.
     * @param medicoId ID del médico.
     * @param fecha fecha en formato YYYY-MM-DD.
     * @param hora hora en formato HH:mm.
     * @return agenda bloqueada o mensaje de error.
     */
    @PostMapping("/bloquear-horario")
    public ResponseEntity<?> bloquearPorHorario(
            @RequestParam Long medicoId,
            @RequestParam String fecha,
            @RequestParam String hora
    ) {
        try {
            LocalDate fechaLocal = LocalDate.parse(fecha);
            LocalTime horaLocal = LocalTime.parse(hora);

            Agenda bloqueada = agendaService.buscarYBloquearAgenda(medicoId, fechaLocal, horaLocal);
            return ResponseEntity.ok(bloqueada);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            String mensaje = e.getMessage();

            if (mensaje.contains("No se encontró")) {
                error.put("mensaje", mensaje);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } else if (mensaje.contains("no está disponible")) {
                error.put("mensaje", mensaje);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            } else {
                error.put("mensaje", "Error al bloquear el horario");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }
    }

    /**
     * Desbloquea una agenda previamente bloqueada.
     * @param id ID de la agenda.
     * @return agenda desbloqueada o error si no existe.
     */
    @PutMapping("/{id}/desbloquear")
    public ResponseEntity<?> desbloquearAgenda(@PathVariable Long id) {
        try {
            Agenda desbloqueada = agendaService.desbloquearAgenda(id);
            return ResponseEntity.ok(desbloqueada);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}