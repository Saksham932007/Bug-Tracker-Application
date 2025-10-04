package com.example.bugtracker.service;

import com.example.bugtracker.model.User;
import com.example.bugtracker.model.enums.Role;
import com.example.bugtracker.repository.DataRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for user-related business logic.
 * Handles user authentication and user management operations.
 */
public class UserService {
    private final DataRepository repository;
    private User currentUser;

    /**
     * Creates a new UserService with the specified repository
     */
    public UserService(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * Attempts to log in a user with the given username
     * 
     * @param username The username to authenticate
     * @return true if login successful, false otherwise
     */
    public boolean login(String username) {
        User user = repository.getUserByUsername(username);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Gets the currently logged-in user
     * 
     * @return Current user or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if a user is currently logged in
     * 
     * @return true if user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Checks if the current user has the specified role
     * 
     * @param role The role to check for
     * @return true if current user has the role, false otherwise
     */
    public boolean hasRole(Role role) {
        return currentUser != null && currentUser.getRole() == role;
    }

    /**
     * Gets all users in the system
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    /**
     * Gets a user by their ID
     * 
     * @param userId The user ID to look for
     * @return User with the specified ID or null if not found
     */
    public User getUserById(String userId) {
        return repository.getUserById(userId);
    }

    /**
     * Gets a user by their username
     * 
     * @param username The username to look for
     * @return User with the specified username or null if not found
     */
    public User getUserByUsername(String username) {
        return repository.getUserByUsername(username);
    }

    /**
     * Gets all users with the specified role
     * 
     * @param role The role to filter by
     * @return List of users with the specified role
     */
    public List<User> getUsersByRole(Role role) {
        return getAllUsers().stream()
                .filter(user -> user.getRole() == role)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Creates a new user (for future expansion)
     * 
     * @param username The username for the new user
     * @param role     The role for the new user
     * @return The created user
     */
    public User createUser(String username, Role role) {
        String userId = repository.getNextUserId();
        User user = new User(userId, username, role);
        repository.addUser(user);
        return user;
    }
}