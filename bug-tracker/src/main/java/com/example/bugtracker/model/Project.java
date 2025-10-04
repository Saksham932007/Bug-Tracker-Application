package com.example.bugtracker.model;

/**
 * Represents a project in the bug tracking system.
 * Projects contain bugs and are managed by project managers.
 */
public class Project {
    private String id;
    private String name;
    private String description;

    /**
     * Default constructor for JSON deserialization
     */
    public Project() {
    }

    /**
     * Creates a new Project with the specified details
     * 
     * @param id          Unique identifier for the project
     * @param name        The project's name
     * @param description A description of the project
     */
    public Project(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id != null && id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}