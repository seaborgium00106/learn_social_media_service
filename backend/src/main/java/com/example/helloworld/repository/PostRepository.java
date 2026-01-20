package com.example.helloworld.repository;

import com.example.helloworld.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Post entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * Find all posts by a specific user.
     * @param userId the ID of the user
     * @return list of posts by the user
     */
    List<Post> findByUserId(Long userId);
    
    /**
     * Find all posts by a specific user with pagination.
     * @param userId the ID of the user
     * @param pageable pagination information
     * @return page of posts by the user
     */
    Page<Post> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find posts containing a specific text (search functionality).
     * @param text the text to search for
     * @return list of posts containing the text
     */
    List<Post> findByTextContainingIgnoreCase(String text);
    
    /**
     * Find posts containing a specific text with pagination.
     * @param text the text to search for
     * @param pageable pagination information
     * @return page of posts containing the text
     */
    Page<Post> findByTextContainingIgnoreCase(String text, Pageable pageable);
    
    /**
     * Find all posts by multiple users (used for timeline aggregation).
     * @param userIds list of user IDs
     * @return list of posts by the specified users
     */
    List<Post> findByUserIdIn(List<Long> userIds);
}
