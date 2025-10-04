package com.example.bugtracker.service;

import com.example.bugtracker.model.Project;
import com.example.bugtracker.repository.DataRepository;
import java.util.List;

/**
 * Service class for project-related business logic.
 * Handles project creation, retrieval, and management operations.
 */
public class ProjectService {
    private final DataRepository repository;

    /**
     * Creates a new ProjectService with the specified repository
     */
    public ProjectService(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new project
     * 
     * @param name        The name of the project
     * @param description The description of the project
     * @return The created project
     */
    public Project createProject(String name, String description) {
        String projectId = repository.getNextProjectId();
        Project project = new Project(projectId, name, description);
        repository.addProject(project);
        return project;
    }

    /**
     * Gets all projects in the system
     * 
     * @return List of all projects
     */
    public List<Project> getAllProjects() {
        return repository.getAllProjects();
    }

    /**
     * Gets a project by its ID
     * 
     * @param projectId The project ID to look for
     * @return Project with the specified ID or null if not found
     */
    public Project getProjectById(String projectId) {
        return repository.getProjectById(projectId);
    }

    /**
     * Checks if a project exists with the given ID
     * 
     * @param projectId The project ID to check
     * @return true if project exists, false otherwise
     */
    public boolean projectExists(String projectId) {
        return getProjectById(projectId) != null;
    }

    /**
     * Validates that a project name is not empty and doesn't already exist
     * 
     * @param name The project name to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidProjectName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        return getAllProjects().stream()
                .noneMatch(project -> project.getName().equalsIgnoreCase(name.trim()));
    }
}