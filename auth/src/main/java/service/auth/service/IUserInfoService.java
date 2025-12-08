package service.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import service.auth.entity.UserRegistrationRequest;

// Extends UserDetailsService to fulfill the requirement in SecurityConfig
public interface IUserInfoService extends UserDetailsService {
    String addUser(UserRegistrationRequest userRegistrationRequest);

    String getUserAuthorities(String username);
}
