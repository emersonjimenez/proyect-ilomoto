package com.motos.ilomoto.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
public class BrandViewController {
    @RequestMapping(value = "/brands")
    public String brands(Model model) {
        initializeSessionAttributes(model);
        return "/brand/brands";
    }

    @RequestMapping(value = "/new-brand")
    public String newBrand(Model model) {
        initializeSessionAttributes(model);
        return "/brand/new-brand";
    }

    @RequestMapping(value = "/update-brand/{id}")
    public String updateBrand(Model model) {
        initializeSessionAttributes(model);
        return "/brand/update-brand";
    }

    private void initializeSessionAttributes(Model model) {
        // Añadir atributos al modelo
        model.addAttribute("username", "Usuario");
        model.addAttribute("role", "Administrador");
        model.addAttribute("today", LocalDate.now());
    }
}
