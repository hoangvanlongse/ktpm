package service.auth.controller;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.auth.entity.AuthRequest;
import service.auth.entity.UserRegistrationRequest;
import service.auth.service.IUserInfoService;
import service.auth.service.IJwtService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    // Using interfaces and final fields (DIP/Constructor Injection)
    private final IUserInfoService userInfoService;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Constructor Injection replaces @Autowired fields
    public UserController(IUserInfoService userInfoService, IJwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    // ... rest of the class methods remain the same, using userInfoService and
    // jwtService
    // ...

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        String addNewUserstatus = userInfoService.addUser(userRegistrationRequest); // Uses IUserInfoService

        HashMap<String, String> resBody = new HashMap<>();

        if ("existed".equals(addNewUserstatus)) {
            resBody.put("success", "false");
            resBody.put("message", "User already existed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody);
        }

        if (addNewUserstatus.startsWith("failed:")) {
            String errorMessage = addNewUserstatus.substring("failed:".length());
            resBody.put("success", "false");
            resBody.put("message", "User creation failed: " + errorMessage);
            // Use INTERNAL_SERVER_ERROR or BAD_REQUEST depending on the root cause
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resBody);
        }

        resBody.put("success", "true");
        resBody.put("message", "Add new user successfully");
        return ResponseEntity.status(HttpStatus.OK).body(resBody);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        HashMap<String, String> resBody = new HashMap<>();
        if (authentication.isAuthenticated()) {
            String userRole = userInfoService.getUserAuthorities(authRequest.getUsername()); // Uses IUserInfoService

            resBody.put("success", "true");
            resBody.put("token", jwtService.generateToken(authRequest.getUsername(), userRole)); // Uses IJwtService
            return ResponseEntity.status(HttpStatus.OK).body(resBody);
        } else {
            resBody.put("success", "fail");
            resBody.put("message", "Email or password is invalid");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resBody);
        }
    }
}