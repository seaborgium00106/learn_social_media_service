package com.example.helloworld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating a post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    
    private String text;
    private Long userId;
}
