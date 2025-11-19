package com.frontend.controller.usuarios;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.PacienteDTO;
import com.frontend.dtos.request.PacienteRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/front/pacientes")
public class PacienteFrontendController {

    @Autowired
    private UsuariosClient client;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ------------------------ LISTAR ------------------------
    @GetMapping
    public String listar(Model model) {
        List<PacienteDTO> pacientes = client.listarPacientes();

        // Convertir fechas a formato ISO yyyy-MM-dd para evitar errores
        pacientes.forEach(p -> {
            if (p.getFechaNacimiento() != null) {
                p.setFechaNacimiento(parseFecha(p.getFechaNacimiento()));
            }
        });

        model.addAttribute("pacientes", pacientes);
        return "paciente/lista";
    }

    // ------------------------ FORMULARIO CREAR ------------------------
    @GetMapping("/crear")
    public String mostrarFormulario(@RequestParam Long usuarioId, Model model) {
        PacienteRequestDTO paciente = new PacienteRequestDTO();
        paciente.setUsuarioId(usuarioId);
        model.addAttribute("paciente", paciente);
        return "usuario/paciente/CrearPaciente";
    }

    // ------------------------ CREAR ------------------------
    @PostMapping("/crear")
    public String crear(@ModelAttribute PacienteRequestDTO dto) {
        // No hace falta conversión, backend espera yyyy-MM-dd
        client.crearPaciente(dto);
        return "redirect:/front/pacientes";
    }

    // ------------------------ DETALLE ------------------------
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        PacienteDTO paciente = client.buscarPacientePorId(id);

        if (paciente.getFechaNacimiento() != null) {
            paciente.setFechaNacimiento(parseFecha(paciente.getFechaNacimiento()));
        }

        model.addAttribute("paciente", paciente);
        return "paciente/detalle";
    }

    // ------------------------ ELIMINAR ------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        client.eliminarPaciente(id);
        return "redirect:/front/pacientes";
    }

    // ------------------------ MÉTODO AUXILIAR ------------------------
    private String parseFecha(String fecha) {
        try {
            LocalDate ld = LocalDate.parse(fecha, formatter);
            return ld.toString(); // Devuelve en formato ISO yyyy-MM-dd
        } catch (Exception e) {
            // Si no se puede parsear, devolver la misma fecha
            return fecha;
        }
    }
}
