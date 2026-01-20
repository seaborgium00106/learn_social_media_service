package com.example.helloworld.service;

import com.example.helloworld.dto.UserRequest;
import com.example.helloworld.dto.UserResponse;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for User operations.
 * Contains business logic for user management.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * Create a new user.
     * Invalidates all user caches.
     * @param request user creation request
     * @return created user response
     */
    @CacheEvict(value = "allUsers", allEntries = true)
    public UserResponse createUser(UserRequest request) {
        // Validate username and email uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        
        // Create and save user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }
    
    /**
     * Get a user by ID.
     * Cached with 60-second TTL.
     * @param id user ID
     * @return user response
     */
    @Cacheable(value = "userById", key = "#id")
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return mapToResponse(user);
    }
    
    /**
     * Get a user by username.
     * Cached with 60-second TTL.
     * @param username the username
     * @return user response
     */
    @Cacheable(value = "userByUsername", key = "#username")
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return mapToResponse(user);
    }
    
    /**
     * Get all users.
     * Cached with 60-second TTL.
     * @return list of user responses
     */
    @Cacheable(value = "allUsers")
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Update a user.
     * Invalidates user caches for the updated user.
     * @param id user ID
     * @param request user update request
     * @return updated user response
     */
    @CacheEvict(value = {"userById", "userByUsername", "allUsers"}, allEntries = true)
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        
        // Check if username is being changed and if it's unique
        if (!user.getUsername().equals(request.getUsername()) 
                && userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }
        
        // Check if email is being changed and if it's unique
        if (!user.getEmail().equals(request.getEmail()) 
                && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        
        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }
    
    /**
     * Delete a user.
     * Invalidates all user caches.
     * @param id user ID
     */
    @CacheEvict(value = {"userById", "userByUsername", "allUsers"}, allEntries = true)
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    /**
     * Map User entity to UserResponse DTO.
     * @param user the user entity
     * @return user response DTO
     */
    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
