package com.eps.citas.controllers;

import com.eps.citas.dtos.CitaRequestDTO;
import com.eps.citas.models.Cita;
import com.eps.citas.services.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Cita> crear(@RequestBody CitaRequestDTO dto) {
        Cita guardada = citaService.guardarCita(dto);
        return ResponseEntity.ok(guardada);
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
