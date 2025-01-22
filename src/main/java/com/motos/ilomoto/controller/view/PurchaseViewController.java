package com.motos.ilomoto.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;

@Controller
public class PurchaseViewController {
    @RequestMapping(value = "/purchase-new/",method = RequestMethod.GET)
    public String newPurchase(Model model) {
        model.addAttribute("today", LocalDate.now());
        return "purchase/purchase-new";
    }

    @RequestMapping(value = "/purchase-details/",method = RequestMethod.GET)
    public String detailsPurchase(Model model) {
        model.addAttribute("today", LocalDate.now());
        return "purchase/purchase-details";
    }
}
