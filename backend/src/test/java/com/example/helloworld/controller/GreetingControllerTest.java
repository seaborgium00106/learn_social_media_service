package com.example.helloworld.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, World!"));
    }

    @Test
    public void testHelloWithName() throws Exception {
        mockMvc.perform(get("/hello").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, John!"));
    }

    @Test
    public void testGreetingEndpoint() throws Exception {
        mockMvc.perform(get("/greeting").param("firstName", "Jane"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Greetings, Jane!"));
    }
}
