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

@RestController
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public List<Cita> listar() {
        return citaService.listarCitas();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CitaRequestDTO dto) {
        try {
            // ✅ Validar que venga el email del paciente
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

            // Manejo específico de errores
            String mensaje = e.getMessage();
            HttpStatus status;

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

    @GetMapping("/{id}")
    public Cita buscarPorId(@PathVariable Long id) {
        return citaService.buscarPorId(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Cita> buscarPorPaciente(@PathVariable Long pacienteId) {
        return citaService.buscarPorPacienteId(pacienteId);
    }

    @GetMapping("/medico/{medicoId}")
    public List<Cita> buscarPorMedico(@PathVariable Long medicoId) {
        return citaService.buscarPorMedicoId(medicoId);
    }

    /**
     * ✅ MEJORADO: Cancelar cita con datos del paciente para notificación
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
                // Si vienen los datos, enviar notificación
                cancelada = citaService.cancelarCita(id, emailPaciente, nombrePaciente);
            } else {
                // Si no vienen, cancelar sin notificación específica
                cancelada = citaService.cancelarCita(id);
            }

            return ResponseEntity.ok(cancelada);
        } catch (RuntimeException e) {
            String mensaje = e.getMessage();
            HttpStatus status;

            if (mensaje.contains("12 horas")) {
                status = HttpStatus.BAD_REQUEST; // 400
            } else if (mensaje.contains("no encontrada")) {
                status = HttpStatus.NOT_FOUND; // 404
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
            }

            Map<String, String> error = new HashMap<>();
            error.put("mensaje", mensaje);

            return ResponseEntity.status(status).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable Long id) {
        citaService.eliminarPorId(id);
    }
}