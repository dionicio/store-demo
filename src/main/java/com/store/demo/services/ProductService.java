package com.store.demo.services;

import java.util.List;

import com.store.demo.dto.ProductDTO;

public interface ProductService {

	List<ProductDTO> fetchProductsFromProxy();

	ProductDTO findById(Integer id);

	ProductDTO save(ProductDTO productDto);

}
