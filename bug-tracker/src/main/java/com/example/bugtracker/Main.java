package com.example.bugtracker;

import com.example.bugtracker.repository.DataRepository;
import com.example.bugtracker.service.*;
import com.example.bugtracker.ui.ConsoleUI;

/**
 * Main entry point for the Bug Tracking Application.
 * Initializes all components and starts the user interface.
 */
public class Main {
    
    /**
     * Main method that starts the application
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Initialize the data repository
            DataRepository repository = new DataRepository();
            
            // Initialize the service layer
            UserService userService = new UserService(repository);
            ProjectService projectService = new ProjectService(repository);
            BugService bugService = new BugService(repository);
            
            // Initialize and start the console UI
            ConsoleUI consoleUI = new ConsoleUI(userService, projectService, bugService);
            consoleUI.start();
            
        } catch (Exception e) {
            System.err.println("An error occurred while starting the application:");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}