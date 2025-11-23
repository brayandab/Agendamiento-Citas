package com.frontend.controller.citas;

import com.frontend.clients.AgendaClient;
import com.frontend.clients.CitasClient;
import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.request.cita.CitaRequestDTO;
import com.frontend.dtos.response.usuarios.DoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/front/citas")
public class CitaController {
    @Autowired
    private UsuariosClient usuariosClient;

    @Autowired
    private AgendaClient agendaClient;

    @Autowired
    private CitasClient citasClient;


    @GetMapping("/agendar")
    public String agendar() {
        return "agendar-cita";
    }

    @GetMapping("/doctores")
    @ResponseBody
    public List<DoctorDTO> listarDoctores(@RequestParam String especialidad) {
        return usuariosClient.buscarDoctoresPorEspecialidad(especialidad);
    }

    /*@GetMapping("/agenda")
    @ResponseBody
    public List<String> horas(@RequestParam Long doctorId, @RequestParam String fecha) {
        return agendaClient.obtenerAgenda(doctorId, fecha);
    }*/

    @PostMapping("/agendar")
    public String crearCita(@ModelAttribute CitaRequestDTO cita) {
        citasClient.crearCita(cita);
        return "redirect:/home-paciente";
    }
}

