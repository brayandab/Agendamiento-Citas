package com.frontend.controller;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.request.LoginRequestDTO;
import com.frontend.dtos.request.usuarios.UsuarioRequestDTO;
import com.frontend.dtos.response.LoginResponseDTO;
import com.frontend.dtos.response.usuarios.DoctorDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado de gestionar el inicio de sesión de los usuarios.
 * Permite mostrar el formulario de login y procesar las credenciales ingresadas.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuariosClient usuariosClient;

    /**
     * Muestra la página de login al usuario.
     *
     * @param model Model de Spring para enviar atributos a la vista
     * @return Nombre de la plantilla Thymeleaf "login"
     */
    @GetMapping
    public String loginPage(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDTO());
        return "login";
    }

    /**
     * Procesa las credenciales ingresadas por el usuario.
     * Valida el rol y redirige al panel correspondiente según el tipo de usuario.
     * Además, guarda información del usuario en la sesión.
     *
     * @param loginRequestDTO DTO con correo y contraseña ingresados
     * @param model           Model de Spring para enviar atributos a la vista
     * @param session         Sesión HTTP para almacenar información del usuario
     * @return Redirección a la página correspondiente o recarga del login con mensaje de error
     */
    @PostMapping
    public String login(@ModelAttribute LoginRequestDTO loginRequestDTO,
                        Model model, HttpSession session) {
        try {
            LoginResponseDTO response = usuariosClient.login(loginRequestDTO);

            if (response == null || response.getRol() == null) {
                model.addAttribute("error", "Credenciales inválidas");
                model.addAttribute("usuario", new UsuarioRequestDTO());
                return "login";
            }

            String rol = response.getRol().trim().toUpperCase();

            // Guardamos usuarioId en sesión
            // Y tambien guardamos el usuarioLogueado para modificaciones del perfil y vizualizar el nombre

            session.setAttribute("usuarioId", response.getId());
            session.setAttribute("usuarioLogueado", response);

            switch (rol) {
                case "ADMIN":
                    return "redirect:/home/admin?rol=ADMIN";

                case "DOCTOR":
                    DoctorDTO doctor = usuariosClient.buscarDoctorPorUsuarioId(response.getId());
                    if (doctor != null) {
                        session.setAttribute("doctorId", doctor.getId());
                    } else {
                        model.addAttribute("error", "No se encontró doctor asociado");
                        model.addAttribute("usuario", new UsuarioRequestDTO());
                        return "login";
                    }
                    return "redirect:/home/doctor?rol=DOCTOR";

                case "PACIENTE":
                    return "redirect:/home/paciente?rol=PACIENTE";

                default:
                    model.addAttribute("error", "Rol inválido");
                    model.addAttribute("usuario", new UsuarioRequestDTO());
                    return "login";
            }

        } catch (Exception e) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            model.addAttribute("usuario", new UsuarioRequestDTO());
            return "login";
        }
    }
}
