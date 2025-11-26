package com.frontend.clients;

import com.frontend.dtos.request.cita.CitaRequestDTO;
import com.frontend.dtos.response.citas.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cliente Feign para interactuar con el microservicio de Citas.
 * Permite realizar operaciones CRUD y consultas específicas sobre citas de pacientes y médicos.
 */
@FeignClient(
        name = "citas",            // Nombre lógico del cliente Feign
        url = "http://localhost:8083" // URL base del microservicio de citas
)
public interface CitasClient {

    /**
     * Obtiene la lista completa de todas las citas.
     *
     * @return Lista de objetos CitaDTO
     */
    @GetMapping("/citas")
    List<CitaDTO> listarCitas();

    /**
     * Crea una nueva cita en el sistema.
     *
     * @param dto Objeto CitaRequestDTO con los datos de la cita a crear
     * @return El objeto CitaDTO recién creado
     */
    @PostMapping("/citas")
    CitaDTO crearCita(@RequestBody CitaRequestDTO dto);

    /**
     * Busca una cita por su ID.
     *
     * @param id Identificador de la cita a buscar
     * @return Objeto CitaDTO correspondiente
     */
    @GetMapping("/citas/{id}")
    CitaDTO buscarCitaPorId(@PathVariable("id") Long id);

    /**
     * Elimina una cita específica por su ID.
     *
     * @param id Identificador de la cita a eliminar
     */
    @DeleteMapping("/citas/{id}")
    void eliminarCita(@PathVariable("id") Long id);

    /**
     * Obtiene todas las citas asociadas a un paciente específico.
     *
     * @param pacienteId ID del paciente
     * @return Lista de citas del paciente
     */
    @GetMapping("/citas/paciente/{pacienteId}")
    List<CitaDTO> citasPorPaciente(@PathVariable("pacienteId") Long pacienteId);

    /**
     * Obtiene todas las citas asociadas a un médico específico.
     *
     * @param medicoId ID del médico
     * @return Lista de citas del médico
     */
    @GetMapping("/citas/medico/{medicoId}")
    List<CitaDTO> buscarPorMedico(@PathVariable("medicoId") Long medicoId);

    /**
     * Cancela una cita específica por su ID (versión simple sin notificación).
     *
     * @param id ID de la cita a cancelar
     * @return Objeto CitaDTO con el estado actualizado
     */
    @PutMapping("/citas/{id}/cancelar")
    CitaDTO cancelarCita(@PathVariable("id") Long id);

    /**
     * Cancela una cita específica por su ID enviando información del paciente
     * para notificación.
     *
     * @param id ID de la cita a cancelar
     * @param emailPaciente Correo electrónico del paciente
     * @param nombrePaciente Nombre del paciente
     * @return Objeto CitaDTO con el estado actualizado
     */
    @PutMapping("/citas/{id}/cancelar")
    CitaDTO cancelarCitaConNotificacion(
            @PathVariable("id") Long id,
            @RequestParam("emailPaciente") String emailPaciente,
            @RequestParam("nombrePaciente") String nombrePaciente
    );
}
