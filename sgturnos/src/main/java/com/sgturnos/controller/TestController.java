package com.sgturnos.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-password")
    public String testPassword() {
        String passwordInput = "cardona0120";
        String storedPassword = "$2a$10$xDgJXW5m5L5UeK7z6bZVO.9V7vD7Q6b1F0Q9JkXyYH1d2wL1z3JkK"; 
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = passwordEncoder.matches(passwordInput, storedPassword);

        if (isPasswordMatch) {
            return "Contraseña correcta";
        } else {
            return "Contraseña incorrecta";
        }
    }
}
