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

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuariosClient usuariosClient;

    @GetMapping
    public String loginPage(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDTO());
        return "login";
    }

    @PostMapping
    public String login(@ModelAttribute LoginRequestDTO loginRequestDTO,
                        Model model, HttpSession session) {
        try {
            LoginResponseDTO response = usuariosClient.login(loginRequestDTO);

            if (response == null || response.getRol() == null) {
                model.addAttribute("error", "Credenciales inválidas");
                return "login";
            }

            String rol = response.getRol().trim().toUpperCase();

            // Guardamos usuarioId en sesión
            session.setAttribute("usuarioId", response.getId());
            session.setAttribute("usuarioLogueado", response);
            switch (rol) {
                case "ADMIN":
                    return "redirect:/home/admin?rol=ADMIN";

                case "DOCTOR":
                    // Buscar doctor por usuarioId
                    DoctorDTO doctor = usuariosClient.buscarDoctorPorUsuarioId(response.getId());
                    if (doctor != null) {
                        session.setAttribute("doctorId", doctor.getId());
                    } else {
                        model.addAttribute("error", "No se encontró doctor asociado");
                        return "login";
                    }
                    return "redirect:/home/doctor?rol=DOCTOR";

                case "PACIENTE":
                    return "redirect:/home/paciente?rol=PACIENTE";

                default:
                    model.addAttribute("error", "Rol inválido");
                    return "login";
            }

        } catch (Exception e) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "login";
        }
    }
}
