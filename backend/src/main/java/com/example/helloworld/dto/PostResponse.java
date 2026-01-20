package com.example.helloworld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning post information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    
    private Long id;
    private String text;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
