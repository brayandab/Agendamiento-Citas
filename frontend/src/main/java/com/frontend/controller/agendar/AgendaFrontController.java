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

@Controller
@RequestMapping("/front/agenda")
public class AgendaFrontController {

    @Autowired
    private AgendaClient agendaClient;

    // Obtener agenda completa de un doctor (para pacientes)
    @GetMapping("/doctor/{doctorId}")
    @ResponseBody
    public List<AgendaDTO> obtenerAgendaPorDoctor(@PathVariable Long doctorId) {
        return agendaClient.findByDoctor(doctorId);
    }

    // Obtener agenda simple de un doctor (solo franjas)
    @GetMapping("/doctor/{doctorId}/simple")
    @ResponseBody
    public List<AgendaDTO> obtenerAgendaSimplePorDoctor(@PathVariable Long doctorId) {
        return agendaClient.findByDoctorSimple(doctorId);
    }

    // Mostrar formulario para que el doctor registre horario
    @GetMapping("/crear")
    public String mostrarFormularioCrearAgenda(Model model) {
        AgendaRequestDTO dto = new AgendaRequestDTO();
        dto.setDisponible(true);
        dto.setMedicoId(9L);
        model.addAttribute("agenda", dto);
        return "usuario/doctor/AgendarDoctor";
    }

    // Guardar la agenda del doctor
    @PostMapping("/crear")
    public String crearAgendaSubmit(@ModelAttribute AgendaRequestDTO agendaRequestDTO) {
        agendaClient.crearAgenda(agendaRequestDTO);
        return "redirect:/front/agenda/crear?exito";
    }

    // Bloquear un horario
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

        // Aquí hacemos la conversión correcta
        dto.setFecha(agenda.getFecha().format(dateFormatter));
        dto.setHoraInicio(agenda.getHoraInicio().format(timeFormatter));
        dto.setHoraFin(agenda.getHoraFin().format(timeFormatter));

        dto.setDisponible(false);

        return agendaClient.actualizar(id, dto);
    }


}
