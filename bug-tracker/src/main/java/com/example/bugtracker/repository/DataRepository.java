package com.example.bugtracker.repository;

import com.example.bugtracker.model.*;
import com.example.bugtracker.model.enums.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Repository class responsible for data persistence and retrieval.
 * Handles loading and saving all application data to/from a JSON file.
 */
public class DataRepository {
    private static final String DATA_FILE = "bugs.json";
    private final Gson gson;
    
    // In-memory data storage
    private List<User> users;
    private List<Project> projects;
    private List<Bug> bugs;

    /**
     * Initializes the repository and loads data from file
     */
    public DataRepository() {
        // Configure Gson with custom serializers for LocalDateTime
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .setPrettyPrinting()
                .create();
        
        loadData();
    }

    /**
     * Custom serializer for LocalDateTime to handle JSON conversion
     */
    private static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    /**
     * Custom deserializer for LocalDateTime to handle JSON conversion
     */
    private static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    /**
     * Loads data from the JSON file, creates default data if file doesn't exist
     */
    private void loadData() {
        File file = new File(DATA_FILE);
        
        if (!file.exists()) {
            createDefaultData();
            saveData();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Type dataType = new TypeToken<DataContainer>(){}.getType();
            DataContainer data = gson.fromJson(reader, dataType);
            
            if (data != null) {
                this.users = data.users != null ? data.users : new ArrayList<>();
                this.projects = data.projects != null ? data.projects : new ArrayList<>();
                this.bugs = data.bugs != null ? data.bugs : new ArrayList<>();
            } else {
                createDefaultData();
            }
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
            createDefaultData();
        }
    }

    /**
     * Creates default mock data for demonstration purposes
     */
    private void createDefaultData() {
        // Create default users
        users = new ArrayList<>();
        users.add(new User("1", "manager1", Role.PROJECT_MANAGER));
        users.add(new User("2", "dev1", Role.DEVELOPER));
        users.add(new User("3", "dev2", Role.DEVELOPER));
        users.add(new User("4", "tester1", Role.TESTER));
        users.add(new User("5", "tester2", Role.TESTER));

        // Create default projects
        projects = new ArrayList<>();
        projects.add(new Project("1", "E-commerce Website", "Main company e-commerce platform"));
        projects.add(new Project("2", "Mobile App", "iOS and Android mobile application"));
        projects.add(new Project("3", "API Service", "Backend API for all company services"));

        // Create default bugs
        bugs = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        Bug bug1 = new Bug("1", "Login page not loading", 
                "The login page fails to load when accessed from mobile devices", 
                "1", "4", Priority.HIGH);
        bug1.setCreatedDate(now.minusDays(2));
        bug1.setUpdatedDate(now.minusDays(1));
        bug1.addComment(new Comment("4", "Reproduced on iPhone 12 with Safari", now.minusDays(1)));
        
        Bug bug2 = new Bug("2", "Checkout process slow", 
                "The checkout process takes too long to complete, causing user frustration", 
                "1", "5", Priority.MEDIUM);
        bug2.setCreatedDate(now.minusDays(3));
        bug2.setUpdatedDate(now.minusDays(2));
        bug2.assignTo("2");
        bug2.updateStatus(Status.IN_PROGRESS);
        
        Bug bug3 = new Bug("3", "API timeout errors", 
                "API calls are timing out frequently during peak hours", 
                "3", "4", Priority.HIGH);
        bug3.setCreatedDate(now.minusDays(1));
        bug3.setUpdatedDate(now.minusDays(1));
        
        bugs.add(bug1);
        bugs.add(bug2);
        bugs.add(bug3);
    }

    /**
     * Saves current data to the JSON file
     */
    public void saveData() {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            DataContainer data = new DataContainer();
            data.users = this.users;
            data.projects = this.projects;
            data.bugs = this.bugs;
            
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    // User operations
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public User getUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
        saveData();
    }

    // Project operations
    public List<Project> getAllProjects() {
        return new ArrayList<>(projects);
    }

    public Project getProjectById(String id) {
        return projects.stream()
                .filter(project -> project.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addProject(Project project) {
        projects.add(project);
        saveData();
    }

    // Bug operations
    public List<Bug> getAllBugs() {
        return new ArrayList<>(bugs);
    }

    public List<Bug> getBugsByProject(String projectId) {
        return bugs.stream()
                .filter(bug -> bug.getProjectId().equals(projectId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public Bug getBugById(String id) {
        return bugs.stream()
                .filter(bug -> bug.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addBug(Bug bug) {
        bugs.add(bug);
        saveData();
    }

    public void updateBug(Bug bug) {
        for (int i = 0; i < bugs.size(); i++) {
            if (bugs.get(i).getId().equals(bug.getId())) {
                bugs.set(i, bug);
                saveData();
                return;
            }
        }
    }

    /**
     * Generates the next available ID for users
     */
    public String getNextUserId() {
        return String.valueOf(users.size() + 1);
    }

    /**
     * Generates the next available ID for projects
     */
    public String getNextProjectId() {
        return String.valueOf(projects.size() + 1);
    }

    /**
     * Generates the next available ID for bugs
     */
    public String getNextBugId() {
        return String.valueOf(bugs.size() + 1);
    }

    /**
     * Container class for JSON serialization
     */
    private static class DataContainer {
        List<User> users;
        List<Project> projects;
        List<Bug> bugs;
    }
}