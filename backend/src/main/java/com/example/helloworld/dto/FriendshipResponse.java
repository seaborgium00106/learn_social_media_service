package com.example.helloworld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for friendship response operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipResponse {
    
    private Long id;
    private Long userId;
    private String username;
    private Long friendId;
    private String friendUsername;
    private LocalDateTime createdAt;
}
