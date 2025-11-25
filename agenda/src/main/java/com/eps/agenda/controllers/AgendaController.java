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

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping
    public List<Agenda> listar() {
        return agendaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agenda> obtenerPorId(@PathVariable Long id) {
        return agendaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Agenda> crear(@RequestBody AgendarCreateDTO dto) {
        return ResponseEntity.ok(agendaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agenda> actualizar(@PathVariable Long id, @RequestBody AgendarCreateDTO dto) {
        return ResponseEntity.ok(agendaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        agendaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/con-doctor")
    public List<AgendaResponseDTO> listarConDoctor() {
        return agendaService.listarConDoctor();
    }

    @GetMapping("/doctor/{doctorId}")
    public List<AgendaResponseDTO> listarPorMedico(@PathVariable Long doctorId) {
        return agendaService.listarPorMedico(doctorId);
    }

    @GetMapping("/doctor/{doctorId}/simple")
    public List<AgendaResponseDTO> findByDoctorSimple(@PathVariable Long doctorId) {
        return agendaService.findByDoctorId(doctorId);
    }

    @GetMapping("/doctor/{doctorId}/fecha/{fecha}")
    public List<AgendaResponseDTO> listarPorMedicoYFecha(
            @PathVariable Long doctorId,
            @PathVariable String fecha
    ) {
        LocalDate f = LocalDate.parse(fecha);
        return agendaService.listarPorMedicoYFecha(doctorId, f);
    }

    @PostMapping("/franjas")
    public ResponseEntity<List<Agenda>> crearFranjas(@RequestBody AgendarCreateDTO dto) {
        List<Agenda> creadas = agendaService.crearFranjas(dto);
        return ResponseEntity.ok(creadas);
    }

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