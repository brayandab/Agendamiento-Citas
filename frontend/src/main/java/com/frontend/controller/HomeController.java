package com.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador principal para las páginas de inicio de los distintos roles del sistema.
 * Redirige a la vista correspondiente según el rol del usuario logueado: ADMIN, DOCTOR o PACIENTE.
 * Valida que exista una sesión activa y obtiene la información del usuario desde HttpSession.
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    /**
     * Muestra el panel de inicio del administrador.
     * Verifica si hay un usuario logueado; si no, redirige al login.
     *
     * @param model   Modelo Thymeleaf para pasar atributos a la vista
     * @param session Sesión HTTP para obtener el usuario logueado
     * @return Nombre de la plantilla "home-admin" o redirección a login
     */
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

    /**
     * Muestra el panel de inicio del doctor.
     * Verifica si hay un usuario logueado; si no, redirige al login.
     *
     * @param model   Modelo Thymeleaf para pasar atributos a la vista
     * @param session Sesión HTTP para obtener el usuario logueado
     * @return Nombre de la plantilla "home-doctor" o redirección a login
     */
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

    /**
     * Muestra el panel de inicio del paciente.
     * Verifica si hay un usuario logueado; si no, redirige al login.
     *
     * @param model   Modelo Thymeleaf para pasar atributos a la vista
     * @param session Sesión HTTP para obtener el usuario logueado
     * @return Nombre de la plantilla "home-paciente" o redirección a login
     */
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
