package com.sgturnos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/sgturnos/dashboard_admin")
    public String dashboardAdmin() {
        return "login/dashboard_admin"; // aquí se llama al archivo HTML correspondiente
    }
}