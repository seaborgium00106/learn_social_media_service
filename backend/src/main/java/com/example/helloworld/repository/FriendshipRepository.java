package com.example.helloworld.repository;

import com.example.helloworld.model.Friendship;
import com.example.helloworld.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Friendship entity.
 * Provides database operations for managing friendships.
 */
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    
    /**
     * Find all friends of a specific user.
     * @param userId the user's ID
     * @return list of friendships
     */
    @Query("SELECT f FROM Friendship f WHERE f.user.id = :userId")
    List<Friendship> findByUserId(@Param("userId") Long userId);
    
    /**
     * Check if a friendship exists between two users (directional).
     * @param userId the user's ID
     * @param friendId the friend's ID
     * @return true if friendship exists
     */
    boolean existsByUserIdAndFriendId(Long userId, Long friendId);
    
    /**
     * Find a specific friendship relationship.
     * @param userId the user's ID
     * @param friendId the friend's ID
     * @return optional friendship
     */
    Optional<Friendship> findByUserIdAndFriendId(Long userId, Long friendId);
    
    /**
     * Delete a friendship relationship.
     * @param userId the user's ID
     * @param friendId the friend's ID
     */
    void deleteByUserIdAndFriendId(Long userId, Long friendId);
    
    /**
     * Count the number of friends for a user.
     * @param userId the user's ID
     * @return count of friends
     */
    long countByUserId(Long userId);
}
