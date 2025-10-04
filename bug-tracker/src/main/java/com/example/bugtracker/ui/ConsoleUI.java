package com.example.bugtracker.ui;

import com.example.bugtracker.model.*;
import com.example.bugtracker.model.enums.*;
import com.example.bugtracker.service.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console user interface for the bug tracking application.
 * Provides menu-driven interaction with role-based access control.
 */
public class ConsoleUI {
    private final Scanner scanner;
    private final UserService userService;
    private final ProjectService projectService;
    private final BugService bugService;
    
    private Project currentProject;
    private Bug currentBug;

    /**
     * Creates a new ConsoleUI with the specified services
     */
    public ConsoleUI(UserService userService, ProjectService projectService, BugService bugService) {
        this.scanner = new Scanner(System.in);
        this.userService = userService;
        this.projectService = projectService;
        this.bugService = bugService;
    }

    /**
     * Starts the main application loop
     */
    public void start() {
        System.out.println("=================================");
        System.out.println("Welcome to the Bug Tracker!");
        System.out.println("=================================");
        
        if (login()) {
            showMainMenu();
        } else {
            System.out.println("Login failed. Exiting...");
        }
        
        scanner.close();
    }

    /**
     * Handles user login
     */
    private boolean login() {
        System.out.println("\nAvailable users for demo:");
        System.out.println("- manager1 (Project Manager)");
        System.out.println("- dev1, dev2 (Developers)");
        System.out.println("- tester1, tester2 (Testers)");
        
        for (int attempts = 0; attempts < 3; attempts++) {
            System.out.print("\nEnter username to log in: ");
            String username = scanner.nextLine().trim();
            
            if (userService.login(username)) {
                User currentUser = userService.getCurrentUser();
                System.out.println("Welcome, " + currentUser.getUsername() + 
                                 " (" + currentUser.getRole() + ")!");
                return true;
            } else {
                System.out.println("Invalid username. Please try again.");
            }
        }
        return false;
    }

    /**
     * Shows the main menu with role-based options
     */
    private void showMainMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("MAIN MENU");
            System.out.println("=".repeat(40));
            System.out.println("1. Select Project");
            
            if (userService.hasRole(Role.PROJECT_MANAGER)) {
                System.out.println("2. Create New Project (Managers Only)");
            }
            
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    selectProject();
                    break;
                case "2":
                    if (userService.hasRole(Role.PROJECT_MANAGER)) {
                        createProject();
                    } else {
                        System.out.println("Access denied. Only Project Managers can create projects.");
                    }
                    break;
                case "3":
                    System.out.println("Thank you for using Bug Tracker. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Handles project selection
     */
    private void selectProject() {
        List<Project> projects = projectService.getAllProjects();
        
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(40));
        System.out.println("AVAILABLE PROJECTS");
        System.out.println("=".repeat(40));
        
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            System.out.println((i + 1) + ". " + project.getName() + 
                             " - " + project.getDescription());
        }
        
        System.out.print("\nSelect a project (number): ");
        String choice = scanner.nextLine().trim();
        
        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < projects.size()) {
                currentProject = projects.get(index);
                showProjectMenu();
            } else {
                System.out.println("Invalid project selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Handles project creation (Project Manager only)
     */
    private void createProject() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("CREATE NEW PROJECT");
        System.out.println("=".repeat(40));
        
        System.out.print("Enter project name: ");
        String name = scanner.nextLine().trim();
        
        if (!projectService.isValidProjectName(name)) {
            System.out.println("Invalid or duplicate project name.");
            return;
        }
        
        System.out.print("Enter project description: ");
        String description = scanner.nextLine().trim();
        
        Project project = projectService.createProject(name, description);
        System.out.println("Project '" + project.getName() + "' created successfully!");
    }

    /**
     * Shows the project menu with role-based options
     */
    private void showProjectMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("PROJECT: " + currentProject.getName());
            System.out.println("=".repeat(40));
            System.out.println("1. View All Bugs");
            
            if (userService.hasRole(Role.TESTER)) {
                System.out.println("2. Report a New Bug (Testers Only)");
            }
            
            System.out.println("3. Select a Bug by ID");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    viewAllBugs();
                    break;
                case "2":
                    if (userService.hasRole(Role.TESTER)) {
                        reportBug();
                    } else {
                        System.out.println("Access denied. Only Testers can report bugs.");
                    }
                    break;
                case "3":
                    selectBug();
                    break;
                case "4":
                    currentProject = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Displays all bugs in the current project
     */
    private void viewAllBugs() {
        List<Bug> bugs = bugService.getBugsByProject(currentProject.getId());
        
        if (bugs.isEmpty()) {
            System.out.println("No bugs found in this project.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("BUGS IN PROJECT: " + currentProject.getName());
        System.out.println("=".repeat(60));
        System.out.printf("%-4s %-20s %-12s %-8s %-15s%n", 
                         "ID", "Title", "Status", "Priority", "Assigned To");
        System.out.println("-".repeat(60));
        
        for (Bug bug : bugs) {
            String assignee = "Unassigned";
            if (bug.getAssigneeId() != null) {
                User assignedUser = userService.getUserById(bug.getAssigneeId());
                if (assignedUser != null) {
                    assignee = assignedUser.getUsername();
                }
            }
            
            System.out.printf("%-4s %-20s %-12s %-8s %-15s%n",
                             bug.getId(),
                             truncate(bug.getTitle(), 20),
                             bug.getStatus(),
                             bug.getPriority(),
                             assignee);
        }
    }

    /**
     * Handles bug reporting (Tester only)
     */
    private void reportBug() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("REPORT NEW BUG");
        System.out.println("=".repeat(40));
        
        System.out.print("Enter bug title: ");
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println("Bug title cannot be empty.");
            return;
        }
        
        System.out.print("Enter bug description: ");
        String description = scanner.nextLine().trim();
        
        if (description.isEmpty()) {
            System.out.println("Bug description cannot be empty.");
            return;
        }
        
        System.out.println("Select priority:");
        System.out.println("1. LOW");
        System.out.println("2. MEDIUM");
        System.out.println("3. HIGH");
        System.out.print("Choose priority (1-3): ");
        
        String priorityChoice = scanner.nextLine().trim();
        Priority priority;
        
        switch (priorityChoice) {
            case "1":
                priority = Priority.LOW;
                break;
            case "2":
                priority = Priority.MEDIUM;
                break;
            case "3":
                priority = Priority.HIGH;
                break;
            default:
                System.out.println("Invalid priority. Defaulting to MEDIUM.");
                priority = Priority.MEDIUM;
        }
        
        Bug bug = bugService.createBug(title, description, currentProject.getId(),
                                      userService.getCurrentUser().getId(), priority);
        System.out.println("Bug #" + bug.getId() + " reported successfully!");
    }

    /**
     * Handles bug selection by ID
     */
    private void selectBug() {
        System.out.print("\nEnter bug ID: ");
        String bugId = scanner.nextLine().trim();
        
        Bug bug = bugService.getBugById(bugId);
        if (bug == null || !bug.getProjectId().equals(currentProject.getId())) {
            System.out.println("Bug not found in this project.");
            return;
        }
        
        currentBug = bug;
        showBugMenu();
    }

    /**
     * Shows the bug menu with role-based options
     */
    private void showBugMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("BUG: #" + currentBug.getId() + " - " + currentBug.getTitle());
            System.out.println("=".repeat(50));
            System.out.println("1. View Full Details");
            System.out.println("2. Add a Comment");
            
            if (userService.hasRole(Role.PROJECT_MANAGER)) {
                System.out.println("3. Assign Bug (Managers Only)");
            }
            
            if (userService.hasRole(Role.DEVELOPER)) {
                System.out.println("4. Update Status (Developers Only)");
            }
            
            if (userService.hasRole(Role.TESTER)) {
                System.out.println("5. Close Bug (Testers Only)");
            }
            
            System.out.println("6. Back to Project Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    viewBugDetails();
                    break;
                case "2":
                    addComment();
                    break;
                case "3":
                    if (userService.hasRole(Role.PROJECT_MANAGER)) {
                        assignBug();
                    } else {
                        System.out.println("Access denied. Only Project Managers can assign bugs.");
                    }
                    break;
                case "4":
                    if (userService.hasRole(Role.DEVELOPER)) {
                        updateBugStatus();
                    } else {
                        System.out.println("Access denied. Only Developers can update bug status.");
                    }
                    break;
                case "5":
                    if (userService.hasRole(Role.TESTER)) {
                        closeBug();
                    } else {
                        System.out.println("Access denied. Only Testers can close bugs.");
                    }
                    break;
                case "6":
                    currentBug = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Displays full details of the current bug
     */
    private void viewBugDetails() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("BUG DETAILS");
        System.out.println("=".repeat(60));
        
        System.out.println("ID: " + currentBug.getId());
        System.out.println("Title: " + currentBug.getTitle());
        System.out.println("Description: " + currentBug.getDescription());
        System.out.println("Status: " + currentBug.getStatus());
        System.out.println("Priority: " + currentBug.getPriority());
        
        User reporter = userService.getUserById(currentBug.getReporterId());
        System.out.println("Reported by: " + (reporter != null ? reporter.getUsername() : "Unknown"));
        
        if (currentBug.getAssigneeId() != null) {
            User assignee = userService.getUserById(currentBug.getAssigneeId());
            System.out.println("Assigned to: " + (assignee != null ? assignee.getUsername() : "Unknown"));
        } else {
            System.out.println("Assigned to: Unassigned");
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("Created: " + currentBug.getCreatedDate().format(formatter));
        System.out.println("Updated: " + currentBug.getUpdatedDate().format(formatter));
        
        // Show comments
        List<Comment> comments = currentBug.getComments();
        if (!comments.isEmpty()) {
            System.out.println("\nComments:");
            System.out.println("-".repeat(60));
            for (Comment comment : comments) {
                User author = userService.getUserById(comment.getAuthorId());
                String authorName = author != null ? author.getUsername() : "Unknown";
                System.out.println("[" + comment.getTimestamp().format(formatter) + "] " +
                                 authorName + ": " + comment.getText());
            }
        }
    }

    /**
     * Handles adding a comment to the current bug
     */
    private void addComment() {
        System.out.print("\nEnter your comment: ");
        String commentText = scanner.nextLine().trim();
        
        if (commentText.isEmpty()) {
            System.out.println("Comment cannot be empty.");
            return;
        }
        
        if (bugService.addComment(currentBug.getId(), userService.getCurrentUser().getId(), commentText)) {
            // Refresh the current bug to show the new comment
            currentBug = bugService.getBugById(currentBug.getId());
            System.out.println("Comment added successfully!");
        } else {
            System.out.println("Failed to add comment.");
        }
    }

    /**
     * Handles bug assignment (Project Manager only)
     */
    private void assignBug() {
        if (!bugService.canAssignBug(currentBug.getId())) {
            System.out.println("This bug cannot be assigned (status: " + currentBug.getStatus() + ")");
            return;
        }
        
        List<User> developers = userService.getUsersByRole(Role.DEVELOPER);
        if (developers.isEmpty()) {
            System.out.println("No developers available for assignment.");
            return;
        }
        
        System.out.println("\nAvailable Developers:");
        for (int i = 0; i < developers.size(); i++) {
            System.out.println((i + 1) + ". " + developers.get(i).getUsername());
        }
        
        System.out.print("Select developer (number): ");
        String choice = scanner.nextLine().trim();
        
        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < developers.size()) {
                User developer = developers.get(index);
                if (bugService.assignBug(currentBug.getId(), developer.getId())) {
                    currentBug = bugService.getBugById(currentBug.getId());
                    System.out.println("Bug assigned to " + developer.getUsername() + " successfully!");
                } else {
                    System.out.println("Failed to assign bug.");
                }
            } else {
                System.out.println("Invalid developer selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Handles bug status update (Developer only)
     */
    private void updateBugStatus() {
        String currentUserId = userService.getCurrentUser().getId();
        
        if (!bugService.canDeveloperUpdateStatus(currentBug.getId(), currentUserId)) {
            System.out.println("You cannot update this bug's status. " +
                             "Bug must be assigned to you and in NEW or IN_PROGRESS state.");
            return;
        }
        
        Status[] validStatuses = bugService.getValidDeveloperStatusTransitions(currentBug.getStatus());
        
        if (validStatuses.length == 0) {
            System.out.println("No valid status transitions available.");
            return;
        }
        
        System.out.println("\nAvailable status updates:");
        for (int i = 0; i < validStatuses.length; i++) {
            System.out.println((i + 1) + ". " + validStatuses[i]);
        }
        
        System.out.print("Select new status (number): ");
        String choice = scanner.nextLine().trim();
        
        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < validStatuses.length) {
                Status newStatus = validStatuses[index];
                if (bugService.updateBugStatus(currentBug.getId(), newStatus)) {
                    currentBug = bugService.getBugById(currentBug.getId());
                    System.out.println("Bug status updated to " + newStatus + " successfully!");
                } else {
                    System.out.println("Failed to update bug status.");
                }
            } else {
                System.out.println("Invalid status selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * Handles bug closure (Tester only)
     */
    private void closeBug() {
        if (!bugService.canCloseBug(currentBug.getId())) {
            System.out.println("This bug cannot be closed. It must be in RESOLVED status first.");
            return;
        }
        
        System.out.print("Are you sure you want to close this bug? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            if (bugService.updateBugStatus(currentBug.getId(), Status.CLOSED)) {
                currentBug = bugService.getBugById(currentBug.getId());
                System.out.println("Bug closed successfully!");
            } else {
                System.out.println("Failed to close bug.");
            }
        } else {
            System.out.println("Bug closure cancelled.");
        }
    }

    /**
     * Utility method to truncate strings for display
     */
    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}