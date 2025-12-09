package service.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import service.auth.entity.UserRegistrationRequest;

// Extends UserDetailsService to fulfill the requirement in SecurityConfig
public interface IUserInfoService extends UserDetailsService {
    String addUser(UserRegistrationRequest userRegistrationRequest);

    String getUserName(String username);

    Long getUserStudyId(String username);

    String getUserAuthorities(String username);
}
