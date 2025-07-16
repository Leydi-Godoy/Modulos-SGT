package com.sgturnos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mallas")
public class MallaController {

    @GetMapping
    public String showMallasPage() {
        return "mallas/revisar"; // Nombre del archivo: revisar.html
    }
}