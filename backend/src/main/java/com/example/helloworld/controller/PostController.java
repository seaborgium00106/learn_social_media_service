package com.example.helloworld.controller;

import com.example.helloworld.dto.PostRequest;
import com.example.helloworld.dto.PostResponse;
import com.example.helloworld.service.PostService;
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
 * REST Controller for Post operations.
 * Provides endpoints for creating, reading, updating, and deleting posts.
 */
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Post management endpoints")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowedHeaders = "*", maxAge = 3600)
public class PostController {
    
    private final PostService postService;
    
    /**
     * Create a new post.
     * @param request post creation request
     * @return created post
     */
    @PostMapping
    @Operation(summary = "Create a new post", description = "Creates a new post for a specific user")
    @ApiResponse(responseCode = "201", description = "Post created successfully",
            content = @Content(schema = @Schema(implementation = PostResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input or user not found")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        PostResponse response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get all posts.
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @return list of all posts
     */
    @GetMapping
    @Operation(summary = "Get all posts", description = "Retrieves all posts in the system with pagination")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully",
            content = @Content(schema = @Schema(implementation = PostResponse.class)))
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        List<PostResponse> posts = postService.getAllPosts(page, size);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * Get a post by ID.
     * @param id post ID
     * @return post details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieves a specific post by its ID")
    @ApiResponse(responseCode = "200", description = "Post retrieved successfully",
            content = @Content(schema = @Schema(implementation = PostResponse.class)))
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<PostResponse> getPostById(
            @Parameter(description = "Post ID", example = "1")
            @PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }
    
    /**
     * Get all posts by a specific user.
     * @param userId user ID
     * @return list of user's posts
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get posts by user", description = "Retrieves all posts created by a specific user")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully",
            content = @Content(schema = @Schema(implementation = PostResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<List<PostResponse>> getPostsByUserId(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId) {
        List<PostResponse> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * Search posts by text content.
     * @param search search text
     * @return list of matching posts
     */
    @GetMapping("/search")
    @Operation(summary = "Search posts", description = "Searches posts by text content (case-insensitive)")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully",
            content = @Content(schema = @Schema(implementation = PostResponse.class)))
    public ResponseEntity<List<PostResponse>> searchPosts(
            @Parameter(description = "Search text", example = "spring")
            @RequestParam String search) {
        List<PostResponse> posts = postService.searchPosts(search);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * Update a post.
     * @param id post ID
     * @param request post update request
     * @return updated post
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a post", description = "Updates the text content of an existing post")
    @ApiResponse(responseCode = "200", description = "Post updated successfully",
            content = @Content(schema = @Schema(implementation = PostResponse.class)))
    @ApiResponse(responseCode = "404", description = "Post not found")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<PostResponse> updatePost(
            @Parameter(description = "Post ID", example = "1")
            @PathVariable Long id,
            @RequestBody PostRequest request) {
        PostResponse response = postService.updatePost(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete a post.
     * @param id post ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post", description = "Deletes a specific post")
    @ApiResponse(responseCode = "204", description = "Post deleted successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "Post ID", example = "1")
            @PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
