package service.auth.service;

import org.springframework.http.HttpMethod;

import jakarta.servlet.http.HttpServletRequest;

public interface IAuthorizationService {
    boolean canAccess(HttpServletRequest req);

    boolean canAccess(String userRoles, String path, HttpMethod reqMethod);
}