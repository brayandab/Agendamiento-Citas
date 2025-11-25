package com.frontend.clients;

import com.frontend.dtos.request.cita.CitaRequestDTO;
import com.frontend.dtos.response.citas.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "citas",
        url = "http://localhost:8083"
)
public interface CitasClient {

    @GetMapping("/citas")
    List<CitaDTO> listarCitas();

    @PostMapping("/citas")
    CitaDTO crearCita(@RequestBody CitaRequestDTO dto);

    @GetMapping("/citas/{id}")
    CitaDTO buscarCitaPorId(@PathVariable("id") Long id);

    @DeleteMapping("/citas/{id}")
    void eliminarCita(@PathVariable("id") Long id);

    @GetMapping("/citas/paciente/{pacienteId}")
    List<CitaDTO> citasPorPaciente(@PathVariable("pacienteId") Long pacienteId);

    @GetMapping("/citas/medico/{medicoId}")
    List<CitaDTO> buscarPorMedico(@PathVariable("medicoId") Long medicoId);


     // Cancelar cita sin parámetros (versión simple)

    @PutMapping("/citas/{id}/cancelar")
    CitaDTO cancelarCita(@PathVariable("id") Long id);


     // Cancelar cita con datos del paciente para notificación

    @PutMapping("/citas/{id}/cancelar")
    CitaDTO cancelarCitaConNotificacion(
            @PathVariable("id") Long id,
            @RequestParam("emailPaciente") String emailPaciente,
            @RequestParam("nombrePaciente") String nombrePaciente
    );
}