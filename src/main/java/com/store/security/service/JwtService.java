package com.store.security.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.store.security.entities.User;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(User userDetails);

    boolean isTokenValid(String token);
}
