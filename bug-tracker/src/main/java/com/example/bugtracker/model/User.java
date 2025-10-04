package com.example.bugtracker.model;

import com.example.bugtracker.model.enums.Role;

/**
 * Represents a user in the bug tracking system.
 * Each user has a unique ID, username, and role that determines their permissions.
 */
public class User {
    private String id;
    private String username;
    private Role role;

    /**
     * Default constructor for JSON deserialization
     */
    public User() {
    }

    /**
     * Creates a new User with the specified details
     * 
     * @param id       Unique identifier for the user
     * @param username The user's login name
     * @param role     The user's role determining their permissions
     */
    public User(String id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}