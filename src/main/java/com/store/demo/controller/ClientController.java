package com.store.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.demo.dto.ClientDTO;
import com.store.demo.dto.CommonResDTO;
import com.store.demo.entities.ClientEntity;
import com.store.demo.services.ClientService;
import com.store.security.dao.request.SignUpRequest;
import com.store.security.dao.response.JwtAuthenticationResponse;
import com.store.security.service.AuthenticationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/v1")
@Validated
public class ClientController {
	@Autowired
	ClientService clientService;

	@Autowired
	private ModelMapper mapper;

	@Value("${error.generic-msg}")
	private String genericMsg;
	
	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping(value = "/clients")
	List<ClientDTO> findAll() {
		return clientService.findAll().stream().map(c-> 
		 mapper.map(c, ClientDTO.class)).collect(Collectors.toList());
	
	}

	@GetMapping(value = "/clients/{id}")
	ResponseEntity<ClientDTO> findById(@PathVariable("id") @Min(1) int id) {
		ClientEntity client = clientService.findById(id);
		ClientDTO clientdto = mapper.map(client, ClientDTO.class);
		return ResponseEntity.ok().body(clientdto);
	}

	@PostMapping(value = "/clients/create")
	ResponseEntity<JwtAuthenticationResponse> create(@Valid @RequestBody ClientDTO clientDto) {

		ClientEntity client = clientService.save(clientDto);
		
		  SignUpRequest signUpUser = SignUpRequest.builder().email(client.getEmail()).password(clientDto.getPassword()).build();
		  JwtAuthenticationResponse token=   authenticationService.signup(signUpUser);
		
		return ResponseEntity.ok().body(token);

	}

	@PutMapping(value = "/clients/{id}")
	ResponseEntity<ClientDTO> update(@PathVariable("id") @Min(1) int id, @Valid @RequestBody ClientDTO clientDto) {

		ClientEntity client = clientService.update(clientDto, id);
		ClientDTO clientdto = mapper.map(client, ClientDTO.class);
		return ResponseEntity.ok().body(clientdto);

	}

	@DeleteMapping(value = "/clients/{id}")
	ResponseEntity<CommonResDTO> delete(@PathVariable("id") @Min(1) int id) {

		clientService.deleteById(id);
		return ResponseEntity.ok().body(new CommonResDTO("Client deleted with success!"));

	}
}