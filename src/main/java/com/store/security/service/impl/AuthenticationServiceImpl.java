package com.store.security.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.store.security.dao.request.SignUpRequest;
import com.store.security.dao.request.SigninRequest;
import com.store.security.dao.response.JwtAuthenticationResponse;
import com.store.security.entities.Role;
import com.store.security.entities.User;
import com.store.security.repository.UserRepository;
import com.store.security.service.AuthenticationService;
import com.store.security.service.JwtService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@PostConstruct
	private void postConstruct() {

		Optional<User> user = userRepository.findByEmail("admin@store.com");
		if (user.isEmpty()) {
			var admin = User.builder().email("admin@store.com").password(passwordEncoder.encode("123")).role(Role.ADMIN)
					.build();
			userRepository.save(admin);
		}
	}

	@Override
	public JwtAuthenticationResponse signup(SignUpRequest request) {

		var user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER).build();
		userRepository.save(user);
		var jwt = jwtService.generateToken(user);
		return JwtAuthenticationResponse.builder().token(jwt).build();
	}

	@Override
	public JwtAuthenticationResponse signin(SigninRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
		var jwt = jwtService.generateToken(user);
		return JwtAuthenticationResponse.builder().token(jwt).build();
	}
}
