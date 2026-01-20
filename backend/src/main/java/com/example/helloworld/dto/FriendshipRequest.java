package com.example.helloworld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for friendship request operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipRequest {
    
    private Long userId;
    private Long friendId;
}
