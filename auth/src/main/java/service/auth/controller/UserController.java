package service.auth.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.RequiredArgsConstructor;
import service.auth.entity.AuthRequest;
import service.auth.entity.UserInfo;
import service.auth.service.JwtService;
import service.auth.service.UserInfoService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody UserInfo userInfo) {
        String addNewUserstatus = service.addUser(userInfo);

        HashMap<String, String> resBody = new HashMap<>();

        if ("existed".equals(addNewUserstatus)) {
            resBody.put("success", "false");
            resBody.put("message", "User already existed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody);
        }

        if ("failed".equals(addNewUserstatus)) {
            resBody.put("success", "false");
            resBody.put("message", "Internal error");
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
            String userRole = service.getUserAuthorities(authRequest.getUsername());

            resBody.put("success", "true");
            resBody.put("token", jwtService.generateToken(authRequest.getUsername(), userRole));
            return ResponseEntity.status(HttpStatus.OK).body(resBody);
        } else {
            resBody.put("success", "fail");
            resBody.put("message", "Email or password is invalid");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resBody);
        }
    }

    // @PostMapping("/generateToken")
    // public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    // Authentication authentication = authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
    // authRequest.getPassword()));
    // if (authentication.isAuthenticated()) {

    // return jwtService.generateToken(authRequest.getUsername());
    // } else {
    // throw new UsernameNotFoundException("Invalid user request!");
    // }
    // }
}
