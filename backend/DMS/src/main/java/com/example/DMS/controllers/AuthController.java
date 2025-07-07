package com.example.DMS.controllers;


import com.example.DMS.requests.AuthenticationRequest;
import com.example.DMS.requests.RegisterRequest;
import com.example.DMS.responses.AuthenticationResponse;
import com.example.DMS.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;




    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register( @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login( @Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
