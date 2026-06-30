package com.example.ecscicd;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "Eugene Anokye");
        model.addAttribute("lab", "ECS CI/CD Lab – BEM13");
        model.addAttribute("environment", "Production");
        model.addAttribute("platform", "Amazon ECS Fargate");
        model.addAttribute("deployment", "Blue/Green via CodeDeploy");
        model.addAttribute("javaVersion", "Java " + System.getProperty("java.version"));
        model.addAttribute("serverTime",
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));

        String hostname = System.getenv("HOSTNAME");
        if (hostname == null || hostname.isBlank()) {
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (Exception e) {
                hostname = "unknown";
            }
        }
        model.addAttribute("hostname", hostname);

        return "index";
    }
}
