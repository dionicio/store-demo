package com.store.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.demo.entities.OrderDetailEntity;
import com.store.demo.entities.OrderEntity;
import com.store.demo.entities.ProductEntity;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Integer> {
	
	List<OrderDetailEntity> findByOrder(OrderEntity order);
	
	Optional<OrderDetailEntity> findByOrderAndProduct(OrderEntity order, ProductEntity product);
}
