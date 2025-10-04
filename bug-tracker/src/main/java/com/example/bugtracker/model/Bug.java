package com.example.bugtracker.model;

import com.example.bugtracker.model.enums.Priority;
import com.example.bugtracker.model.enums.Status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bug in the tracking system.
 * Bugs are reported by testers, assigned to developers, and tracked through various states.
 */
public class Bug {
    private String id;
    private String title;
    private String description;
    private String projectId;
    private String reporterId;
    private String assigneeId;
    private Status status;
    private Priority priority;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<Comment> comments;

    /**
     * Default constructor for JSON deserialization
     */
    public Bug() {
        this.comments = new ArrayList<>();
    }

    /**
     * Creates a new Bug with the specified details
     * 
     * @param id          Unique identifier for the bug
     * @param title       Short title describing the bug
     * @param description Detailed description of the bug
     * @param projectId   ID of the project this bug belongs to
     * @param reporterId  ID of the user who reported the bug
     * @param priority    Priority level of the bug
     */
    public Bug(String id, String title, String description, String projectId, 
               String reporterId, Priority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.projectId = projectId;
        this.reporterId = reporterId;
        this.priority = priority;
        this.status = Status.NEW;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
        this.comments = new ArrayList<>();
    }

    /**
     * Adds a comment to this bug and updates the modification time
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.updatedDate = LocalDateTime.now();
    }

    /**
     * Updates the bug status and modification time
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
        this.updatedDate = LocalDateTime.now();
    }

    /**
     * Assigns the bug to a developer and updates modification time
     */
    public void assignTo(String developerId) {
        this.assigneeId = developerId;
        this.updatedDate = LocalDateTime.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments != null ? comments : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Bug{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", assigneeId='" + assigneeId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bug bug = (Bug) o;
        return id != null && id.equals(bug.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}