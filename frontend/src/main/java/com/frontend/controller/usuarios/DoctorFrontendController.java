package com.frontend.controller.usuarios;

import com.frontend.clients.UsuariosClient;
import com.frontend.clients.AgendaClient;
import com.frontend.dtos.response.usuarios.DoctorDTO;
import com.frontend.dtos.request.usuarios.DoctorRequestDTO;
import com.frontend.dtos.request.agenda.AgendaRequestDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/front/doctores")
public class DoctorFrontendController {

    @Autowired
    private UsuariosClient client;

    @Autowired
    private AgendaClient agendaClient;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("doctores", client.listarDoctores());
        return "usuario/doctor/lista";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(@RequestParam Long usuarioId, Model model) {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setUsuarioId(usuarioId);

        model.addAttribute("doctor", dto);
        return "usuario/doctor/CrearDoctor";
    }

    @PostMapping("/crear")
    public String crearDoctor(@ModelAttribute DoctorRequestDTO dto) {
        client.crearDoctor(dto);
        return "login";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        DoctorDTO doctor = client.buscarDoctorPorId(id);
        model.addAttribute("doctor", doctor);
        return "usuario/doctor/detalle";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        client.eliminarDoctor(id);
        return "redirect:/front/doctores";
    }

    @GetMapping("/disponibilidad")
    public String disponibilidadFormulario(HttpSession session, Model model) {

        Long doctorId = (Long) session.getAttribute("doctorId");

        if (doctorId == null) {
            return "redirect:/login";
        }

        model.addAttribute("doctorId", doctorId);
        return "usuario/doctor/AgendarDoctor";
    }

    @PostMapping("/disponibilidad/guardar")
    public String guardarDisponibilidad(
            @RequestParam String fecha,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            HttpSession session
    ) {
        Long doctorId = (Long) session.getAttribute("doctorId");

        if (doctorId == null) {
            return "redirect:/login";
        }

        AgendaRequestDTO dto = new AgendaRequestDTO();
        dto.setMedicoId(doctorId);
        dto.setFecha(fecha);
        dto.setHoraInicio(horaInicio);
        dto.setHoraFin(horaFin);
        dto.setDisponible(true);

        agendaClient.crearFranjas(dto);

        return "redirect:/front/doctores/disponibilidad?success";
    }
}
