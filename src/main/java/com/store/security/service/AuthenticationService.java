package com.store.security.service;

import com.store.security.dao.request.SignUpRequest;
import com.store.security.dao.request.SigninRequest;
import com.store.security.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
