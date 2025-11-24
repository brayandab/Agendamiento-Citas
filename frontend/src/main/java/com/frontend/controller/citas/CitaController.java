package com.frontend.controller.citas;

import com.frontend.clients.CitasClient;
import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.request.cita.CitaRequestDTO;
import com.frontend.dtos.response.citas.CitaDTO;
import com.frontend.dtos.response.usuarios.DoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // Página del formulario de agendar cita
    @GetMapping("/agendar")
    public String agendar(Model model, @SessionAttribute("usuarioId") Long usuarioId) {
        // Pasamos el pacienteId al frontend
        model.addAttribute("pacienteId", usuarioId);
        return "agendar-cita";
    }

    // 🔹 Obtener doctores por especialidad (LLAMADO DESDE EL HTML)
    @GetMapping("/doctores")
    @ResponseBody
    public List<DoctorDTO> listarDoctores(@RequestParam String especialidad) {
        if (especialidad == null || especialidad.isEmpty()) {
            return Collections.emptyList();
        }

        List<DoctorDTO> doctores = usuariosClient.buscarDoctoresPorEspecialidad(especialidad);

        doctores.forEach(doc -> {
            if (doc.getUsuario() != null) {
                System.out.println("Doctor: " + doc.getUsuario().getNombre() + " "
                        + doc.getUsuario().getApellido() + " - " + doc.getEspecialidad());
            }
        });

        return doctores;
    }

    // 🔹 Crear cita (Recibiendo JSON desde el frontend)
    @PostMapping("/agendar")
    @ResponseBody
    public CitaDTO crearCita(@RequestBody CitaRequestDTO cita) {
        // Llamada al microservicio de citas
        CitaDTO citaCreada = citasClient.crearCita(cita);
        System.out.println("Cita creada: " + citaCreada);
        return citaCreada; // Devuelve la cita creada como JSON al frontend
    }

    @GetMapping("/{doctorId}/citas")
    public String verCitas(@PathVariable Long doctorId, Model model) {
        List<CitaDTO> citas = citasClient.citasPorDoctor(doctorId);
        model.addAttribute("citas", citas);
        return "usuario/doctor/CitasAgendadas"; // Nombre del HTML Thymeleaf
    }

}
