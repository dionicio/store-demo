package com.store.demo.services;

import java.util.List;

import com.store.demo.dto.ClientDTO;
import com.store.demo.entities.ClientEntity;
import com.store.security.dao.response.JwtAuthenticationResponse;

public interface ClientService {

	List<?> findAll();

	ClientEntity findById(Integer id);

	ClientEntity save(ClientDTO clientDto);

	ClientEntity update(ClientDTO clientDto, Integer id);

	Integer deleteById(Integer id);


	


}
