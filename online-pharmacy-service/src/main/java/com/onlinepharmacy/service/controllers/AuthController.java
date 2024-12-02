package com.onlinepharmacy.service.controllers;

import com.onlinepharmacy.service.entities.Role;
import com.onlinepharmacy.service.entities.User;
import com.onlinepharmacy.service.payloads.LoginCredentials;
import com.onlinepharmacy.service.payloads.UserDTO;
import com.onlinepharmacy.service.security.JwtUtil;
import com.onlinepharmacy.service.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerHandler(@Valid @RequestBody UserDTO user) {
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        UserDTO userDTO = userService.registerUser(user);
        String token = jwtUtil.generateToken(userDTO.getEmail());
        return new ResponseEntity<>(Collections.singletonMap("jwt-token", token),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@Valid @RequestBody LoginCredentials credentials) {
        UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                credentials.getEmail(), credentials.getPassword());
        authenticationManager.authenticate(authCredentials);
        String token = jwtUtil.generateToken(credentials.getEmail());
        UserDTO userDTO = userService.getUserByEmail(credentials.getEmail());
        String role = userDTO.getRoles()
                .stream()
                .map(Role::getRoleName)
                .filter("ADMIN"::equals)
                .findFirst()
                .orElse("USER");
        return Map.of("jwt-token", token,
                "role", role,
                "firstName", userDTO.getFirstName(),
                "lastName", userDTO.getLastName(),
                "user", userDTO.getEmail(),
                "cartId", userDTO.getCart().getCartId(),
                "userId", userDTO.getUserId());
    }
}
