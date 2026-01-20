package com.example.helloworld.controller;

import com.example.helloworld.dto.TimelinePostResponse;
import com.example.helloworld.service.TimelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing user timelines.
 * Aggregates posts from all friends to create a personalized feed.
 */
@RestController
@RequestMapping("/api/v1/timeline")
@RequiredArgsConstructor
@Tag(name = "Timeline", description = "User timeline and feed management APIs")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowedHeaders = "*", maxAge = 3600)
public class TimelineController {
    
    private final TimelineService timelineService;
    
    /**
     * Get a user's complete timeline (all posts from friends).
     * 
     * @param userId the user's ID
     * @return list of timeline posts sorted by creation date (newest first)
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user timeline", description = "Retrieves all posts from the user's friends, sorted by creation date (newest first)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved timeline"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<TimelinePostResponse>> getUserTimeline(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        List<TimelinePostResponse> timeline = timelineService.getUserTimeline(userId);
        return ResponseEntity.ok(timeline);
    }
    
    /**
     * Get a user's timeline with pagination support.
     * 
     * @param userId the user's ID
     * @param page page number (0-indexed, default: 0)
     * @param size page size (default: 10)
     * @return page of timeline posts
     */
    @GetMapping("/user/{userId}/paginated")
    @Operation(summary = "Get paginated user timeline", description = "Retrieves posts from the user's friends with pagination support")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated timeline"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Page<TimelinePostResponse>> getUserTimelineWithPagination(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Page<TimelinePostResponse> timelinePage = timelineService.getUserTimelineWithPagination(userId, page, size);
        return ResponseEntity.ok(timelinePage);
    }
    
    /**
     * Get a user's timeline filtered by date range.
     * 
     * @param userId the user's ID
     * @param fromDate start date (inclusive, ISO format: yyyy-MM-dd'T'HH:mm:ss)
     * @param toDate end date (inclusive, ISO format: yyyy-MM-dd'T'HH:mm:ss)
     * @return list of timeline posts within the date range
     */
    @GetMapping("/user/{userId}/daterange")
    @Operation(summary = "Get timeline by date range", description = "Retrieves posts from the user's friends filtered by date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered timeline"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<TimelinePostResponse>> getUserTimelineByDateRange(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "Start date (ISO format: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @Parameter(description = "End date (ISO format: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        List<TimelinePostResponse> timeline = timelineService.getUserTimelineByDateRange(userId, fromDate, toDate);
        return ResponseEntity.ok(timeline);
    }
    
    /**
     * Get a user's timeline with both pagination and date range filtering.
     * 
     * @param userId the user's ID
     * @param page page number (0-indexed, default: 0)
     * @param size page size (default: 10)
     * @param fromDate start date (inclusive, ISO format: yyyy-MM-dd'T'HH:mm:ss)
     * @param toDate end date (inclusive, ISO format: yyyy-MM-dd'T'HH:mm:ss)
     * @return page of timeline posts within the date range
     */
    @GetMapping("/user/{userId}/filtered")
    @Operation(summary = "Get filtered and paginated timeline", description = "Retrieves posts from the user's friends with both pagination and date range filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered and paginated timeline"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Page<TimelinePostResponse>> getUserTimelineWithPaginationAndDateRange(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Start date (ISO format: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @Parameter(description = "End date (ISO format: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        Page<TimelinePostResponse> timelinePage = timelineService.getUserTimelineWithPaginationAndDateRange(
                userId, page, size, fromDate, toDate);
        return ResponseEntity.ok(timelinePage);
    }
    
    /**
     * Get the count of posts in a user's timeline.
     * 
     * @param userId the user's ID
     * @return count of timeline posts
     */
    @GetMapping("/user/{userId}/count")
    @Operation(summary = "Get timeline post count", description = "Returns the total number of posts in the user's timeline")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, Long>> getTimelinePostCount(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        long count = timelineService.getTimelinePostCount(userId);
        return ResponseEntity.ok(Map.of("userId", userId, "postCount", count));
    }
    
    /**
     * Get the count of posts in a user's timeline within a date range.
     * 
     * @param userId the user's ID
     * @param fromDate start date (inclusive)
     * @param toDate end date (inclusive)
     * @return count of timeline posts within date range
     */
    @GetMapping("/user/{userId}/count/daterange")
    @Operation(summary = "Get timeline post count by date range", description = "Returns the number of posts in the user's timeline within a date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, Object>> getTimelinePostCountByDateRange(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "Start date (ISO format: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @Parameter(description = "End date (ISO format: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        long count = timelineService.getTimelinePostCountByDateRange(userId, fromDate, toDate);
        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "postCount", count,
                "fromDate", fromDate != null ? fromDate.toString() : "null",
                "toDate", toDate != null ? toDate.toString() : "null"
        ));
    }
}
