package com.store.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.demo.dto.PaymentDTO;
import com.store.demo.dto.PaymentReqDTO;
import com.store.demo.services.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
@Validated
public class PaymentController {

	@Autowired
	PaymentService paymentService;

	@Value("${error.generic-msg}")
	private String genericMsg;

	@PostMapping(value = "/payments")
	ResponseEntity<PaymentDTO> payOrder(@Valid @RequestBody PaymentReqDTO paymentReq) {
		PaymentDTO payment = paymentService.payOrder(paymentReq);
		return ResponseEntity.ok().body(payment);
	}

}