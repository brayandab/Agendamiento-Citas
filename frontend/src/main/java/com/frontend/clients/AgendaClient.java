package com.frontend.clients;


import com.frontend.dtos.request.agenda.AgendaRequestDTO;
import com.frontend.dtos.response.agenda.AgendaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "agenda",
        url = "http://localhost:8082"
)
public interface AgendaClient {

    @GetMapping("/agenda")
    List<AgendaDTO> listarAgenda();

    @PostMapping("/agenda")
    AgendaDTO crearAgenda(@RequestBody AgendaRequestDTO dto);

    @GetMapping("/agenda/{id}")
    AgendaDTO buscarAgendaPorId(@PathVariable("id") Long id);

    @DeleteMapping("/agenda/{id}")
    void eliminarAgenda(@PathVariable("id") Long id);

    // 👉 Agendas disponibles por doctor
    @GetMapping("/agenda/doctor/{doctorId}")
    List<AgendaDTO> findByDoctor(@PathVariable("doctorId") Long doctorId);

    // 👉 Agendas disponibles por doctor y fecha
    @GetMapping("/agenda/doctor/{doctorId}/fecha/{fecha}")
    List<AgendaDTO> findByDoctorAndFecha(
            @PathVariable("doctorId") Long doctorId,
            @PathVariable("fecha") String fecha
    );
}
