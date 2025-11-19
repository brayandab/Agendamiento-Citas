package com.frontend.controller.usuarios;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.DoctorDTO;
import com.frontend.dtos.request.DoctorRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/front/doctores")
public class DoctorFrontendController {

    @Autowired
    private UsuariosClient client;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("doctores", client.listarDoctores());
        // Debes crear esta vista: templates/usuario/doctor/lista.html
        return "usuario/doctor/lista";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(@RequestParam Long usuarioId, Model model) {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setUsuarioId(usuarioId);

        model.addAttribute("doctor", dto);
        return "doctor/crear";
    }

    @PostMapping("/crear")
    public String crearDoctor(@ModelAttribute DoctorRequestDTO dto) {

        client.crearDoctor(dto);
        return "redirect:/front/doctores";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        DoctorDTO doctor = client.buscarDoctorPorId(id);
        model.addAttribute("doctor", doctor);
        // Debes crear esta vista: templates/usuario/doctor/detalle.html
        return "usuario/doctor/detalle";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        client.eliminarDoctor(id);
        return "redirect:/front/doctores";
    }
}
