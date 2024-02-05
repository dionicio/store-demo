package com.store.demo.controller;

import java.util.List;

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
import com.store.demo.dto.OrderDetailDTO;
import com.store.demo.exception.ItemRequiredException;
import com.store.demo.services.OrderDetailService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1")
@Validated
@Slf4j
public class OrderDetailController {
	@Autowired
	OrderDetailService orderDetService;

	@Autowired
	private ModelMapper mapper;

	@Value("${error.generic-msg}")
	private String genericMsg;

	@GetMapping(value = "/orderdetails/{id}")
	ResponseEntity<OrderDetailDTO> findById(@PathVariable("id") @Min(1) int orderDetailId) {
		OrderDetailDTO orderDto = orderDetService.findById(orderDetailId);
		return ResponseEntity.ok().body(orderDto);
	}
	
	@GetMapping(value = "/orderdetails/order/{id}")
	List<OrderDetailDTO> findByOrder(@PathVariable("id") @Min(1) int orderId) {
		return orderDetService.findByOrder(orderId);
	
	}

	

	@PostMapping(value = "/orderdetails/order/{id}")
	ResponseEntity<OrderDetailDTO> create(@Valid @RequestBody OrderDetailDTO orderDetDto,@PathVariable("id") @Min(1) int orderId) {
	
		// find the product
		if (orderDetDto == null || orderDetDto.getProduct() == null) {		
		  throw new ItemRequiredException("There isn't any product!");
		}
		
		// quantity is required
		if ( orderDetDto.getQuantity() == null) {		
		  throw new ItemRequiredException("quantity is required!");
		}
		OrderDetailDTO order = orderDetService.save(orderDetDto,orderId);
		return ResponseEntity.ok().body(order);

	}

	@PutMapping(value = "/orderdetails/order/{id}")
	ResponseEntity<OrderDetailDTO> update(@Valid @RequestBody OrderDetailDTO orderDetDto,@PathVariable("id") @Min(1) int orderId) {
		// find the product
				if (orderDetDto == null || orderDetDto.getProduct() == null) {		
				  throw new ItemRequiredException("There isn't any product!");
				}
				
				// quantity is required
				if ( orderDetDto.getQuantity() == null) {		
				  throw new ItemRequiredException("quantity is required!");
				}
		
		OrderDetailDTO order = orderDetService.update(orderDetDto, orderId);
		return ResponseEntity.ok().body(order);

	}
	

	@DeleteMapping(value = "/orderdetails/{id}")
	ResponseEntity<CommonResDTO> delete(@PathVariable("id") @Min(1) int ordetDetailId) {

		orderDetService.deleteById(ordetDetailId);
		return ResponseEntity.ok().body(new CommonResDTO("OrderDetail deleted with success!"));

	}
}