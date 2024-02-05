package com.store.demo.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.store.demo.dto.OrderDTO;
import com.store.demo.dto.OrderDetailDTO;
import com.store.demo.entities.ClientEntity;
import com.store.demo.entities.OrderEntity;
import com.store.demo.exception.ItemNotFoundException;
import com.store.demo.repository.OrderRepository;
import com.store.demo.services.ClientService;
import com.store.demo.services.OrderDetailService;
import com.store.demo.services.OrderService;
import com.store.demo.services.ProductService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductService productService;
	
	@Autowired
	OrderDetailService orderDetailService;
	
	@Autowired
	ClientService clientService;

	@Value("${error.order-not-found}")
	private String orderNotFound;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<OrderEntity> findAll() {
		return orderRepository.findAll();
	}
	
	@Override
	public List<OrderEntity> findAllByClient(int clientId) {
		return orderRepository.findByClient(ClientEntity.builder().id(clientId).build());
	
	}

	@Override
	public OrderEntity findById(Integer id) {
		return orderRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(orderNotFound + id));
	}

	/**
	 * Save an Client's Order and Details
	 * @param id
	 * @return
	 */
	@Override
	public OrderDTO save(OrderDTO orderDto) {
		
		final List<OrderDetailDTO> details;
		//find the client
		ClientEntity client=clientService.findById(orderDto.getClient().getId());

			
		
		OrderEntity order =  OrderEntity.builder().client(client).createdAt(new Date()).status("INI").build();
		//save the order			
		OrderEntity orderP =orderRepository.save(order);

		
		//save details and get the id of each one
		List<OrderDetailDTO> detailEntities = orderDto.getDetails().stream().map(det -> {				
				return  orderDetailService.save(det, orderP.getId());
			}).collect(Collectors.toList());
		
		OrderDTO	orderPDto=	mapper.map(order, OrderDTO.class);		
		
		
		orderPDto.setDetails(detailEntities);
		return orderPDto;
	}

	@Override
	public OrderEntity update(OrderDTO orderDto, Integer id) {
		OrderEntity orderP = findById(id); // verifying before update it
		OrderEntity order = mapper.map(orderDto, OrderEntity.class);
		order.setId(orderP.getId());
		return orderRepository.save(order);
	}
		

	@Override
	public Integer deleteById(Integer id) {
		OrderEntity orderP = findById(id); // verifying before delete it
		orderRepository.deleteById(orderP.getId());
		return 1;
	}

	
}
