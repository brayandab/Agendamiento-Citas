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

    @GetMapping("/agenda/con-doctor")
    List<AgendaDTO> listarConDoctor();

    @GetMapping("/agenda/doctor/{doctorId}")
    List<AgendaDTO> findByDoctor(@PathVariable("doctorId") Long doctorId);

    @GetMapping("/agenda/doctor/{doctorId}/simple")
    List<AgendaDTO> findByDoctorSimple(@PathVariable("doctorId") Long doctorId);

    @PostMapping("/agenda/franjas")
    void crearFranjas(@RequestBody AgendaRequestDTO dto);

    @PutMapping("/agenda/{id}")
    AgendaDTO actualizar(@PathVariable("id") Long id, @RequestBody AgendaRequestDTO dto);
}

