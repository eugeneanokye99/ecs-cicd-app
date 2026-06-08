package com.example.ecscicd;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "Eugene Anokye");
        model.addAttribute("lab", "ECS CI/CD");
        return "index";
    }
}
