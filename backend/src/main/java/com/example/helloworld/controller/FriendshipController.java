package com.example.helloworld.controller;

import com.example.helloworld.dto.FriendshipRequest;
import com.example.helloworld.dto.FriendshipResponse;
import com.example.helloworld.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing friendships between users.
 * Provides endpoints for adding, removing, and retrieving friendships.
 */
@RestController
@RequestMapping("/api/v1/friendships")
@RequiredArgsConstructor
@Tag(name = "Friendship", description = "Friendship management APIs")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowedHeaders = "*", maxAge = 3600)
public class FriendshipController {
    
    private final FriendshipService friendshipService;
    
    /**
     * Add a new friendship between two users.
     * Creates a bidirectional friendship relationship.
     * 
     * @param request containing userId and friendId
     * @return created friendship response
     */
    @PostMapping
    @Operation(summary = "Add a friendship", description = "Creates a bidirectional friendship between two users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Friendship created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or friendship already exists"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<FriendshipResponse> addFriend(@RequestBody FriendshipRequest request) {
        FriendshipResponse response = friendshipService.addFriend(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Remove a friendship between two users.
     * Removes the bidirectional friendship relationship.
     * 
     * @param request containing userId and friendId
     * @return no content
     */
    @DeleteMapping
    @Operation(summary = "Remove a friendship", description = "Removes a bidirectional friendship between two users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Friendship removed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or friendship does not exist"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> removeFriend(@RequestBody FriendshipRequest request) {
        friendshipService.removeFriend(request);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get all friends of a specific user.
     * 
     * @param userId the user's ID
     * @return list of friends
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get friends of a user", description = "Retrieves all friends of a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved friends"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<FriendshipResponse>> getFriendsOfUser(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        List<FriendshipResponse> friends = friendshipService.getFriendsOfUser(userId);
        return ResponseEntity.ok(friends);
    }
    
    /**
     * Check if two users are friends.
     * 
     * @param userId the first user's ID
     * @param friendId the second user's ID
     * @return response indicating if they are friends
     */
    @GetMapping("/check")
    @Operation(summary = "Check if users are friends", description = "Checks if two users have a friendship relationship")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully checked friendship status")
    })
    public ResponseEntity<Map<String, Boolean>> areFriends(
            @Parameter(description = "ID of the first user") @RequestParam Long userId,
            @Parameter(description = "ID of the second user") @RequestParam Long friendId) {
        boolean areFriends = friendshipService.areFriends(userId, friendId);
        return ResponseEntity.ok(Map.of("areFriends", areFriends));
    }
    
    /**
     * Get the friend count for a user.
     * 
     * @param userId the user's ID
     * @return friend count
     */
    @GetMapping("/user/{userId}/count")
    @Operation(summary = "Get friend count", description = "Retrieves the number of friends a user has")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved friend count"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, Long>> getFriendCount(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        long count = friendshipService.getFriendCount(userId);
        return ResponseEntity.ok(Map.of("userId", userId, "friendCount", count));
    }
}
