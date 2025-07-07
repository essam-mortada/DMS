package com.example.DMS.responses;

import com.example.DMS.models.Role;
import com.example.DMS.models.User;

public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;

    // Constructor using User entity
    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;}

    public Role getRole() {
        return role;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }
}