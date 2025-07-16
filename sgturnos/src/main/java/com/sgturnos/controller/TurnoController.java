package com.sgturnos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/turnos")
public class TurnoController {

    @GetMapping
    public String showTurnosPage() {
        return "turnos/planificar"; // Nombre del archivo: planificar.html
    }
}