package com.eps.agenda.controllers;

import com.eps.agenda.dtos.AgendaResponseDTO;
import com.eps.agenda.dtos.AgendarCreateDTO;
import com.eps.agenda.models.Agenda;
import com.eps.agenda.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    // ======================================
    // NUEVO: Bloquear agenda desde el endpoint cuando se reserva
    // ======================================
    @PutMapping("/{id}/bloquear")
    public ResponseEntity<Agenda> bloquearAgenda(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.bloquearAgenda(id));
    }
}
