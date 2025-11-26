package com.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/admin")
    public String adminHome(Model model, HttpSession session) {

        Object usuario = session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", "ADMIN");
        model.addAttribute("panelTitulo", "Panel Administrador");
        return "logueo/home-admin";
    }

    @GetMapping("/doctor")
    public String doctorHome(Model model, HttpSession session) {

        Object usuario = session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", "DOCTOR");
        model.addAttribute("panelTitulo", "Panel Doctor");
        return "logueo/home-doctor";
    }

    @GetMapping("/paciente")
    public String homePaciente(Model model, HttpSession session) {

        Object usuario = session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", "PACIENTE");
        model.addAttribute("panelTitulo", "Panel Paciente");

        return "logueo/home-paciente";
    }

}