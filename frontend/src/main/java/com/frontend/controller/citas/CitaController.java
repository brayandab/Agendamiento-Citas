package com.frontend.controller.citas;

import com.frontend.clients.CitasClient;
import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.request.cita.CitaRequestDTO;
import com.frontend.dtos.response.citas.CitaDTO;
import com.frontend.dtos.response.usuarios.DoctorDTO;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import feign.FeignException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/front/citas")
public class CitaController {

    @Autowired
    private UsuariosClient usuariosClient;

    @Autowired
    private CitasClient citasClient;

    // ============================================================
    //  AGENDAR CITA - FORMULARIO
    // ============================================================
    @GetMapping("/agendar")
    public String agendar(Model model, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        model.addAttribute("pacienteId", usuarioId);
        return "agendar-cita";
    }

    // ============================================================
    // OBTENER DOCTORES POR ESPECIALIDAD
    // ============================================================
    @GetMapping("/doctores")
    @ResponseBody
    public List<DoctorDTO> listarDoctores(@RequestParam String especialidad) {
        if (especialidad == null || especialidad.isEmpty()) {
            return Collections.emptyList();
        }

        List<DoctorDTO> doctores = usuariosClient.buscarDoctoresPorEspecialidad(especialidad);
        return doctores;
    }

    // ============================================================
    // 🟢 CREAR CITA (Ahora el backend maneja el bloqueo)
    // ============================================================
    @PostMapping("/agendar")
    @ResponseBody
    public ResponseEntity<?> crearCita(@RequestBody CitaRequestDTO cita) {
        try {
            // El microservicio de CITAS ahora se encarga de bloquear la agenda
            CitaDTO citaCreada = citasClient.crearCita(cita);
            return ResponseEntity.ok(citaCreada);

        } catch (FeignException e) {
            // Capturar errores del microservicio de citas
            Map<String, String> error = new HashMap<>();

            if (e.status() == 409) {
                error.put("mensaje", "Este horario ya no está disponible");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            } else if (e.status() == 404) {
                error.put("mensaje", "Horario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } else {
                error.put("mensaje", "Error al crear la cita");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }
    }

    // ============================================================
    //  PACIENTE — VER SUS PROPIAS CITAS
    // ============================================================
    @GetMapping("/mis-citas")
    public String verMisCitasPaciente(Model model, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        List<CitaDTO> citas = citasClient.citasPorPaciente(usuarioId);
        model.addAttribute("citas", citas);

        return "usuario/paciente/mis-citas";
    }

    // ============================================================
    // 🟧 DOCTOR — VER SUS CITAS
    // ============================================================
    @GetMapping("/mis-citas-doctor")
    public String verMisCitasDoctor(Model model, HttpSession session) {
        Long doctorId = (Long) session.getAttribute("doctorId");

        if (doctorId == null) {
            return "redirect:/login";
        }

        List<CitaDTO> citas = citasClient.buscarPorMedico(doctorId);
        model.addAttribute("citas", citas);

        return "usuario/doctor/mis-citas-doctor";
    }
}