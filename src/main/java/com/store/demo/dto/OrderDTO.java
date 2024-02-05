package com.store.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer id;  
    @NonNull
    private ClientDTO client;   
    private String status;
    private BigDecimal amount;
    @NonNull
    private List<OrderDetailDTO> details;
}
    

