/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author Engyz
 */
import models.User;
import models.Student;
import models.Instructor;
import database.JsonUserDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;
    private JsonUserDatabase database;

    public UserManager() {
        this.database = new JsonUserDatabase();
        this.users = new ArrayList<>();
        loadUsersFromJson();
    }

    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
 
        if (getUserByEmail(user.getEmail()) != null) {
            System.out.println("Error: Email already exists!");
            return false;
        }
        
        users.add(user);
        saveUsersToJson();
        return true;
    }

    public User getUserByEmail(String email) {
        if (email == null) {
            return null;
        }
        
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                return user;
            }
        }
        return null;
    }

    public User getUserById(String userId) {
        if (userId == null) {
            return null;
        }
        
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public boolean updateUser(User updatedUser) {
        if (updatedUser == null) {
            return false;
        }
        
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                saveUsersToJson();
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String userId) {
        User user = getUserById(userId);
        if (user != null) {
            users.remove(user);
            saveUsersToJson();
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Student) {
                students.add((Student) user);
            }
        }
        return students;
    }

    public List<Instructor> getAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Instructor) {
                instructors.add((Instructor) user);
            }
        }
        return instructors;
    }

    public void saveUsersToJson() {
        database.writeUsersToFile(users);
    }

    public void loadUsersFromJson() {
        users = database.readUsersFromFile();
    }

    public int getUserCount() {
        return users.size();
    }
}