package com.motos.ilomoto.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
public class EmployeeViewController {
    @RequestMapping(value = "/employees")
    public String employees(Model model) {
        initializeSessionAttributes(model);
        return "/employee/employees";
    }

    @RequestMapping(value = "/new-employee")
    public String newEmployee(Model model) {
        initializeSessionAttributes(model);
        return "/employee/new-employee";
    }

    @RequestMapping(value = "/update-employee/{id}")
    public String updateEmployee(Model model) {
        initializeSessionAttributes(model);
        return "/employee/update-employee";
    }

    private void initializeSessionAttributes(Model model) {
        // Añadir atributos al modelo
        model.addAttribute("username", "Usuario");
        model.addAttribute("role", "Administrador");
        model.addAttribute("today", LocalDate.now());
    }
}
