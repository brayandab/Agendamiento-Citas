package com.frontend.controller.usuarios;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.response.usuarios.UsuarioDTO;
import com.frontend.dtos.request.usuarios.UsuarioRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/front/usuarios")
public class UsuarioFrontendController {

    @Autowired
    private UsuariosClient client;

    /*@GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", client.listarUsuarios());
        return "usuario/ListaUsuarios";
    }*/

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDTO());
        return "usuario/CrearUsuario"; // EXACTO A TU ESTRUCTURA
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute UsuarioRequestDTO dto) {

        UsuarioDTO usuarioCreado = client.crearUsuario(dto);

        // Guardar id y correo en redirect
        Long idUsuario = usuarioCreado.getId();
        String correo = usuarioCreado.getCorreo();
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
