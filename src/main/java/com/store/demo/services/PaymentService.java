package com.store.demo.services;

import com.store.demo.dto.PaymentDTO;
import com.store.demo.dto.PaymentReqDTO;

public interface PaymentService {

	/**
	 * 
	 * @param paymentReq
	 * @return PaymentResDTO
	 */
	PaymentDTO payOrder(PaymentReqDTO paymentReq);

}
