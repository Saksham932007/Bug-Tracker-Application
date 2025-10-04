package com.example.bugtracker.model.enums;

/**
 * Enumeration representing the status of a bug in the tracking system.
 * Bugs progress through these states from creation to closure.
 */
public enum Status {
    /**
     * Bug has been reported but not yet assigned or started
     */
    NEW,
    
    /**
     * Bug is currently being worked on by a developer
     */
    IN_PROGRESS,
    
    /**
     * Bug has been fixed by a developer and is ready for testing
     */
    RESOLVED,
    
    /**
     * Bug has been verified as fixed by a tester and is closed
     */
    CLOSED
}