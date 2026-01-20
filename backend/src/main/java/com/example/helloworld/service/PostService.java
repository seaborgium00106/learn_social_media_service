package com.example.helloworld.service;

import com.example.helloworld.dto.PostRequest;
import com.example.helloworld.dto.PostResponse;
import com.example.helloworld.model.Post;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.PostRepository;
import com.example.helloworld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Post operations.
 * Contains business logic for post management.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    /**
     * Create a new post.
     * Invalidates post-related caches and timeline caches.
     * @param request post creation request
     * @return created post response
     */
    @CacheEvict(value = {"allPosts", "postsByUser", "searchResults", "userTimeline", "userTimelinePaginated", "userTimelineByDate", "userTimelineFilteredPaginated"}, allEntries = true)
    public PostResponse createPost(PostRequest request) {
        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));
        
        // Validate text is not empty
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Post text cannot be empty");
        }
        
        // Create and save post
        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);
        
        Post savedPost = postRepository.save(post);
        return mapToResponse(savedPost);
    }
    
    /**
     * Get a post by ID.
     * Cached with 60-second TTL.
     * @param id post ID
     * @return post response
     */
    @Cacheable(value = "postById", key = "#id")
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        return mapToResponse(post);
    }
    
    /**
     * Get all posts with pagination and sorting by creation date (newest first).
     * Cached with 60-second TTL.
     * @param page page number (0-indexed)
     * @param size page size
     * @return list of post responses
     */
    @Cacheable(value = "paginatedPosts", key = "#page + '-' + #size")
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all posts (without pagination).
     * Cached with 60-second TTL.
     * @return list of all post responses
     */
    @Cacheable(value = "allPosts")
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll(Sort.by("createdAt").descending()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all posts by a specific user.
     * Cached with 60-second TTL.
     * @param userId user ID
     * @return list of post responses
     */
    @Cacheable(value = "postsByUser", key = "#userId")
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        return postRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Search posts by text content.
     * Cached with 60-second TTL.
     * @param searchText text to search for
     * @return list of matching post responses
     */
    @Cacheable(value = "searchResults", key = "#searchText")
    @Transactional(readOnly = true)
    public List<PostResponse> searchPosts(String searchText) {
        return postRepository.findByTextContainingIgnoreCase(searchText).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Update a post.
     * Invalidates post-related caches and timeline caches.
     * @param id post ID
     * @param request post update request
     * @return updated post response
     */
    @CacheEvict(value = {"postById", "allPosts", "paginatedPosts", "postsByUser", "searchResults", "userTimeline", "userTimelinePaginated", "userTimelineByDate", "userTimelineFilteredPaginated"}, allEntries = true)
    public PostResponse updatePost(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        
        // Validate text is not empty
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Post text cannot be empty");
        }
        
        post.setText(request.getText());
        
        Post updatedPost = postRepository.save(post);
        return mapToResponse(updatedPost);
    }
    
    /**
     * Delete a post.
     * Invalidates all post-related caches and timeline caches.
     * @param id post ID
     */
    @CacheEvict(value = {"postById", "allPosts", "paginatedPosts", "postsByUser", "searchResults", "userTimeline", "userTimelinePaginated", "userTimelineByDate", "userTimelineFilteredPaginated"}, allEntries = true)
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }
    
    /**
     * Map Post entity to PostResponse DTO.
     * @param post the post entity
     * @return post response DTO
     */
    private PostResponse mapToResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setText(post.getText());
        response.setUserId(post.getUser().getId());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        return response;
    }
}
