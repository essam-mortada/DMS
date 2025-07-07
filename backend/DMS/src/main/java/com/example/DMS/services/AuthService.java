package com.example.DMS.services;

import com.example.DMS.config.JwtUtils;
import com.example.DMS.models.Role;
import com.example.DMS.models.User;
import com.example.DMS.repository.UserRepository;
import com.example.DMS.requests.AuthenticationRequest;
import com.example.DMS.requests.RegisterRequest;
import com.example.DMS.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.DMS.exception.AuthenticationException;
import com.example.DMS.exception.UserNotFoundException;

import com.example.DMS.responses.UserResponse;@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;



    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        var user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNationalId(request.getNationalId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail());
        return new AuthenticationResponse(token, user);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String token = jwtUtils.generateToken(user);
        return new AuthenticationResponse(token, new UserResponse(user));
    }
}

