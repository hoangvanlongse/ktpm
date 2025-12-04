package service.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import service.auth.entity.UserInfo;
import service.auth.repository.UserInfoRepository;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    // Method to load user details by username (email)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database by email (username)
        Optional<UserInfo> userInfo = repository.findByEmail(username);

        if (userInfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // Convert UserInfo to UserDetails (UserInfoDetails)
        UserInfo user = userInfo.get();

        List<GrantedAuthority> authories = List.of(user.getRoles().split(","))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(user.getEmail(), user.getPassword(), authories);
    }

    // Add any additional methods for registering or managing users
    public String addUser(UserInfo userInfo) {
        Optional<UserInfo> checkUserInfo = repository.findByEmail(userInfo.getEmail());

        if (checkUserInfo.isPresent()) {
            return "existed";
        }

        try {
            // Encrypt password before saving
            userInfo.setPassword(encoder.encode(userInfo.getPassword()));
            repository.save(userInfo);

            return "success";
        } catch (Exception e) {
            return "failed";
        }
    }

    public String getUserAuthorities(String username) {
        Optional<UserInfo> userInfo = repository.findByEmail(username);

        UserInfo user = userInfo.get();

        return user.getRoles();
    }
}
