package com.store.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.demo.dto.ProductDTO;
import com.store.demo.services.ProductService;

import jakarta.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/v1")
@Validated
public class ProductController {
	
	
	@Autowired
	ProductService productService;

	@Value("${error.generic-msg}")
	private String genericMsg;
	


	@GetMapping(value = "/products")
	List<ProductDTO> findAll() {
			return productService.fetchProductsFromProxy();		 
	}

	@GetMapping(value = "/products/{id}")
	ResponseEntity<ProductDTO> findById(@PathVariable("id") @Min(1) int id) {
			ProductDTO product = productService.findById(id);
			return ResponseEntity.ok().body(product);
	}

	
}