package com.example.helloworld.controller;

import com.example.helloworld.dto.UserRequest;
import com.example.helloworld.dto.UserResponse;
import com.example.helloworld.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User operations.
 * Provides endpoints for creating, reading, updating, and deleting users.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowedHeaders = "*", maxAge = 3600)
public class UserController {
    
    private final UserService userService;
    
    /**
     * Create a new user.
     * @param request user creation request
     * @return created user
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with unique username and email")
    @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input or duplicate username/email")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get all users.
     * @return list of all users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get a user by ID.
     * @param id user ID
     * @return user details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Get a user by username.
     * @param username username to search for
     * @return user details
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves a specific user by their username")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getUserByUsername(
            @Parameter(description = "Username", example = "john_doe")
            @PathVariable String username) {
        UserResponse user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Update a user.
     * @param id user ID
     * @param request user update request
     * @return updated user
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates an existing user's information")
    @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "Invalid input or duplicate username/email")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id,
            @RequestBody UserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete a user.
     * @param id user ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes a user and all their associated posts")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
