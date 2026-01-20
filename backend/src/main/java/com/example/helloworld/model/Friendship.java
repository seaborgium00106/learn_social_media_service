package com.example.helloworld.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a friendship relationship between two users.
 * This is a bidirectional friendship (if A is friends with B, then B is also friends with A).
 */
@Entity
@Table(name = "friendships", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "friend_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Constructor for creating a friendship relationship.
     * @param user the user initiating/owning the friendship
     * @param friend the user who is the friend
     */
    public Friendship(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }
}
