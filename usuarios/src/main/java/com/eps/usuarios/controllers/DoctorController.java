package com.eps.usuarios.controllers;

import com.eps.usuarios.dtos.DoctorRequestDTO;
import com.eps.usuarios.models.Doctor;
import com.eps.usuarios.models.enums.Especialidad;
import com.eps.usuarios.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de doctores dentro del módulo de usuarios.
 *
 * Expone endpoints para crear, consultar, eliminar y filtrar doctores
 * según criterios como ID, especialidad o ID de usuario del sistema.
 */
@RestController
@RequestMapping("/usuarios/doctores")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Obtiene la lista completa de doctores registrados.
     *
     * @return lista de objetos {@link Doctor}.
     */
    @GetMapping
    public List<Doctor> listar() {
        return doctorService.listarDoctores();
    }

    /**
     * Crea un nuevo doctor a partir de los datos enviados en el DTO.
     *
     * @param dto datos necesarios para crear un doctor.
     * @return el doctor creado envuelto en un {@link ResponseEntity}.
     */
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody DoctorRequestDTO dto) {
        Doctor guardado = doctorService.createDoctor(dto);
        return ResponseEntity.ok(guardado);
    }

    /**
     * Busca un doctor por su ID.
     *
     * @param id identificador del doctor.
     * @return el doctor encontrado o null si no existe.
     */
    @GetMapping("/{id}")
    public Doctor findById(@PathVariable Long id) {
        return doctorService.findById(id);
    }

    /**
     * Elimina un doctor por su ID.
     *
     * @param id identificador del doctor a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        doctorService.deleteById(id);
    }

    /**
     * Obtiene todos los doctores filtrados por una especialidad específica.
     *
     * @param especialidad especialidad médica (enum {@link Especialidad}).
     * @return lista de doctores que coinciden con esa especialidad.
     */
    @GetMapping("/especialidad/{especialidad}")
    public List<Doctor> findByEspecialidad(@PathVariable Especialidad especialidad) {
        System.out.println("Doctores: " + especialidad);
        return doctorService.findByEspecialidad(especialidad);
    }

    /**
     * Busca un doctor por el ID del usuario asociado en el sistema.
     *
     * @param usuarioId ID del usuario.
     * @return el doctor asociado, o un 404 si no se encuentra.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Doctor> findByUsuarioId(@PathVariable Long usuarioId) {
        Doctor doctor = doctorService.findByUsuarioId(usuarioId);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctor);
    }
}
