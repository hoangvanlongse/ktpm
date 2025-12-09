package service.auth.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface IGatewayProcessingService {
    ResponseEntity<?> processRequest(HttpServletRequest req);
}
