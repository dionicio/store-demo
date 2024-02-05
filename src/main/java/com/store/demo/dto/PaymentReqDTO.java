package com.store.demo.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class PaymentReqDTO {
    @NonNull
	private Integer orderId;
	private String paymentType;
	private String cardType;
	private String cardHolderName;
	@NonNull
	private String cardNumber;
	@NonNull
	private String expiryMonth;
	@NonNull
	private String expiryYear;
	@NonNull
	private String cvv;
	private String currency;

}
