package com.motos.ilomoto.controller.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class CustomErrorController implements ErrorController {

    // Este method manejará todas las rutas de error
    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        // Obtiene el estado HTTP del error
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {// Error 404: Redirige a pages-404.html
                ModelAndView modelAndView = new ModelAndView("error/pages-404");
                modelAndView.setStatus(HttpStatus.NOT_FOUND);
                return modelAndView;
            }
        }

        // Para cualquier otro error: Redirige a la página 500
        ModelAndView modelAndView = new ModelAndView("error/pages-500");
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }
}
