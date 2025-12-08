package service.auth.service;

import java.util.HashMap;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import service.auth.config.IGatewayRouteConfig;

@Service
public class GatewayProcessingService implements IGatewayProcessingService {

    private final IJwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final IGatewayRoutingService gatewayRoutingService;
    private final IGatewayRouteConfig routeConfig;
    private final IAuthorizationService authorizationService;

    // Use Constructor Injection for all dependencies (DIP)
    public GatewayProcessingService(
            IJwtService jwtService,
            IUserInfoService userDetailsService,
            IGatewayRoutingService gatewayRoutingService,
            IGatewayRouteConfig routeConfig,
            IAuthorizationService authorizationService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.gatewayRoutingService = gatewayRoutingService;
        this.routeConfig = routeConfig;
        this.authorizationService = authorizationService;
    }

    @Override
    public ResponseEntity<?> processRequest(HttpServletRequest req) {
        String path = req.getRequestURI();

        // HARD FILTER: ignore /api/auth/**
        if (path.startsWith("/api/auth/")) {
            return null;
        }

        // 1. Find the backend service
        String targetService = routeConfig.resolveService(path);
        if (targetService == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No route defined for " + path);
        }

        // 2. Check public access (Unauthenticated access)
        if (authorizationService.canAccess(req)) {
            return gatewayRoutingService.forwardRequest(req, targetService);
        }

        // 3. Extract and validate JWT (Authenticated access flow)
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return forbiddenResponse("Missing token");
        }

        String token = authHeader.substring(7);
        Claims claims = jwtService.extractAllClaims(token);

        if (claims == null) {
            return forbiddenResponse("Invalid token");
        }

        String username = jwtService.extractUsername(token);

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return forbiddenResponse("User from token not found");
        }

        if (!jwtService.validateToken(token, userDetails)) {
            return forbiddenResponse("Token Expired or Invalid Signature");
        }

        // 4. Authorization check
        String roles = claims.get("roles", String.class);
        HttpMethod reqMethod = HttpMethod.valueOf(req.getMethod());

        if (!authorizationService.canAccess(roles, path, reqMethod)) {
            return forbiddenResponse("You do not have access to: " + path);
        }

        // 5. Forward request to backend
        return gatewayRoutingService.forwardRequest(req, targetService);
    }

    private ResponseEntity<HashMap<String, String>> forbiddenResponse(String message) {
        HashMap<String, String> resBody = new HashMap<>();
        resBody.put("success", "false");
        resBody.put("message", message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resBody);
    }
}