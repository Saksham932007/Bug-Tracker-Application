# Bug Tracker Application

A complete command-line bug tracking application built with Java 11+ and Maven. This application provides comprehensive bug management functionality with role-based access control.

## Features

### User Management
- **Three User Roles**: PROJECT_MANAGER, DEVELOPER, and TESTER
- **Simple Login System**: Username-based authentication with pre-populated demo users
- **Role-based Permissions**: Different features available based on user role

### Project Management
- **Create Projects**: Project Managers can create new projects with name and description
- **List Projects**: All users can view available projects
- **Project Selection**: Navigate into projects to manage bugs

### Bug Tracking
- **Report Bugs**: Testers can report new bugs with title, description, and priority
- **Assign Bugs**: Project Managers can assign bugs to developers
- **Update Status**: Developers can update status of their assigned bugs
- **Close Bugs**: Testers can close resolved bugs after verification
- **View Bugs**: All users can view bugs with filtering by project
- **Bug Details**: View complete bug information including comments and history
- **Comments**: All users can add timestamped comments to bugs

### Data Persistence
- **JSON Storage**: All data stored in `bugs.json` file
- **Auto-save**: Changes automatically saved to file
- **Default Data**: Application comes with sample data for immediate testing

## Prerequisites

- Java 11 or newer
- Maven 3.6 or newer

## Installation and Setup

1. **Clone or extract the project files**
2. **Navigate to the project directory**:
   ```bash
   cd bug-tracker
   ```

3. **Compile the project**:
   ```bash
   mvn clean compile
   ```

4. **Run the application**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.bugtracker.Main"
   ```

Alternatively, you can compile and run in one step:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.example.bugtracker.Main"
```

## Demo Users

The application comes with pre-configured demo users for testing:

| Username | Role | Permissions |
|----------|------|-------------|
| manager1 | PROJECT_MANAGER | Create projects, assign bugs, view all |
| dev1     | DEVELOPER | Update assigned bug status, view all, comment |
| dev2     | DEVELOPER | Update assigned bug status, view all, comment |
| tester1  | TESTER | Report bugs, close resolved bugs, view all, comment |
| tester2  | TESTER | Report bugs, close resolved bugs, view all, comment |

## Usage Guide

### Starting the Application
1. Run the application using Maven
2. Enter a username from the demo users list
3. Navigate through the menu-driven interface

### Main Menu Options
- **Select Project**: Browse and select from available projects
- **Create New Project**: (Project Managers only) Create a new project
- **Exit**: Close the application

### Project Menu Options
- **View All Bugs**: Display all bugs in the current project
- **Report a New Bug**: (Testers only) Create a new bug report
- **Select a Bug by ID**: Navigate to a specific bug
- **Back to Main Menu**: Return to main menu

### Bug Menu Options
- **View Full Details**: See complete bug information and comments
- **Add a Comment**: Add a timestamped comment to the bug
- **Assign Bug**: (Project Managers only) Assign bug to a developer
- **Update Status**: (Developers only) Change bug status if assigned to you
- **Close Bug**: (Testers only) Close a resolved bug
- **Back to Project Menu**: Return to project menu

### Bug Status Workflow
1. **NEW**: Initial state when bug is reported
2. **IN_PROGRESS**: Developer has started working on the bug
3. **RESOLVED**: Developer has fixed the bug
4. **CLOSED**: Tester has verified the fix

### Priority Levels
- **LOW**: Minor issues that don't affect core functionality
- **MEDIUM**: Issues that affect functionality but have workarounds
- **HIGH**: Critical issues that significantly impact functionality

## Project Structure

```
bug-tracker/
├── pom.xml                     # Maven configuration
└── src/main/java/com/example/bugtracker/
    ├── Main.java               # Application entry point
    ├── model/                  # Data models
    │   ├── Bug.java
    │   ├── Comment.java
    │   ├── Project.java
    │   ├── User.java
    │   └── enums/
    │       ├── Priority.java
    │       ├── Role.java
    │       └── Status.java
    ├── repository/             # Data access layer
    │   └── DataRepository.java
    ├── service/                # Business logic layer
    │   ├── BugService.java
    │   ├── ProjectService.java
    │   └── UserService.java
    └── ui/                     # User interface layer
        └── ConsoleUI.java
```

## Data Storage

- All application data is stored in `bugs.json` in the project root directory
- The file is created automatically on first run with sample data
- Data is automatically saved after each operation
- The JSON file can be manually edited if needed (application must be restarted)

## Architecture

The application follows a layered architecture pattern:

1. **Model Layer**: Plain Java objects representing data entities
2. **Repository Layer**: Handles data persistence and retrieval from JSON file
3. **Service Layer**: Contains business logic and validation rules
4. **UI Layer**: Manages user interaction and console input/output
5. **Main Class**: Application bootstrap and component initialization

## Error Handling

- Invalid menu selections are handled gracefully
- Role-based access violations show appropriate error messages
- File I/O errors are caught and reported
- Input validation prevents empty or invalid data entry

## Future Enhancements

Potential areas for expansion:
- User registration and password authentication
- Email notifications for bug assignments
- Bug filtering and search functionality
- Bulk operations (assign multiple bugs, etc.)
- Export functionality (CSV, PDF reports)
- Web-based interface
- Database integration instead of JSON files

## Troubleshooting

### Common Issues

1. **Java Version**: Ensure Java 11 or newer is installed
2. **Maven Not Found**: Install Maven or use Maven wrapper
3. **Permission Errors**: Ensure write permissions in project directory
4. **Compilation Errors**: Run `mvn clean compile` to rebuild

### Getting Help

- Check that all required dependencies are installed
- Verify Java and Maven versions meet requirements
- Ensure proper file permissions for JSON data file creation

## License

This project is provided as-is for educational and demonstration purposes.