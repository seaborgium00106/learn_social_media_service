package com.example.helloworld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating a user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    
    private String username;
    private String email;
}
