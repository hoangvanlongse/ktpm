package service.auth.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import service.auth.config.GatewayRouteConfig;
import service.auth.service.AuthorizationService;
import service.auth.service.GatewayRoutingService;
import service.auth.service.JwtService;

@RestController
public class GatewayController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private GatewayRoutingService gateway;

    @Autowired
    private GatewayRouteConfig routeConfig;

    @Autowired
    private AuthorizationService authorizeService;

    // @Value("${study.service.ip}")
    // private String studyServiceURI;

    @RequestMapping("/api/**")
    public ResponseEntity<?> handleAllApiRequests(HttpServletRequest req) {

        // ---------------------------
        // 1. Find the backend service
        // ---------------------------
        String path = req.getRequestURI();
        String targetService = routeConfig.resolveService(path);

        // HARD FILTER: ignore /api/auth/**
        if (path.startsWith("/api/auth/")) {
            // Let Spring route to another controller
            return null;
        }

        if (targetService == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No route defined for " + path);
        }

        if (authorizeService.canAccess(req)) {
            return gateway.forwardRequest(req, targetService);
        }
        // ---------------------------
        // 2. Extract and validate JWT
        // ---------------------------
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            HashMap<String, String> resBody = new HashMap<>();
            resBody.put("success", "false");
            resBody.put("message", "Missing token");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resBody);
        }

        String token = authHeader.substring(7);
        Claims claims = jwtService.extractAllClaims(token);

        if (claims == null) {
            HashMap<String, String> resBody = new HashMap<>();
            resBody.put("success", "false");
            resBody.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resBody);
        }

        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.validateToken(token, userDetails)) {
            HashMap<String, String> resBody = new HashMap<>();
            resBody.put("success", "false");
            resBody.put("message", "Token Expired");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resBody);
        }

        // ---------------------------
        // 3. Authorization check
        // ---------------------------
        String roles = claims.get("roles", String.class);

        if (!authorizeService.canAccess(roles, path, HttpMethod.valueOf(req.getMethod()))) {
            HashMap<String, String> resBody = new HashMap<>();
            resBody.put("success", "false");
            resBody.put("message", "You do not have access to: " + path);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resBody);
        }

        // ---------------------------
        // 4. Forward request to backend
        // ---------------------------
        return gateway.forwardRequest(req, targetService);
    }

    // @GetMapping("/api/study/**")
    // public ResponseEntity<?> handleStudyGetRequests(HttpServletRequest req) {
    // return handleStudyRequests(req);
    // }

    // @PutMapping("/api/study/**")
    // public ResponseEntity<?> handleStudyPutRequests(HttpServletRequest req) {
    // return handleStudyRequests(req);
    // }

    // @PostMapping("/api/study/**")
    // public ResponseEntity<?> handleStudyPostRequests(HttpServletRequest req) {
    // return handleStudyRequests(req);
    // }

    // @DeleteMapping("/api/study/**")
    // public ResponseEntity<?> handleStudyDeleteRequests(HttpServletRequest req) {
    // return handleStudyRequests(req);
    // }

    // private ResponseEntity<?> handleStudyRequests(HttpServletRequest req) {
    // String authHeader = req.getHeader("Authorization");
    // String token = null;
    // String username = null;

    // if (authHeader != null && authHeader.startsWith("Bearer ")) {
    // token = authHeader.substring(7);
    // username = jwtService.extractUsername(token);
    // }

    // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    // if (jwtService.validateToken(token, userDetails)) {
    // String path = req.getRequestURI();
    // String roles = jwtService.extractAllClaims(token).get("roles", String.class);

    // if (!authorizeService.canAccess(roles, path)) {
    // return new ResponseEntity<>("Don't have access" + '\n' + roles + '\n' + path,
    // HttpStatus.FORBIDDEN);
    // }

    // return gateway.forwardRequest(req, studyServiceURI);
    // }

    // else
    // return new ResponseEntity<>("Invalid token", HttpStatus.FORBIDDEN);
    // }
}
