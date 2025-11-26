package com.frontend.controller.usuarios;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.response.usuarios.UsuarioDTO;
import com.frontend.dtos.request.usuarios.UsuarioRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/front/usuarios")
public class UsuarioFrontendController {

    @Autowired
    private UsuariosClient client;

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDTO());
        return "usuario/CrearUsuario";
    }

    @GetMapping("")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", client.listarUsuarios());
        return "usuario/admin/Gestion-Usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        var usuario = client.findById(id);

        model.addAttribute("usuarios", client.listarUsuarios());
        model.addAttribute("usuarioEditar", usuario);
        model.addAttribute("mostrarSidebar", true);
        model.addAttribute("roles", List.of("ADMIN", "DOCTOR", "PACIENTE")); // ← Roles disponibles

        return "usuario/admin/Gestion-Usuarios";
    }


    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
                                    @ModelAttribute UsuarioDTO usuario) {

        client.actualizar(id, usuario);
        return "redirect:/front/usuarios";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute UsuarioRequestDTO dto) {

        UsuarioDTO usuarioCreado = client.crearUsuario(dto);

        Long idUsuario = usuarioCreado.getId();
        String rol = usuarioCreado.getRol().toUpperCase();

        if (rol.equals("PACIENTE")) {
            return "redirect:/front/pacientes/crear?usuarioId=" + idUsuario;
        }
        if (rol.equals("DOCTOR")) {
            return "redirect:/front/doctores/crear?usuarioId=" + idUsuario;
        }

        return "redirect:/front/usuarios";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        UsuarioDTO usuario = client.findById(id);
        model.addAttribute("usuario", usuario);
        return "usuario/DetalleUsuario";
    }
}
