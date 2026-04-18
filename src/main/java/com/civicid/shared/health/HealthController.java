package com.civicid.shared.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    
    @GetMapping("/api/health")
    public String home() {
        return "CivicID API is running!";
    }
}
