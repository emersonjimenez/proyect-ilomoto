package com.motos.ilomoto.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
public class AuthViewController {
    /* Direccionar al Inicio */
    @RequestMapping(value = "/")
    public String index(Model model){
        initializeSessionAttributes(model);
        return "index";
    }
    /* Direccionar al apartado Login */
    @GetMapping("/authentication/login")
    public String login() {
        return "authentication/login";
    }

    @RequestMapping(value = "/error/pages-500")
    public String page500(Model model){
        initializeSessionAttributes(model);
        return "error/pages-500";
    }

    private void initializeSessionAttributes(Model model) {
        // Añadir atributos al modelo
        model.addAttribute("username", "Usuario");
        model.addAttribute("role", "Administrador");
        model.addAttribute("today", LocalDate.now());
    }
}
