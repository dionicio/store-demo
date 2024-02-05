package com.store.demo.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class OrderDetailDTO {
	 private Integer id;
	@NonNull
    private ProductDTO product;
    private BigDecimal price;
    private Date createdAt;
    private Integer quantity;
  
    
    
    
}
    

