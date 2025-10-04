package com.example.bugtracker.model;

import java.time.LocalDateTime;

/**
 * Represents a comment on a bug in the tracking system.
 * Comments allow users to add notes, updates, and discussions to bugs.
 */
public class Comment {
    private String authorId;
    private String text;
    private LocalDateTime timestamp;

    /**
     * Default constructor for JSON deserialization
     */
    public Comment() {
    }

    /**
     * Creates a new Comment with the specified details
     * 
     * @param authorId  ID of the user who made the comment
     * @param text      The content of the comment
     * @param timestamp When the comment was made
     */
    public Comment(String authorId, String text, LocalDateTime timestamp) {
        this.authorId = authorId;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "authorId='" + authorId + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}