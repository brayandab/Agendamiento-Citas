package com.eps.usuarios.controllers;

import com.eps.usuarios.dtos.PacienteRequestDTO;
import com.eps.usuarios.models.Paciente;
import com.eps.usuarios.models.Usuario;
import com.eps.usuarios.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios/pacientes")
@CrossOrigin(origins = "http://localhost:8085")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public List<Paciente> listar() {
        return pacienteService.listarPacientes();
    }

    @PostMapping
    public ResponseEntity<Paciente> crear(@RequestBody PacienteRequestDTO dto) {
        Paciente guardado = pacienteService.crearPaciente(dto);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping("/{id}")
    public Paciente buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable Long id){
        pacienteService.eliminarPorId(id);
    }

    /*@GetMapping("/correo/{correo}")
    public Paciente buscarPorCorreo(@PathVariable String correo){ return pacienteService.buscarPorCorreo(correo);}
*/
}
