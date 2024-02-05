package com.store.demo.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.demo.dto.ProductDTO;
import com.store.demo.entities.ProductEntity;
import com.store.demo.exception.ItemNotFoundException;
import com.store.demo.repository.ProductRepository;
import com.store.demo.services.ProductService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
     
	@Value("${product.proxy}")
	private String productProxy;
	
	@Value("${error.order-not-found}")
	private String productNotFound;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
    public List<ProductDTO> fetchProductsFromProxy() {
		
		Mono<Object[]> response = WebClient.create()
			      .get()
			      .uri(productProxy)
			      .retrieve()
			      .bodyToMono(Object[].class).log();
		
		Object[] objects = response.block(); 
		ObjectMapper mapper = new ObjectMapper();
		 
		return Arrays.stream(objects)
				  .map(object -> mapper.convertValue(object, ProductDTO.class))
			
				  .collect(Collectors.toList());
    }
	
	@Override
    public ProductDTO findById(Integer id) {
		log.info("find product by "+id);
		Mono<ProductDTO> response = WebClient.create()
			      .get()
			      .uri(productProxy+"/"+id)
			      .retrieve()
			      .bodyToMono(ProductDTO.class).log();
		
		return response.blockOptional().orElseThrow(() -> new ItemNotFoundException(productNotFound + id));

		
    }

	@Override
	public ProductDTO save(ProductDTO productDto) {
			
		ProductEntity product = this.mapper.map(productDto, ProductEntity.class);
		product = productRepository.save(product); 
		
		return this.mapper.map(product, ProductDTO.class);
	}
	
}
