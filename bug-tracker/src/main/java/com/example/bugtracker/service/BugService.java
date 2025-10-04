package com.example.bugtracker.service;

import com.example.bugtracker.model.*;
import com.example.bugtracker.model.enums.*;
import com.example.bugtracker.repository.DataRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for bug-related business logic.
 * Handles bug creation, assignment, status updates, and comment management.
 */
public class BugService {
    private final DataRepository repository;

    /**
     * Creates a new BugService with the specified repository
     */
    public BugService(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new bug report
     * 
     * @param title       The title of the bug
     * @param description The detailed description of the bug
     * @param projectId   The ID of the project this bug belongs to
     * @param reporterId  The ID of the user reporting the bug
     * @param priority    The priority level of the bug
     * @return The created bug
     */
    public Bug createBug(String title, String description, String projectId, 
                         String reporterId, Priority priority) {
        String bugId = repository.getNextBugId();
        Bug bug = new Bug(bugId, title, description, projectId, reporterId, priority);
        repository.addBug(bug);
        return bug;
    }

    /**
     * Gets all bugs in the system
     * 
     * @return List of all bugs
     */
    public List<Bug> getAllBugs() {
        return repository.getAllBugs();
    }

    /**
     * Gets all bugs for a specific project
     * 
     * @param projectId The project ID to filter by
     * @return List of bugs in the specified project
     */
    public List<Bug> getBugsByProject(String projectId) {
        return repository.getBugsByProject(projectId);
    }

    /**
     * Gets a bug by its ID
     * 
     * @param bugId The bug ID to look for
     * @return Bug with the specified ID or null if not found
     */
    public Bug getBugById(String bugId) {
        return repository.getBugById(bugId);
    }

    /**
     * Assigns a bug to a developer
     * 
     * @param bugId       The ID of the bug to assign
     * @param developerId The ID of the developer to assign to
     * @return true if assignment successful, false otherwise
     */
    public boolean assignBug(String bugId, String developerId) {
        Bug bug = getBugById(bugId);
        if (bug != null) {
            bug.assignTo(developerId);
            repository.updateBug(bug);
            return true;
        }
        return false;
    }

    /**
     * Updates the status of a bug
     * 
     * @param bugId     The ID of the bug to update
     * @param newStatus The new status for the bug
     * @return true if update successful, false otherwise
     */
    public boolean updateBugStatus(String bugId, Status newStatus) {
        Bug bug = getBugById(bugId);
        if (bug != null) {
            bug.updateStatus(newStatus);
            repository.updateBug(bug);
            return true;
        }
        return false;
    }

    /**
     * Adds a comment to a bug
     * 
     * @param bugId    The ID of the bug to comment on
     * @param authorId The ID of the user making the comment
     * @param text     The content of the comment
     * @return true if comment added successfully, false otherwise
     */
    public boolean addComment(String bugId, String authorId, String text) {
        Bug bug = getBugById(bugId);
        if (bug != null) {
            Comment comment = new Comment(authorId, text, LocalDateTime.now());
            bug.addComment(comment);
            repository.updateBug(bug);
            return true;
        }
        return false;
    }

    /**
     * Checks if a bug can be assigned (is in NEW or IN_PROGRESS status)
     * 
     * @param bugId The ID of the bug to check
     * @return true if bug can be assigned, false otherwise
     */
    public boolean canAssignBug(String bugId) {
        Bug bug = getBugById(bugId);
        return bug != null && (bug.getStatus() == Status.NEW || bug.getStatus() == Status.IN_PROGRESS);
    }

    /**
     * Checks if a bug can have its status updated by a developer
     * 
     * @param bugId        The ID of the bug to check
     * @param developerId  The ID of the developer
     * @return true if developer can update status, false otherwise
     */
    public boolean canDeveloperUpdateStatus(String bugId, String developerId) {
        Bug bug = getBugById(bugId);
        return bug != null && 
               developerId.equals(bug.getAssigneeId()) &&
               (bug.getStatus() == Status.NEW || bug.getStatus() == Status.IN_PROGRESS);
    }

    /**
     * Checks if a bug can be closed by a tester (must be RESOLVED status)
     * 
     * @param bugId The ID of the bug to check
     * @return true if bug can be closed, false otherwise
     */
    public boolean canCloseBug(String bugId) {
        Bug bug = getBugById(bugId);
        return bug != null && bug.getStatus() == Status.RESOLVED;
    }

    /**
     * Gets all available status transitions for a developer
     * 
     * @param currentStatus The current status of the bug
     * @return Array of valid next statuses
     */
    public Status[] getValidDeveloperStatusTransitions(Status currentStatus) {
        switch (currentStatus) {
            case NEW:
                return new Status[]{Status.IN_PROGRESS};
            case IN_PROGRESS:
                return new Status[]{Status.RESOLVED};
            default:
                return new Status[0];
        }
    }

    /**
     * Gets bugs assigned to a specific developer
     * 
     * @param developerId The ID of the developer
     * @return List of bugs assigned to the developer
     */
    public List<Bug> getBugsAssignedTo(String developerId) {
        return getAllBugs().stream()
                .filter(bug -> developerId.equals(bug.getAssigneeId()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Gets bugs reported by a specific user
     * 
     * @param reporterId The ID of the reporter
     * @return List of bugs reported by the user
     */
    public List<Bug> getBugsReportedBy(String reporterId) {
        return getAllBugs().stream()
                .filter(bug -> reporterId.equals(bug.getReporterId()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}