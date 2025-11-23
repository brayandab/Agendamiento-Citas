package com.eps.usuarios.controllers;

import com.eps.usuarios.dtos.DoctorRequestDTO;
import com.eps.usuarios.dtos.PacienteRequestDTO;
import com.eps.usuarios.models.Doctor;
import com.eps.usuarios.models.Paciente;
import com.eps.usuarios.services.DoctorService;
import com.eps.usuarios.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

@RestController
@RequestMapping("/usuarios/doctores")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public List<Doctor> listar() {
        return doctorService.listarDoctores();
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody DoctorRequestDTO dto) {
        Doctor guardado = doctorService.createDoctor(dto);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping("/{id}")
    public Doctor findById(@PathVariable Long id) {
        return doctorService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        doctorService.deleteById(id);
    }

    @GetMapping("/especialidad/{especialidad}")
    public List<Doctor> findByEspecialidad(@PathVariable String especialidad) {
        return doctorService.findByEspecialidad(especialidad);
    }

}
