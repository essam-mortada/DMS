package com.example.DMS.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final String email;
    private final String password;
    private final String nid;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String email, String password, String nid,
                             Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.nid = nid;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;  // Spring Security expects username as email here
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Custom method for NID
    public String getNid() {
        return nid;
    }
}