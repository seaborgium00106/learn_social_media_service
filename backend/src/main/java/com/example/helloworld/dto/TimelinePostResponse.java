package com.example.helloworld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning post information in a user's timeline.
 * Includes additional friend information for better context.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimelinePostResponse {
    
    private Long id;
    private String text;
    private Long userId;
    private String username;      // The friend who created the post
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
