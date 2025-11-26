package com.frontend.clients;

import com.frontend.dtos.request.agenda.AgendaRequestDTO;
import com.frontend.dtos.response.agenda.AgendaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cliente Feign para interactuar con el microservicio de Agenda.
 * Permite realizar operaciones CRUD y consultas específicas sobre agendas de doctores.
 */
@FeignClient(
        name = "agenda",          // Nombre lógico del cliente Feign
        url = "http://localhost:8082" // URL base del microservicio de agenda
)
public interface AgendaClient {

    /**
     * Obtiene la lista completa de todas las agendas.
     *
     * @return Lista de objetos AgendaDTO
     */
    @GetMapping("/agenda")
    List<AgendaDTO> listarAgenda();

    /**
     * Crea una nueva agenda en el sistema.
     *
     * @param dto Objeto AgendaRequestDTO con los datos de la agenda a crear
     * @return El objeto AgendaDTO recién creado
     */
    @PostMapping("/agenda")
    AgendaDTO crearAgenda(@RequestBody AgendaRequestDTO dto);

    /**
     * Busca una agenda por su ID.
     *
     * @param id Identificador de la agenda a buscar
     * @return Objeto AgendaDTO correspondiente
     */
    @GetMapping("/agenda/{id}")
    AgendaDTO buscarAgendaPorId(@PathVariable("id") Long id);

    /**
     * Elimina una agenda específica por su ID.
     *
     * @param id Identificador de la agenda a eliminar
     */
    @DeleteMapping("/agenda/{id}")
    void eliminarAgenda(@PathVariable("id") Long id);

    /**
     * Lista todas las agendas que tienen asignado un doctor.
     *
     * @return Lista de agendas con doctor asignado
     */
    @GetMapping("/agenda/con-doctor")
    List<AgendaDTO> listarConDoctor();

    /**
     * Obtiene todas las agendas asociadas a un doctor específico.
     *
     * @param doctorId ID del doctor
     * @return Lista de agendas del doctor
     */
    @GetMapping("/agenda/doctor/{doctorId}")
    List<AgendaDTO> findByDoctor(@PathVariable("doctorId") Long doctorId);

    /**
     * Obtiene una versión "simple" de las agendas de un doctor (posiblemente menos detalles).
     *
     * @param doctorId ID del doctor
     * @return Lista de agendas simplificadas del doctor
     */
    @GetMapping("/agenda/doctor/{doctorId}/simple")
    List<AgendaDTO> findByDoctorSimple(@PathVariable("doctorId") Long doctorId);

    /**
     * Crea franjas horarias en una agenda específica.
     *
     * @param dto Objeto AgendaRequestDTO con la información de las franjas
     */
    @PostMapping("/agenda/franjas")
    void crearFranjas(@RequestBody AgendaRequestDTO dto);

    /**
     * Actualiza una agenda existente por su ID.
     *
     * @param id  Identificador de la agenda a actualizar
     * @param dto Datos actualizados de la agenda
     * @return Objeto AgendaDTO actualizado
     */
    @PutMapping("/agenda/{id}")
    AgendaDTO actualizar(@PathVariable("id") Long id, @RequestBody AgendaRequestDTO dto);

    /**
     * Bloquea una agenda específica, probablemente para que no pueda recibir nuevas citas.
     *
     * @param id ID de la agenda a bloquear
     * @return AgendaDTO con el estado actualizado
     */
    @PutMapping("/agenda/{id}/bloquear")
    AgendaDTO bloquearAgenda(@PathVariable("id") Long id);
}
