package com.eps.usuarios.controllers;

import com.eps.usuarios.dtos.PacienteRequestDTO;
import com.eps.usuarios.models.Paciente;
import com.eps.usuarios.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas con los pacientes.
 *
 * Proporciona endpoints para crear, listar, consultar y eliminar pacientes dentro
 * del módulo de usuarios.
 */
@RestController
@RequestMapping("/usuarios/pacientes")
@CrossOrigin(origins = "http://localhost:8085")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    /**
     * Obtiene la lista completa de pacientes registrados.
     *
     * @return lista de objetos {@link Paciente}.
     */
    @GetMapping
    public List<Paciente> listar() {
        return pacienteService.listarPacientes();
    }

    /**
     * Crea un nuevo paciente a partir de los datos enviados en el DTO.
     *
     * @param dto información necesaria para crear un paciente.
     * @return el paciente creado dentro de un {@link ResponseEntity}.
     */
    @PostMapping
    public ResponseEntity<Paciente> crear(@RequestBody PacienteRequestDTO dto) {
        Paciente guardado = pacienteService.crearPaciente(dto);
        return ResponseEntity.ok(guardado);
    }

    /**
     * Busca un paciente por su ID.
     *
     * @param id identificador único del paciente.
     * @return el paciente encontrado o null si no existe.
     */
    @GetMapping("/{id}")
    public Paciente buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id);
    }

    /**
     * Elimina un paciente por su ID.
     *
     * @param id identificador del paciente a eliminar.
     */
    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable Long id){
        pacienteService.eliminarPorId(id);
    }

    /*
    // Endpoint opcional para buscar paciente por correo electrónico
    @GetMapping("/correo/{correo}")
    public Paciente buscarPorCorreo(@PathVariable String correo) {
        return pacienteService.buscarPorCorreo(correo);
    }
    */
}
