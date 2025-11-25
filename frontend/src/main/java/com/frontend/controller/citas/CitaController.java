package com.frontend.controller.citas;

import com.frontend.clients.CitasClient;
import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.request.cita.CitaRequestDTO;
import com.frontend.dtos.response.citas.CitaDTO;
import com.frontend.dtos.response.usuarios.DoctorDTO;
import com.frontend.dtos.response.usuarios.UsuarioDTO;

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


    // AGENDAR CITA - FORMULARIO

    @GetMapping("/agendar")
    public String agendar(Model model, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        model.addAttribute("pacienteId", usuarioId);
        return "agendar-cita";
    }


    //OBTENER DOCTORES POR ESPECIALIDAD

    @GetMapping("/doctores")
    @ResponseBody
    public List<DoctorDTO> listarDoctores(@RequestParam String especialidad) {
        if (especialidad == null || especialidad.isEmpty()) {
            return Collections.emptyList();
        }

        List<DoctorDTO> doctores = usuariosClient.buscarDoctoresPorEspecialidad(especialidad);
        return doctores;
    }


    // CREAR CITA CON NOTIFICACIÓN (MEJORADO)

    @PostMapping("/agendar")
    @ResponseBody
    public ResponseEntity<?> crearCita(@RequestBody CitaRequestDTO cita, HttpSession session) {
        try {
            // Obtener ID del usuario desde la sesión
            Long usuarioId = (Long) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Sesión expirada. Por favor inicia sesión nuevamente.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // Obtener datos completos del paciente desde el microservicio
            UsuarioDTO usuario = usuariosClient.findById(usuarioId);

            if (usuario == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "No se encontró información del usuario");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            //Establecer datos del paciente en el DTO
            cita.setEmailPaciente(usuario.getCorreo());
            cita.setNombrePaciente(usuario.getNombre() + " " + usuario.getApellido());

            // Obtener nombre del doctor
            try {
                DoctorDTO doctor = usuariosClient.buscarDoctorPorId(cita.getMedicoId());
                if (doctor != null && doctor.getUsuario() != null) {
                    String nombreDoctor = doctor.getUsuario().getNombre() + " " +
                            doctor.getUsuario().getApellido();
                    cita.setNombreDoctor(nombreDoctor);
                }
            } catch (Exception e) {
                System.err.println("Error al obtener nombre del doctor: " + e.getMessage());

            }

            // Crear la cita (el backend enviará la notificación)
            CitaDTO citaCreada = citasClient.crearCita(cita);
            return ResponseEntity.ok(citaCreada);

        } catch (FeignException e) {
            Map<String, String> error = new HashMap<>();

            if (e.status() == 409) {
                error.put("mensaje", "Este horario ya no está disponible");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            } else if (e.status() == 404) {
                error.put("mensaje", "Horario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } else {
                error.put("mensaje", "Error al crear la cita: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    // PACIENTE — VER SUS PROPIAS CITAS

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


    // CANCELAR CITA CON VALIDACIÓN Y NOTIFICACIÓN

    @PostMapping("/{id}/cancelar")
    @ResponseBody
    public ResponseEntity<?> cancelarCita(@PathVariable Long id, HttpSession session) {
        try {
            // Verificar sesión
            Long usuarioId = (Long) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Sesión expirada");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            //Cancelar la cita (el backend enviará notificación)
            citasClient.cancelarCita(id);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cita cancelada exitosamente. Se ha enviado un correo de confirmación.");
            return ResponseEntity.ok(response);

        } catch (FeignException e) {
            Map<String, String> error = new HashMap<>();

            if (e.status() == 400) {
                error.put("mensaje", "No se puede cancelar la cita. Debe hacerlo con al menos 12 horas de anticipación");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            } else if (e.status() == 404) {
                error.put("mensaje", "Cita no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } else {
                error.put("mensaje", "Error al cancelar la cita");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }
    }


    //DOCTOR — VER SUS CITAS

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