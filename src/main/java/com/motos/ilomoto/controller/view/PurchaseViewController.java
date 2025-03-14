package com.motos.ilomoto.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
public class PurchaseViewController {
    @RequestMapping(value = "/new-purchase")
    public String newPurchase(Model model) {
        initializeSessionAttributes(model);
        return "purchase/new-purchase";
    }

    @RequestMapping(value = "/purchase-details")
    public String detailsPurchase(Model model) {
        initializeSessionAttributes(model);
        return "purchase/purchase-details";
    }

    private void initializeSessionAttributes(Model model) {
        // Añadir atributos al modelo
        model.addAttribute("username", "Usuario");
        model.addAttribute("role", "Administrador");
        model.addAttribute("today", LocalDate.now());
    }
}
