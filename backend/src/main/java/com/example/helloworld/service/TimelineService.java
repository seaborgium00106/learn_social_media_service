package com.example.helloworld.service;

import com.example.helloworld.dto.TimelinePostResponse;
import com.example.helloworld.model.Post;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.PostRepository;
import com.example.helloworld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Timeline operations.
 * Aggregates posts from all friends of a user to create a personalized timeline.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineService {
    
    private final FriendshipService friendshipService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    /**
     * Get a user's timeline (aggregated posts from all friends).
     * Sorted by creation date (newest first).
     * Cached with 60-second TTL.
     * 
     * @param userId the user's ID
     * @return list of timeline post responses
     */
    @Cacheable(value = "userTimeline", key = "#userId")
    public List<TimelinePostResponse> getUserTimeline(Long userId) {
        return getUserTimelineInternal(userId, null, null);
    }
    
    /**
     * Get a user's timeline with pagination support.
     * Sorted by creation date (newest first).
     * Cached with page-specific key.
     * 
     * @param userId the user's ID
     * @param page page number (0-indexed)
     * @param size page size
     * @return page of timeline post responses
     */
    @Cacheable(value = "userTimelinePaginated", key = "#userId + '-' + #page + '-' + #size")
    public Page<TimelinePostResponse> getUserTimelineWithPagination(Long userId, int page, int size) {
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        // Get all posts (full list)
        List<TimelinePostResponse> allPosts = getUserTimelineInternal(userId, null, null);
        
        // Apply pagination
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allPosts.size());
        
        List<TimelinePostResponse> pageContent = allPosts.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allPosts.size());
    }
    
    /**
     * Get a user's timeline filtered by date range.
     * Includes only posts created between fromDate and toDate (inclusive).
     * Sorted by creation date (newest first).
     * Cached with date-specific key.
     * 
     * @param userId the user's ID
     * @param fromDate start date (inclusive), null for no lower bound
     * @param toDate end date (inclusive), null for no upper bound
     * @return list of timeline post responses within date range
     */
    @Cacheable(value = "userTimelineByDate", key = "#userId + '-' + (#fromDate != null ? #fromDate.toString() : 'null') + '-' + (#toDate != null ? #toDate.toString() : 'null')")
    public List<TimelinePostResponse> getUserTimelineByDateRange(Long userId, LocalDateTime fromDate, LocalDateTime toDate) {
        return getUserTimelineInternal(userId, fromDate, toDate);
    }
    
    /**
     * Get a user's timeline with pagination and date range filtering.
     * Sorted by creation date (newest first).
     * Cached with full key.
     * 
     * @param userId the user's ID
     * @param page page number (0-indexed)
     * @param size page size
     * @param fromDate start date (inclusive), null for no lower bound
     * @param toDate end date (inclusive), null for no upper bound
     * @return page of timeline post responses
     */
    @Cacheable(value = "userTimelineFilteredPaginated", 
               key = "#userId + '-' + #page + '-' + #size + '-' + (#fromDate != null ? #fromDate.toString() : 'null') + '-' + (#toDate != null ? #toDate.toString() : 'null')")
    public Page<TimelinePostResponse> getUserTimelineWithPaginationAndDateRange(
            Long userId, 
            int page, 
            int size, 
            LocalDateTime fromDate, 
            LocalDateTime toDate) {
        
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        // Get all posts with date filtering
        List<TimelinePostResponse> allPosts = getUserTimelineInternal(userId, fromDate, toDate);
        
        // Apply pagination
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allPosts.size());
        
        List<TimelinePostResponse> pageContent = allPosts.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allPosts.size());
    }
    
    /**
     * Get the count of posts in a user's timeline.
     * 
     * @param userId the user's ID
     * @return count of posts in timeline
     */
    public long getTimelinePostCount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        return getUserTimeline(userId).size();
    }
    
    /**
     * Get the count of posts in a user's timeline within a date range.
     * 
     * @param userId the user's ID
     * @param fromDate start date (inclusive)
     * @param toDate end date (inclusive)
     * @return count of posts in timeline within date range
     */
    public long getTimelinePostCountByDateRange(Long userId, LocalDateTime fromDate, LocalDateTime toDate) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        return getUserTimelineByDateRange(userId, fromDate, toDate).size();
    }
    
    /**
     * Internal method to fetch and aggregate timeline posts.
     * Fetches all friends of the user, then fetches their posts, combines and sorts them.
     * 
     * @param userId the user's ID
     * @param fromDate start date (inclusive), null for no lower bound
     * @param toDate end date (inclusive), null for no upper bound
     * @return sorted list of timeline post responses
     */
    private List<TimelinePostResponse> getUserTimelineInternal(Long userId, LocalDateTime fromDate, LocalDateTime toDate) {
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        // Get all friends of the user
        var friendships = friendshipService.getFriendsOfUser(userId);
        
        // Extract friend IDs
        List<Long> friendIds = friendships.stream()
                .map(f -> f.getFriendId())
                .collect(Collectors.toList());
        
        // If user has no friends, return empty timeline
        if (friendIds.isEmpty()) {
            return List.of();
        }
        
        // Fetch all posts from all friends
        List<Post> allFriendsPosts = postRepository.findByUserIdIn(friendIds);
        
        // Apply date filtering if specified
        if (fromDate != null || toDate != null) {
            allFriendsPosts = allFriendsPosts.stream()
                    .filter(post -> {
                        if (fromDate != null && post.getCreatedAt().isBefore(fromDate)) {
                            return false;
                        }
                        if (toDate != null && post.getCreatedAt().isAfter(toDate)) {
                            return false;
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }
        
        // Map to response DTOs and sort by creation date (newest first)
        return allFriendsPosts.stream()
                .map(this::mapToTimelineResponse)
                .sorted(Comparator.comparing(TimelinePostResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * Map Post entity to TimelinePostResponse DTO with friend username.
     * 
     * @param post the post entity
     * @return timeline post response DTO
     */
    private TimelinePostResponse mapToTimelineResponse(Post post) {
        TimelinePostResponse response = new TimelinePostResponse();
        response.setId(post.getId());
        response.setText(post.getText());
        response.setUserId(post.getUser().getId());
        response.setUsername(post.getUser().getUsername());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        return response;
    }
}
