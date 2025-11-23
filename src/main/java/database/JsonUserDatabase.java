/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author Engyz
 */
import models.User;
import models.Student;
import models.Instructor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUserDatabase {
    private static final String USERS_FILE = "users.json";

    public List<User> readUsersFromFile() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);

        if (!file.exists()) {
            try {
                file.createNewFile();
                writeUsersToFile(users); 
            } catch (IOException e) {
                System.err.println("Error creating users.json: " + e.getMessage());
            }
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            String jsonContent = content.toString().trim();
            if (jsonContent.isEmpty()) {
                return users;
            }

            JSONArray jsonArray = new JSONArray(jsonContent);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUser = jsonArray.getJSONObject(i);
                User user = parseUserFromJson(jsonUser);
                if (user != null) {
                    users.add(user);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading users.json: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

        return users;
    }

    public void writeUsersToFile(List<User> users) {
        JSONArray jsonArray = new JSONArray();

        for (User user : users) {
            JSONObject jsonUser = convertUserToJson(user);
            jsonArray.put(jsonUser);
        }

        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            writer.write(jsonArray.toString(4));
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing to users.json: " + e.getMessage());
        }
    }

    public JSONObject findUserByEmail(String email) {
        List<User> users = readUsersFromFile();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return convertUserToJson(user);
            }
        }
        return null;
    }

    public boolean addUserToFile(User user) {
        List<User> users = readUsersFromFile();
        
        for (User existingUser : users) {
            if (existingUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                return false;
            }
        }
        users.add(user);
        writeUsersToFile(users);
        return true;
    }
    public boolean updateUser(String userId, User updatedUser) {
        List<User> users = readUsersFromFile();
        
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(userId)) {
                users.set(i, updatedUser);
                writeUsersToFile(users);
                return true;
            }
        }
        return false;
    }

    private JSONObject convertUserToJson(User user) {
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("userId", user.getUserId());
        jsonUser.put("role", user.getRole());
        jsonUser.put("username", user.getUsername());
        jsonUser.put("email", user.getEmail());
        jsonUser.put("passwordHash", user.getPasswordHash());

        if (user instanceof Student) {
            Student student = (Student) user;
            jsonUser.put("enrolledCourses", new JSONArray(student.getEnrolledCourses()));
            jsonUser.put("progress", new JSONObject(student.getProgress()));
        } else if (user instanceof Instructor) {
            Instructor instructor = (Instructor) user;
            jsonUser.put("createdCourses", new JSONArray(instructor.getCreatedCourses()));
        }

        return jsonUser;
    }

    private User parseUserFromJson(JSONObject jsonUser) {
        try {
            String userId = jsonUser.getString("userId");
            String role = jsonUser.getString("role");
            String username = jsonUser.getString("username");
            String email = jsonUser.getString("email");
            String passwordHash = jsonUser.getString("passwordHash");

            if (role.equalsIgnoreCase("Student")) {
                List<String> enrolledCourses = new ArrayList<>();
                if (jsonUser.has("enrolledCourses")) {
                    JSONArray coursesArray = jsonUser.getJSONArray("enrolledCourses");
                    for (int i = 0; i < coursesArray.length(); i++) {
                        enrolledCourses.add(coursesArray.getString(i));
                    }
                }

                Map<String, Object> progress = new HashMap<>();
                if (jsonUser.has("progress")) {
                    JSONObject progressObj = jsonUser.getJSONObject("progress");
                    for (String key : progressObj.keySet()) {
                        progress.put(key, progressObj.get(key));
                    }
                }

                return new Student(userId, username, email, passwordHash, enrolledCourses, progress);

            } else if (role.equalsIgnoreCase("Instructor")) {
                List<String> createdCourses = new ArrayList<>();
                if (jsonUser.has("createdCourses")) {
                    JSONArray coursesArray = jsonUser.getJSONArray("createdCourses");
                    for (int i = 0; i < coursesArray.length(); i++) {
                        createdCourses.add(coursesArray.getString(i));
                    }
                }

                return new Instructor(userId, username, email, passwordHash, createdCourses);
            }

        } catch (Exception e) {
            System.err.println("Error parsing user: " + e.getMessage());
        }

        return null;
    }
}