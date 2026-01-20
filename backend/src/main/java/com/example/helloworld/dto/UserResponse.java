package com.example.helloworld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning user information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
