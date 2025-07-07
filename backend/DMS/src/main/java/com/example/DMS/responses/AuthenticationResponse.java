package com.example.DMS.responses;

import com.example.DMS.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private UserResponse user;

    // Constructor for token-only response
    public AuthenticationResponse(String token) {
        this.token = token;
    }

    // Constructor for token + user response
    public AuthenticationResponse(String token, User user) {
        this.token = token;
        this.user = new UserResponse(user);
    }

    // Getters
    public String getToken() {
        return token;
    }

    public UserResponse getUser() {
        return user;
    }
}