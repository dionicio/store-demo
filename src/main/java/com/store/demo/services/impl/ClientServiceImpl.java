package com.store.demo.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.store.demo.dto.ClientDTO;
import com.store.demo.entities.ClientEntity;
import com.store.demo.exception.DuplicatedException;
import com.store.demo.exception.ItemNotFoundException;
import com.store.demo.repository.ClientRepository;
import com.store.demo.services.ClientService;
import com.store.security.dao.request.SignUpRequest;
import com.store.security.dao.request.SignUpRequest.SignUpRequestBuilder;
import com.store.security.dao.response.JwtAuthenticationResponse;
import com.store.security.service.AuthenticationService;
@Service
public class ClientServiceImpl implements ClientService{

	@Autowired
	private ClientRepository repository;

	@Value("${error.client-not-found}")
	private String orderNotFound;
	
	@Autowired
	private ModelMapper mapper;


	@Override
	public List<?> findAll() {
		return repository.findAll();
	}

	@Override
	public ClientEntity findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ItemNotFoundException(orderNotFound + id));
	}
	
	@Override
	public ClientEntity save(ClientDTO clientDto) {
	
		Boolean isPresent = repository.findByEmail(clientDto.getEmail()).isPresent();
		if (isPresent) {
			throw new DuplicatedException("The email has already registered!");
		}
		ClientEntity client = this.mapper.map(clientDto, ClientEntity.class);
		client = repository.save(client);
		

	
		return client;
	}

	@Override
	public ClientEntity update(ClientDTO clientDto, Integer id) {		
		ClientEntity clientP=findById(id); // verifying before update it
		ClientEntity client = this.mapper.map(clientDto, ClientEntity.class);
		client.setId(clientP.getId());
		return repository.save(client);
	}

	@Override
	public Integer deleteById(Integer id) {
		ClientEntity clientP=findById(id); // verifying before delete it
		repository.deleteById(clientP.getId());
		return 1;
	}
	
	


}
