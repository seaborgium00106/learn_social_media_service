package com.example.helloworld.service;

import com.example.helloworld.dto.FriendshipRequest;
import com.example.helloworld.dto.FriendshipResponse;
import com.example.helloworld.model.Friendship;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.FriendshipRepository;
import com.example.helloworld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Friendship operations.
 * Contains business logic for managing friendships between users.
 * Implements bidirectional friendships with caching.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipService {
    
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    
    /**
     * Add a friendship between two users (bidirectional).
     * Creates two friendship records: user1->user2 and user2->user1
     * Invalidates relevant caches including timeline caches.
     * @param request containing userId and friendId
     * @return friendship response
     */
    @CacheEvict(value = {"userFriends", "friendCount", "userTimeline", "userTimelinePaginated", "userTimelineByDate", "userTimelineFilteredPaginated"}, allEntries = true)
    public FriendshipResponse addFriend(FriendshipRequest request) {
        Long userId = request.getUserId();
        Long friendId = request.getFriendId();
        
        // Validate users exist
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found with id: " + friendId));
        
        // Prevent self-friendship
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("A user cannot be friends with themselves");
        }
        
        // Check if friendship already exists
        if (friendshipRepository.existsByUserIdAndFriendId(userId, friendId)) {
            throw new IllegalArgumentException("Friendship already exists between user " + userId + " and " + friendId);
        }
        
        // Create bidirectional friendship: user -> friend
        Friendship friendship1 = new Friendship(user, friend);
        friendshipRepository.save(friendship1);
        
        // Create bidirectional friendship: friend -> user
        Friendship friendship2 = new Friendship(friend, user);
        friendshipRepository.save(friendship2);
        
        return mapToResponse(friendship1);
    }
    
    /**
     * Remove a friendship between two users (bidirectional).
     * Removes both friendship records if they exist.
     * Invalidates relevant caches including timeline caches.
     * @param request containing userId and friendId
     */
    @CacheEvict(value = {"userFriends", "friendCount", "userTimeline", "userTimelinePaginated", "userTimelineByDate", "userTimelineFilteredPaginated"}, allEntries = true)
    public void removeFriend(FriendshipRequest request) {
        Long userId = request.getUserId();
        Long friendId = request.getFriendId();
        
        // Validate users exist
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        if (!userRepository.existsById(friendId)) {
            throw new IllegalArgumentException("Friend not found with id: " + friendId);
        }
        
        // Check if friendship exists
        if (!friendshipRepository.existsByUserIdAndFriendId(userId, friendId)) {
            throw new IllegalArgumentException("Friendship does not exist between user " + userId + " and " + friendId);
        }
        
        // Remove bidirectional friendship
        friendshipRepository.deleteByUserIdAndFriendId(userId, friendId);
        friendshipRepository.deleteByUserIdAndFriendId(friendId, userId);
    }
    
    /**
     * Get all friends of a user.
     * Cached with 60-second TTL.
     * @param userId the user's ID
     * @return list of friend responses
     */
    @Cacheable(value = "userFriends", key = "#userId")
    @Transactional(readOnly = true)
    public List<FriendshipResponse> getFriendsOfUser(Long userId) {
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        return friendshipRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Check if two users are friends.
     * @param userId the first user's ID
     * @param friendId the second user's ID
     * @return true if they are friends, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean areFriends(Long userId, Long friendId) {
        return friendshipRepository.existsByUserIdAndFriendId(userId, friendId);
    }
    
    /**
     * Get the count of friends for a user.
     * Cached with 60-second TTL.
     * @param userId the user's ID
     * @return number of friends
     */
    @Cacheable(value = "friendCount", key = "#userId")
    @Transactional(readOnly = true)
    public long getFriendCount(Long userId) {
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        return friendshipRepository.countByUserId(userId);
    }
    
    /**
     * Map Friendship entity to FriendshipResponse DTO.
     * @param friendship the friendship entity
     * @return friendship response DTO
     */
    private FriendshipResponse mapToResponse(Friendship friendship) {
        FriendshipResponse response = new FriendshipResponse();
        response.setId(friendship.getId());
        response.setUserId(friendship.getUser().getId());
        response.setUsername(friendship.getUser().getUsername());
        response.setFriendId(friendship.getFriend().getId());
        response.setFriendUsername(friendship.getFriend().getUsername());
        response.setCreatedAt(friendship.getCreatedAt());
        return response;
    }
}
