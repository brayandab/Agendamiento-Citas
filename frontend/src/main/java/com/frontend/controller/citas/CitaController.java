package com.frontend.controller.citas;

import com.frontend.clients.CitasClient;
import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.request.cita.CitaRequestDTO;
import com.frontend.dtos.response.usuarios.DoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/front/citas")
public class CitaController {

    @Autowired
    private UsuariosClient usuariosClient;

    @Autowired
    private CitasClient citasClient;

    // Página de agendar cita
    @GetMapping("/agendar")
    public String agendar() {
        return "agendar-cita";
    }

    // Endpoint para obtener doctores por especialidad
    @GetMapping("/doctores")
    @ResponseBody
    public List<DoctorDTO> listarDoctores(@RequestParam String especialidad) {
        if (especialidad == null || especialidad.isEmpty()) {
            return Collections.emptyList(); // No devuelve nada si no hay especialidad
        }

        List<DoctorDTO> doctores = usuariosClient.buscarDoctoresPorEspecialidad(especialidad);

        // Imprime para depuración
        doctores.forEach(doc -> {
            System.out.println("Doctor: " + doc.getUsuario().getNombre() + " " +
                    doc.getUsuario().getApellido() + " - " + doc.getEspecialidad() +
                    " - " + doc.getHorarioAtencion());
        });

        return doctores;
    }

    /*
    // Método de agenda deshabilitado por ahora
    @GetMapping("/agenda")
    @ResponseBody
    public List<String> horas(@RequestParam Long doctorId, @RequestParam String fecha) {
        return agendaClient.obtenerAgenda(doctorId, fecha);
    }
    */

    // Crear cita
    @PostMapping("/agendar")
    public String crearCita(@ModelAttribute CitaRequestDTO cita) {
        citasClient.crearCita(cita);
        return "redirect:/home-paciente";
    }
}
