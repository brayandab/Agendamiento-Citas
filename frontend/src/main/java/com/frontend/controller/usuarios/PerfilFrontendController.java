package com.frontend.controller.usuarios;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.response.LoginResponseDTO;
import com.frontend.dtos.response.usuarios.UsuarioDTO;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/front/usuario")
public class PerfilFrontendController {

    private final UsuariosClient usuariosClient;

    public PerfilFrontendController(UsuariosClient usuariosClient) {
        this.usuariosClient = usuariosClient;
    }

    @GetMapping("/perfil")
    public String verPerfil(Model model, HttpSession session) {

        LoginResponseDTO usuarioLogueado =
                (LoginResponseDTO) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/login";
        }

        // Llamada al microservicio para obtener info completa
        UsuarioDTO usuario = usuariosClient.findById(usuarioLogueado.getId());

        model.addAttribute("usuario", usuario);

        return "usuario/perfil";
    }

    @PostMapping("/perfil/editar")
    public String editarPerfil(@ModelAttribute UsuarioDTO usuarioDTO,
                               HttpSession session) {

        LoginResponseDTO usuarioLogueado =
                (LoginResponseDTO) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/login";
        }

        // Forzar ID correcto
        usuarioDTO.setId(usuarioLogueado.getId());

        // Evitar que el correo llegue null
        if (usuarioDTO.getCorreo() == null || usuarioDTO.getCorreo().isEmpty()) {
            usuarioDTO.setCorreo(usuarioLogueado.getCorreo());
        }

        usuariosClient.actualizar(usuarioDTO.getId(), usuarioDTO);

        // Actualizar sesión
        usuarioLogueado.setNombre(usuarioDTO.getNombre());
        usuarioLogueado.setApellido(usuarioDTO.getApellido());
        usuarioLogueado.setCorreo(usuarioDTO.getCorreo());

        session.setAttribute("usuarioLogueado", usuarioLogueado);

        return "redirect:/front/usuario/perfil?success=true";
    }

}