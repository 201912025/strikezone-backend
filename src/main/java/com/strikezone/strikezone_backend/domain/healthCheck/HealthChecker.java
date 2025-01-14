package com.strikezone.strikezone_backend.domain.healthCheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HealthChecker {

    @GetMapping()
    public String checkHealth() {

        return "✅ server is running";

    }
}
