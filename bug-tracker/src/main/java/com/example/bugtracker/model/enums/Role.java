package com.example.bugtracker.model.enums;

/**
 * Enumeration representing user roles in the bug tracking system.
 * Each role has different permissions within the application.
 */
public enum Role {
    /**
     * Project Manager - Can create projects, assign bugs, and manage overall project workflow
     */
    PROJECT_MANAGER,
    
    /**
     * Developer - Can update bug status and work on assigned bugs
     */
    DEVELOPER,
    
    /**
     * Tester - Can report new bugs and close resolved bugs after verification
     */
    TESTER
}