

package com.sgturnos.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
     @GetMapping("/dashboard") // The URL your users will access for the dashboard
    public String showDashboard() {
        return "dashboard"; // This will resolve to src/main/resources/templates/dashboard.html
    }
    
}
