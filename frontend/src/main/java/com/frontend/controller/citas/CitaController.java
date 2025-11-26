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

/**
 * Controlador para la gestión de citas en el frontend.
 * Permite a pacientes y doctores crear, consultar y cancelar citas.
 * Interactúa con los microservicios de usuarios y citas mediante FeignClient.
 */
@Controller
@RequestMapping("/front/citas")
public class CitaController {

    @Autowired
    private UsuariosClient usuariosClient;

    @Autowired
    private CitasClient citasClient;

    // ------------------- FORMULARIO AGENDAR CITA -------------------

    /**
     * Muestra el formulario para que un paciente agende una cita.
     *
     * @param model   Modelo Thymeleaf para pasar atributos a la vista
     * @param session Sesión HTTP para obtener el ID del usuario logueado
     * @return Nombre de la plantilla Thymeleaf "agendar-cita" o redirección a login
     */
    @GetMapping("/agendar")
    public String agendar(Model model, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        model.addAttribute("pacienteId", usuarioId);
        return "agendar-cita";
    }

    // ------------------- CONSULTA DE DOCTORES -------------------

    /**
     * Obtiene los doctores según la especialidad indicada.
     *
     * @param especialidad Nombre de la especialidad
     * @return Lista de DoctorDTO; vacía si no se especifica la especialidad
     */
    @GetMapping("/doctores")
    @ResponseBody
    public List<DoctorDTO> listarDoctores(@RequestParam String especialidad) {
        if (especialidad == null || especialidad.isEmpty()) {
            return Collections.emptyList();
        }
        return usuariosClient.buscarDoctoresPorEspecialidad(especialidad);
    }

    // ------------------- CREACIÓN DE CITA -------------------

    /**
     * Crea una cita para un paciente con notificación por correo.
     * Se obtienen los datos del paciente y del doctor, y se envía al microservicio de citas.
     *
     * @param cita    DTO con información de la cita
     * @param session Sesión HTTP para obtener ID del paciente
     * @return ResponseEntity con la cita creada o mensaje de error
     */
    @PostMapping("/agendar")
    @ResponseBody
    public ResponseEntity<?> crearCita(@RequestBody CitaRequestDTO cita, HttpSession session) {
        try {
            Long usuarioId = (Long) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Sesión expirada. Por favor inicia sesión nuevamente.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            UsuarioDTO usuario = usuariosClient.findById(usuarioId);
            if (usuario == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "No se encontró información del usuario");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            cita.setEmailPaciente(usuario.getCorreo());
            cita.setNombrePaciente(usuario.getNombre() + " " + usuario.getApellido());

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

    // ------------------- CONSULTA DE CITAS PACIENTE -------------------

    /**
     * Muestra todas las citas de un paciente logueado.
     *
     * @param model   Modelo Thymeleaf
     * @param session Sesión HTTP para obtener ID del paciente
     * @return Plantilla "mis-citas" o redirección a login
     */
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

    // ------------------- CANCELACIÓN DE CITA -------------------

    /**
     * Cancela una cita del paciente con validación de sesión y notificación.
     *
     * @param id      ID de la cita
     * @param session Sesión HTTP para validar paciente
     * @return ResponseEntity con mensaje de éxito o error
     */
    @PostMapping("/{id}/cancelar")
    @ResponseBody
    public ResponseEntity<?> cancelarCita(@PathVariable Long id, HttpSession session) {
        try {
            Long usuarioId = (Long) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Sesión expirada");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

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

    // ------------------- CONSULTA DE CITAS DOCTOR -------------------

    /**
     * Muestra todas las citas asignadas a un doctor logueado.
     *
     * @param model   Modelo Thymeleaf
     * @param session Sesión HTTP para obtener ID del doctor
     * @return Plantilla "mis-citas-doctor" o redirección a login
     */
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
