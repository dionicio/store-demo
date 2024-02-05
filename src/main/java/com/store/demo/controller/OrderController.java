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

import com.store.demo.dto.CommonResDTO;
import com.store.demo.dto.OrderDTO;
import com.store.demo.entities.OrderEntity;
import com.store.demo.exception.ItemRequiredException;
import com.store.demo.services.OrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/v1")
@Validated
public class OrderController {
	@Autowired
	OrderService orderService;

	@Value("${error.generic-msg}")
	private String genericMsg;
	
	@Autowired
	private ModelMapper mapper;

	@GetMapping(value = "/orders")
	List<OrderDTO> findAll() {
		
		return orderService.findAll().stream().map(o-> 
		 mapper.map(o, OrderDTO.class)).collect(Collectors.toList());

	}
	
	@GetMapping(value = "/orders/client/{id}")
	List<OrderDTO> findAll(@PathVariable("id") @Min(1) int clientId) {
		
		return orderService.findAllByClient(clientId).stream().map(o-> 
		 mapper.map(o, OrderDTO.class)).collect(Collectors.toList());

	}

	@GetMapping(value = "/orders/{id}")
	ResponseEntity<OrderDTO> findById(@PathVariable("id") @Min(1) int id) {

		OrderEntity order = orderService.findById(id);
		OrderDTO orderDto = mapper.map(order, OrderDTO.class);

		return ResponseEntity.ok().body(orderDto);

	}
	
	

	@PostMapping(value = "/orders")
	ResponseEntity<?> create(@Valid @RequestBody OrderDTO orderDTO) {

		// find the details
		if (orderDTO.getDetails() == null || orderDTO.getDetails().size() == 0) {		
		  throw new ItemRequiredException("There isn't any detail!");
		}
		
		OrderDTO addedorder = orderService.save(orderDTO);
		return ResponseEntity.ok().body(addedorder);

	}

	@PutMapping(value = "/orders/{id}")
	ResponseEntity<OrderDTO> update(@PathVariable("id") @Min(1) int id, @Valid @RequestBody OrderDTO OrderDTO) {

		OrderEntity order = orderService.update(OrderDTO, id);
		OrderDTO orderDto = mapper.map(order, OrderDTO.class);
		return ResponseEntity.ok().body(orderDto);

	}

	@DeleteMapping(value = "/orders/{id}")
	ResponseEntity<?> delete(@PathVariable("id") @Min(1) int id) {

		orderService.deleteById(id);
		return ResponseEntity.ok().body(new CommonResDTO("order deleted with success!"));

	}
}