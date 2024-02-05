package com.store.demo.services;

import java.util.List;

import com.store.demo.dto.OrderDetailDTO;

public interface OrderDetailService {

	/**
	 * Get Order's Items
	 * @param order
	 * @return
	 */
	List<OrderDetailDTO> findByOrder(int orderId);

	/**
	 * Find OrderDetail by Id
	 * @param id
	 * @return
	 */
	OrderDetailDTO findById(Integer id);

	/**
	 * Add  Item to an Order
	 * @param orderDetailDto
	 * @param orderId
	 * @return
	 */
	OrderDetailDTO save(OrderDetailDTO orderDetailDto, Integer orderId);

	/**
	 * Update  Item to an Order
	 * @param orderDetailDto
	 * @param update
	 * @return
	 */
	OrderDetailDTO update(OrderDetailDTO orderDetailDto, Integer orderId);

	/**
	 * Delete Item from an Order
	 * @param id
	 * @return
	 */
	Integer deleteById(Integer id);

}
