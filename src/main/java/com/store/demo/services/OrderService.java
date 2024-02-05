package com.store.demo.services;

import java.util.List;

import com.store.demo.dto.OrderDTO;
import com.store.demo.entities.OrderEntity;

public interface OrderService {

	List<?> findAll();

	OrderEntity findById(Integer id);

	Integer deleteById(Integer id);

	OrderDTO save(OrderDTO orderDto);

	OrderEntity update(OrderDTO orderDto, Integer id);

	List<?> findAllByClient(int clientId);


}
