package com.store.demo.services.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.demo.dto.OrderDetailDTO;
import com.store.demo.dto.PaymentDTO;
import com.store.demo.dto.PaymentReqDTO;
import com.store.demo.entities.OrderEntity;
import com.store.demo.entities.PaymentEntity;
import com.store.demo.repository.OrderRepository;
import com.store.demo.repository.PaymentRepository;
import com.store.demo.services.OrderDetailService;
import com.store.demo.services.OrderService;
import com.store.demo.services.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	private ModelMapper mapper;


	/**
	 *  Processing the payments. First, it validates the order and calculates the amount to pay,
	 *   then simulates a call to the gateway and gets the authorization, 
	 *   finally saving the payment.
	 * @param paymentReq
	 * @return PaymentDTO
	 */

	@Override
	public PaymentDTO payOrder(PaymentReqDTO paymentReq) {
		
		OrderEntity order =orderService.findById(paymentReq.getOrderId());
		List<OrderDetailDTO> details = orderDetailService.findByOrder(order.getId());
		BigDecimal amount=details.stream().map(d-> d.getPrice()).reduce( (a,b) -> a.add(b) ).get();
		//call the payment gateway and get the authorization
		String authorization="";
		
		  try { Thread.sleep(1000*2);
		  authorization =UUID.randomUUID().toString();
		  } catch (InterruptedException e) { e.printStackTrace(); }
		 
				
		PaymentEntity payment = PaymentEntity.builder().amount(amount)
			     .order(order)
				.paymentAuthorization(authorization)
		.cardNumber(paymentReq.getCardNumber().substring(paymentReq.getCardNumber().length() - 4))
		.paymentType(paymentReq.getPaymentType())
		.cardHolderName(paymentReq.getCardHolderName())
		.cardType(paymentReq.getCardType())
		.status("PAID")
		.paymentDate(new Date()).build();
	
		
		PaymentEntity paymentP = paymentRepository.save(payment);
		order.setStatus("PAID");
		
		orderRepository.save(order);
		
		
		return mapper.map(paymentP, PaymentDTO.class);
	
		
	}

	
	
}
