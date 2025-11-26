package com.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página de inicio o landing page de la aplicación.
 * Redirige al usuario a la vista principal de presentación.
 */
@Controller
public class LandingController {

    /**
     * Muestra la página de aterrizaje ("landing") de la aplicación.
     *
     * @return Nombre de la plantilla Thymeleaf "landing"
     */
    @GetMapping("/")
    public String landing() {
        return "landing";
    }
}
