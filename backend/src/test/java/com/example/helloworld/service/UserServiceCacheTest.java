package com.example.helloworld.service;

import com.example.helloworld.dto.UserRequest;
import com.example.helloworld.dto.UserResponse;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test to verify caching behavior in UserService.
 */
@SpringBootTest
public class UserServiceCacheTest {

    @Autowired
    private UserService userService;

    @SpyBean
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testGetUserById_CacheHit() {
        // Create a test user
        UserRequest request = new UserRequest();
        request.setUsername("cachetest");
        request.setEmail("cache@test.com");
        
        UserResponse created = userService.createUser(request);
        Long userId = created.getId();
        
        // Clear the spy to reset invocation counts
        clearInvocations(userRepository);
        
        // First call - should hit database
        UserResponse response1 = userService.getUserById(userId);
        assertNotNull(response1);
        assertEquals("cachetest", response1.getUsername());
        
        // Verify repository was called once
        verify(userRepository, times(1)).findById(userId);
        
        // Second call - should hit cache (no database call)
        UserResponse response2 = userService.getUserById(userId);
        assertNotNull(response2);
        assertEquals("cachetest", response2.getUsername());
        
        // Verify repository was STILL called only once (cache hit!)
        verify(userRepository, times(1)).findById(userId);
        
        // Verify both responses are equal
        assertEquals(response1.getUsername(), response2.getUsername());
        assertEquals(response1.getEmail(), response2.getEmail());
        
        System.out.println("✓ Cache is working! Second call did not hit the database.");
    }

    @Test
    public void testGetUserByUsername_CacheHit() {
        // Create a test user
        UserRequest request = new UserRequest();
        request.setUsername("cachetest2");
        request.setEmail("cache2@test.com");
        
        userService.createUser(request);
        
        // Clear the spy to reset invocation counts
        clearInvocations(userRepository);
        
        // First call - should hit database
        UserResponse response1 = userService.getUserByUsername("cachetest2");
        assertNotNull(response1);
        
        // Verify repository was called once
        verify(userRepository, times(1)).findByUsername("cachetest2");
        
        // Second call - should hit cache
        UserResponse response2 = userService.getUserByUsername("cachetest2");
        assertNotNull(response2);
        
        // Verify repository was STILL called only once (cache hit!)
        verify(userRepository, times(1)).findByUsername("cachetest2");
        
        System.out.println("✓ Cache is working for getUserByUsername!");
    }

    @Test
    public void testUpdateUser_CacheEviction() {
        // Create a test user
        UserRequest request = new UserRequest();
        request.setUsername("evictiontest");
        request.setEmail("eviction@test.com");
        
        UserResponse created = userService.createUser(request);
        Long userId = created.getId();
        
        // Clear the spy
        clearInvocations(userRepository);
        
        // First call - populates cache
        userService.getUserById(userId);
        verify(userRepository, times(1)).findById(userId);
        
        // Second call - cache hit
        userService.getUserById(userId);
        verify(userRepository, times(1)).findById(userId);
        
        // Update the user - should evict cache
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("evictiontest");
        updateRequest.setEmail("updated@test.com");
        userService.updateUser(userId, updateRequest);
        
        // Clear spy again
        clearInvocations(userRepository);
        
        // Next call should hit database again (cache was evicted)
        UserResponse afterUpdate = userService.getUserById(userId);
        verify(userRepository, times(1)).findById(userId);
        assertEquals("updated@test.com", afterUpdate.getEmail());
        
        System.out.println("✓ Cache eviction is working on update!");
    }

    @Test
    public void testCacheManagerConfiguration() {
        // Verify cache manager is configured
        assertNotNull(cacheManager);
        
        // Check that our caches exist
        assertNotNull(cacheManager.getCache("userById"));
        assertNotNull(cacheManager.getCache("userByUsername"));
        assertNotNull(cacheManager.getCache("allUsers"));
        
        System.out.println("✓ Cache manager is properly configured with all caches!");
    }
}
