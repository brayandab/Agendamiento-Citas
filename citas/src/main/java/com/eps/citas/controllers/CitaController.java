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

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancelarCita(@PathVariable Long id) {
        Cita cancelada = citaService.cancelarCita(id);
        return ResponseEntity.ok(cancelada);
    }

    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable Long id) {
        citaService.eliminarPorId(id);
    }
}