package com.frontend.controller.usuarios;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.UsuarioDTO;
import com.frontend.dtos.request.UsuarioRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/front/usuarios")
public class UsuarioFrontendController {

    @Autowired
    private UsuariosClient client;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", client.listarUsuarios());
        return "usuarios/lista";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDTO());
        return "usuarios/crear";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute UsuarioRequestDTO dto) {
        // 1. Crear usuario primero
        client.crearUsuario(dto);

        // 2. Según el rol, redirigir al formulario correcto
        if (dto.getRol().equalsIgnoreCase("PACIENTE")) {
            return "redirect:/front/pacientes/crear?correo=" + dto.getCorreo();
        }

        if (dto.getRol().equalsIgnoreCase("DOCTOR")) {
            return "redirect:/front/doctores/crear?correo=" + dto.getCorreo();
        }

        return "redirect:/front/usuarios"; // fallback
    }


    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        UsuarioDTO usuario = client.findById(id);
        model.addAttribute("usuario", usuario);
        return "usuarios/detalle";
    }

    /*@GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        client.eliminarUsuario(id);
        return "redirect:/front/usuarios";
    }*/
}
