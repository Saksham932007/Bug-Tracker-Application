package com.example.bugtracker.model.enums;

/**
 * Enumeration representing the priority level of a bug.
 * Used to help prioritize which bugs should be addressed first.
 */
public enum Priority {
    /**
     * Low priority - Minor issues that don't affect core functionality
     */
    LOW,
    
    /**
     * Medium priority - Issues that affect functionality but have workarounds
     */
    MEDIUM,
    
    /**
     * High priority - Critical issues that significantly impact functionality
     */
    HIGH
}