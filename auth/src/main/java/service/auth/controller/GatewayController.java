package service.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import service.auth.service.IGatewayProcessingService;

@RestController
public class GatewayController {

    private final IGatewayProcessingService gatewayProcessingService;

    // Use Constructor Injection (DIP)
    public GatewayController(IGatewayProcessingService gatewayProcessingService) {
        this.gatewayProcessingService = gatewayProcessingService;
    }

    @RequestMapping("/api/**")
    public ResponseEntity<?> handleAllApiRequests(HttpServletRequest req) {
        // Delegate all complex logic to the dedicated service (SRP)
        return gatewayProcessingService.processRequest(req);
    }
}