package com.frontend.controller;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.request.LoginRequestDTO;
import com.frontend.dtos.request.usuarios.UsuarioRequestDTO;
import com.frontend.dtos.response.LoginResponseDTO;
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
    public String login(@ModelAttribute LoginRequestDTO loginRequestDTO, Model model) {
        try {
            LoginResponseDTO response = usuariosClient.login(loginRequestDTO);

            if (response == null || response.getRol() == null) {
                model.addAttribute("error", "Credenciales inválidas");
                return "login";
            }

            String rol = response.getRol().trim().toUpperCase();

            switch (rol) {
                case "ADMIN":
                    return "redirect:/home/admin?rol=ADMIN";
                case "DOCTOR":
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