package service.auth.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface IGatewayRoutingService {
    ResponseEntity<?> forwardRequest(HttpServletRequest req, String address);
}