package com.store.security.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.store.security.entities.User;

public interface UserService {
    UserDetailsService userDetailsService();

}
