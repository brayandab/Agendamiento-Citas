package com.frontend.controller.agendar;

import com.frontend.clients.AgendaClient;
import com.frontend.dtos.request.agenda.AgendaRequestDTO;
import com.frontend.dtos.response.agenda.AgendaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador frontend para manejar las operaciones relacionadas con la agenda.
 * Permite que los pacientes y doctores interactúen con los horarios disponibles de los doctores.
 *
 * Funciona como intermediario entre la vista Thymeleaf y el microservicio de agenda a través de AgendaClient.
 */
@Controller
@RequestMapping("/front/agenda")
public class AgendaFrontController {

    @Autowired
    private AgendaClient agendaClient;

    // ------------------- CONSULTA DE AGENDA -------------------

    /**
     * Obtiene la agenda completa de un doctor, incluyendo toda la información de franjas.
     *
     * @param doctorId ID del doctor
     * @return Lista de AgendaDTO con todos los horarios del doctor
     */
    @GetMapping("/doctor/{doctorId}")
    @ResponseBody
    public List<AgendaDTO> obtenerAgendaPorDoctor(@PathVariable Long doctorId) {
        return agendaClient.findByDoctor(doctorId);
    }

    /**
     * Obtiene la agenda simple de un doctor, únicamente con las franjas disponibles.
     *
     * @param doctorId ID del doctor
     * @return Lista de AgendaDTO simplificada
     */
    @GetMapping("/doctor/{doctorId}/simple")
    @ResponseBody
    public List<AgendaDTO> obtenerAgendaSimplePorDoctor(@PathVariable Long doctorId) {
        return agendaClient.findByDoctorSimple(doctorId);
    }

    // ------------------- CREACIÓN DE HORARIOS -------------------

    /**
     * Muestra el formulario para que el doctor registre nuevos horarios en su agenda.
     *
     * @param model Modelo Thymeleaf para pasar datos a la vista
     * @return Nombre de la plantilla Thymeleaf "AgendarDoctor"
     */
    @GetMapping("/crear")
    public String mostrarFormularioCrearAgenda(Model model) {
        AgendaRequestDTO dto = new AgendaRequestDTO();
        dto.setDisponible(true);
        dto.setMedicoId(9L); // Se puede reemplazar dinámicamente con el doctor actual
        model.addAttribute("agenda", dto);
        return "usuario/doctor/AgendarDoctor";
    }

    /**
     * Recibe los datos del formulario y crea un nuevo horario en la agenda del doctor.
     *
     * @param agendaRequestDTO Datos del horario a crear
     * @return Redirección a la página de creación con indicador de éxito
     */
    @PostMapping("/crear")
    public String crearAgendaSubmit(@ModelAttribute AgendaRequestDTO agendaRequestDTO) {
        agendaClient.crearAgenda(agendaRequestDTO);
        return "redirect:/front/agenda/crear?exito";
    }

    // ------------------- BLOQUEO DE HORARIOS -------------------

    /**
     * Bloquea un horario existente en la agenda de un doctor.
     * Convierte las fechas y horas a String antes de enviar la actualización al microservicio.
     *
     * @param id ID del horario a bloquear
     * @return AgendaDTO actualizado con el horario bloqueado
     * @throws RuntimeException si no se encuentra la agenda con el ID proporcionado
     */
    @PutMapping("/bloquear/{id}")
    @ResponseBody
    public AgendaDTO bloquearHorario(@PathVariable Long id) {
        AgendaDTO agenda = agendaClient.buscarAgendaPorId(id);

        if (agenda == null) {
            throw new RuntimeException("No se encontró la agenda con ID " + id);
        }

        AgendaRequestDTO dto = new AgendaRequestDTO();
        dto.setMedicoId(agenda.getMedicoId());

        // Convertir LocalDate y LocalTime a String
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        dto.setFecha(agenda.getFecha().format(dateFormatter));
        dto.setHoraInicio(agenda.getHoraInicio().format(timeFormatter));
        dto.setHoraFin(agenda.getHoraFin().format(timeFormatter));
        dto.setDisponible(false);

        return agendaClient.actualizar(id, dto);
    }

}
