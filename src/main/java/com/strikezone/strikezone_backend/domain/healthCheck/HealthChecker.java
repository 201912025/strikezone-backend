package com.strikezone.strikezone_backend.domain.healthCheck;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HealthChecker {

    @GetMapping
    public String checkHealth() {
        return "✅ server is running";
    }

    @GetMapping("/api/health")
    public ResponseEntity<String> checkApiHealth() {
        return ResponseEntity.ok("server is running");
    }

    @GetMapping("/health")
    public ResponseEntity<String> checkProbeHealth() {
        return ResponseEntity.ok("server is running");
    }
}
