package com.store.demo.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.store.demo.entities.OrderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
   
    private Integer id;
    private OrderDTO order;
    private Date paymentDate;
    private BigDecimal amount;
    private String paymentType;
    private String paymentAuthorization;
    private String cardNumber;
	private String cardType;
	private String cardHolderName;
    
}
    
