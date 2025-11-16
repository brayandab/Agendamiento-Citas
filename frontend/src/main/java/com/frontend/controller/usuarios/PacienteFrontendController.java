package com.frontend.controller.usuarios;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.PacienteDTO;
import com.frontend.dtos.request.PacienteRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/front/pacientes")
public class PacienteFrontendController {

    @Autowired
    private UsuariosClient client;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pacientes", client.listarPacientes());
        return "pacientes/lista";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(@RequestParam String correo, Model model) {

        // Buscar el usuario ya creado por correo
        var usuario = client.buscarPorCorreo(correo);

        PacienteRequestDTO paciente = new PacienteRequestDTO();
        paciente.setUsuario(usuario); // IMPORTANTÍSIMO

        model.addAttribute("paciente", paciente);

        return "pacientes/crear";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute PacienteRequestDTO dto) {
        client.crearPaciente(dto);
        return "redirect:/front/pacientes";
    }


    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        PacienteDTO paciente = client.buscarPacientePorId(id);
        model.addAttribute("paciente", paciente);
        return "pacientes/detalle";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        client.eliminarPaciente(id);
        return "redirect:/front/pacientes";
    }
}
