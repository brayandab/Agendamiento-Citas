package com.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeFrontendController {

    @GetMapping("/")
    public String home() {
        return "home"; // Carga home.html
    }
}
