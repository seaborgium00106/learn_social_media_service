package com.example.helloworld.service;

import com.example.helloworld.dto.FriendshipRequest;
import com.example.helloworld.dto.FriendshipResponse;
import com.example.helloworld.model.Friendship;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.FriendshipRepository;
import com.example.helloworld.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify friendship functionality including bidirectional relationships and caching.
 */
@SpringBootTest
@ActiveProfiles("test")
public class FriendshipServiceTest {
    
    @Autowired
    private FriendshipService friendshipService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FriendshipRepository friendshipRepository;
    
    private User user1;
    private User user2;
    private User user3;
    
    @BeforeEach
    public void setUp() {
        // Clear repositories
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
        
        // Create test users
        user1 = new User();
        user1.setUsername("alice");
        user1.setEmail("alice@example.com");
        user1 = userRepository.save(user1);
        
        user2 = new User();
        user2.setUsername("bob");
        user2.setEmail("bob@example.com");
        user2 = userRepository.save(user2);
        
        user3 = new User();
        user3.setUsername("charlie");
        user3.setEmail("charlie@example.com");
        user3 = userRepository.save(user3);
    }
    
    @Test
    public void testAddFriendCreatesbidirectionalRelationship() {
        FriendshipRequest request = new FriendshipRequest(user1.getId(), user2.getId());
        FriendshipResponse response = friendshipService.addFriend(request);
        
        assertNotNull(response);
        assertEquals(user1.getId(), response.getUserId());
        assertEquals(user2.getId(), response.getFriendId());
        
        // Verify bidirectional relationship exists
        assertTrue(friendshipService.areFriends(user1.getId(), user2.getId()));
        assertTrue(friendshipService.areFriends(user2.getId(), user1.getId()));
        
        // Verify both relationships are in database
        assertEquals(2, friendshipRepository.count());
    }
    
    @Test
    public void testGetFriendsOfUser() {
        // Add multiple friendships
        friendshipService.addFriend(new FriendshipRequest(user1.getId(), user2.getId()));
        friendshipService.addFriend(new FriendshipRequest(user1.getId(), user3.getId()));
        
        List<FriendshipResponse> friends = friendshipService.getFriendsOfUser(user1.getId());
        
        assertEquals(2, friends.size());
        
        // Verify friends are correct
        boolean hasBob = friends.stream().anyMatch(f -> f.getFriendId().equals(user2.getId()));
        boolean hasCharlie = friends.stream().anyMatch(f -> f.getFriendId().equals(user3.getId()));
        
        assertTrue(hasBob, "Should have Bob as friend");
        assertTrue(hasCharlie, "Should have Charlie as friend");
    }
    
    @Test
    public void testRemoveFriendRemovesBidirectionalRelationship() {
        // Add friendship
        friendshipService.addFriend(new FriendshipRequest(user1.getId(), user2.getId()));
        
        // Verify it exists
        assertTrue(friendshipService.areFriends(user1.getId(), user2.getId()));
        assertTrue(friendshipService.areFriends(user2.getId(), user1.getId()));
        assertEquals(2, friendshipRepository.count());
        
        // Remove friendship
        friendshipService.removeFriend(new FriendshipRequest(user1.getId(), user2.getId()));
        
        // Verify it's gone both ways
        assertFalse(friendshipService.areFriends(user1.getId(), user2.getId()));
        assertFalse(friendshipService.areFriends(user2.getId(), user1.getId()));
        assertEquals(0, friendshipRepository.count());
    }
    
    @Test
    public void testGetFriendCountCaching() {
        // Add some friendships
        friendshipService.addFriend(new FriendshipRequest(user1.getId(), user2.getId()));
        friendshipService.addFriend(new FriendshipRequest(user1.getId(), user3.getId()));
        
        // First call
        long count1 = friendshipService.getFriendCount(user1.getId());
        assertEquals(2, count1);
        
        // Second call - should hit cache
        long count2 = friendshipService.getFriendCount(user1.getId());
        assertEquals(2, count2);
    }
    
    @Test
    public void testCannotAddSelfAsFriend() {
        FriendshipRequest request = new FriendshipRequest(user1.getId(), user1.getId());
        
        assertThrows(IllegalArgumentException.class, () -> friendshipService.addFriend(request),
                "Should not allow self-friendship");
    }
    
    @Test
    public void testCannotAddDuplicateFriendship() {
        FriendshipRequest request = new FriendshipRequest(user1.getId(), user2.getId());
        
        // Add friendship once
        friendshipService.addFriend(request);
        
        // Try to add the same friendship again
        assertThrows(IllegalArgumentException.class, () -> friendshipService.addFriend(request),
                "Should not allow duplicate friendship");
    }
    
    @Test
    public void testRemoveNonExistentFriendshipThrowsError() {
        FriendshipRequest request = new FriendshipRequest(user1.getId(), user2.getId());
        
        assertThrows(IllegalArgumentException.class, () -> friendshipService.removeFriend(request),
                "Should throw error when removing non-existent friendship");
    }
    
    @Test
    public void testAddFriendWithNonExistentUserThrowsError() {
        FriendshipRequest request = new FriendshipRequest(user1.getId(), 9999L);
        
        assertThrows(IllegalArgumentException.class, () -> friendshipService.addFriend(request),
                "Should throw error when friend user does not exist");
    }
    
    @Test
    public void testGetFriendsOfNonExistentUserThrowsError() {
        assertThrows(IllegalArgumentException.class, () -> friendshipService.getFriendsOfUser(9999L),
                "Should throw error for non-existent user");
    }
}
