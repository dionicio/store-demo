package com.store.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.security.controller.AuthenticationController;
import com.store.security.dao.request.SigninRequest;
import com.store.security.service.AuthenticationService;
import com.store.security.service.JwtService;
import com.store.security.service.UserService;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TestLoginController {

    @Autowired
    private MockMvc mvc;

	@MockBean
	JwtService jwtService;
	
	@MockBean
	UserService userService;
	@MockBean
	AuthenticationService authService;
	

	
	@Test
	public void Login() throws Exception 
	{
		
	  mvc.perform(MockMvcRequestBuilders
	  			.post("/api/v1/auth/signin")
	  			.content(asJsonString(new SigninRequest("admin@store.com", "123")))
	  	      .contentType(MediaType.APPLICATION_JSON)
	  	      .accept(MediaType.APPLICATION_JSON))
	  .andExpect(status().isOk());
	

	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
