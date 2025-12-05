package service.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import service.auth.service.GatewayProcessingService;

@RestController
public class GatewayController {

    private final GatewayProcessingService gatewayProcessingService;

    // Use Constructor Injection (DIP)
    public GatewayController(GatewayProcessingService gatewayProcessingService) {
        this.gatewayProcessingService = gatewayProcessingService;
    }

    @RequestMapping("/api/**")
    public ResponseEntity<?> handleAllApiRequests(HttpServletRequest req) {
        // Delegate all complex logic to the dedicated service (SRP)
        return gatewayProcessingService.processRequest(req);
    }
}