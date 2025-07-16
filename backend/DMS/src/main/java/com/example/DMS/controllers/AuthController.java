package com.example.DMS.controllers;


import com.example.DMS.requests.AuthenticationRequest;
import com.example.DMS.requests.RegisterRequest;
import com.example.DMS.DTO.userDTO;
import com.example.DMS.DTO.authDTO;

import com.example.DMS.responses.AuthenticationResponse;
import com.example.DMS.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;




    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register( @Valid @RequestBody userDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login( @Valid @RequestBody authDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
