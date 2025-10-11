package com.eps.agenda.controllers;

import com.eps.agenda.dtos.AgendarDTO;
import com.eps.agenda.models.Agenda;
import com.eps.agenda.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Agenda> crear(@RequestBody AgendarDTO dto) {
        return ResponseEntity.ok(agendaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agenda> actualizar(@PathVariable Long id, @RequestBody AgendarDTO dto) {
        return ResponseEntity.ok(agendaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        agendaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
