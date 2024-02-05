package com.store.demo.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.store.demo.dto.OrderDetailDTO;
import com.store.demo.dto.ProductDTO;
import com.store.demo.entities.OrderDetailEntity;
import com.store.demo.entities.OrderEntity;
import com.store.demo.entities.ProductEntity;
import com.store.demo.exception.ItemNotFoundException;
import com.store.demo.repository.OrderDetailRepository;
import com.store.demo.repository.OrderRepository;
import com.store.demo.services.OrderDetailService;
import com.store.demo.services.ProductService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ProductService productService;

	@Value("${error.order-detail-not-found}")
	private String orderDetailNotFound;

	@Autowired
	private ModelMapper mapper;

	/**
	 * Get Order's Items
	 * @param order
	 * @return
	 */
	@Override
	public List<OrderDetailDTO> findByOrder(int orderId) {
		return orderDetailRepository.findByOrder(OrderEntity.builder().id(orderId).build())
				.stream().map(c-> mapper.map(c, OrderDetailDTO.class)).collect(Collectors.toList());
	}

	/**
	 * Find OrderDetail by Id
	 * @param id
	 * @return
	 */
	@Override
	public OrderDetailDTO findById(Integer id) {
		
		OrderDetailEntity orderDet = orderDetailRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException(orderDetailNotFound + id));
		return mapper.map(orderDet, OrderDetailDTO.class);

	}

	/**
	 * Add  Item to an Order
	 * @param orderDetailDto
	 * @param orderId
	 * @return
	 */
	@Override
	public OrderDetailDTO save(OrderDetailDTO orderDetailDto, Integer orderId) {
		
		// get product data
		ProductDTO product = productService.findById(orderDetailDto.getProduct().getId());
		OrderDetailDTO orderDetail = new OrderDetailDTO(null,product, product.getPrice(), new Date(), orderDetailDto.getQuantity());
		
		// verify the order
		OrderEntity order =orderRepository.findById(orderId)
				.orElseThrow(() -> new ItemNotFoundException(orderDetailNotFound + orderId));
		OrderDetailEntity orderDetEntity = mapper.map(orderDetail, OrderDetailEntity.class);
		orderDetEntity.setOrder(order);
		
		// save the product on local repository 
		productService.save(orderDetail.getProduct());
		// save the detail
		orderDetEntity = orderDetailRepository.save(orderDetEntity);
		OrderDetailDTO orderDetPDto = mapper.map(orderDetEntity, OrderDetailDTO.class);

		return orderDetPDto;
	}
	
	/**
	 * Update  Item to an Order
	 * @param orderDetailDto
	 * @param update
	 * @return
	 */
	@Override
	public OrderDetailDTO update(OrderDetailDTO orderDetailDto, Integer orderId) {
		
		OrderEntity order=OrderEntity.builder().id(orderId).build();
		ProductEntity product=mapper.map(orderDetailDto.getProduct(), ProductEntity.class);
		 // verifying before update it
		OrderDetailEntity orderDet = orderDetailRepository.findByOrderAndProduct(order, product)
				.orElseThrow(() -> new ItemNotFoundException(String.format("Order Detail Not Found order -> %S , product /> ", orderDetailDto.getProduct().getId(),orderId) ));;
		
			// if it's present then update the quantity
		
			orderDet.setQuantity(orderDetailDto.getQuantity());
			orderDetailRepository.save(orderDet);
		
		
		OrderDetailDTO orderDetP = mapper.map(orderDet, OrderDetailDTO.class);
		
				
		return orderDetP;
	}

	/**
	 * Delete Item from an Order
	 * @param id
	 * @return
	 */
	@Override
	public Integer deleteById(Integer id) {
		OrderDetailDTO orderDetP = findById(id); // verifying before delete it
		orderDetailRepository.deleteById(orderDetP.getId());
		return 1;
	}
}
