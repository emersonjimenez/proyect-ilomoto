package com.motos.ilomoto.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
public class CategoryViewController {
    @RequestMapping(value = "/categories")
    public String categories(Model model) {
        initializeSessionAttributes(model);
        return "/category/categories";
    }

    @RequestMapping(value = "/new-category")
    public String newCategory(Model model) {
        initializeSessionAttributes(model);
        return "/category/new-category";
    }

    @RequestMapping(value = "/update-category/{id}")
    public String updateCategory(Model model) {
        initializeSessionAttributes(model);
        return "/category/update-category";
    }

    private void initializeSessionAttributes(Model model) {
        // Añadir atributos al modelo
        model.addAttribute("username", "Usuario");
        model.addAttribute("role", "Administrador");
        model.addAttribute("today", LocalDate.now());
    }
}
