package com.store.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
   
	private Integer id;
    private String firstName;
    private String lastName;   
    private String email;  
    private String password;
  

   

    
}
