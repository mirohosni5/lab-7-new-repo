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
import utils.ValidationUtils;

public class AuthService {
    private UserManager userManager;
    private User currentUser;

    public AuthService() {
        this.userManager = new UserManager();
        this.currentUser = null;
    }

    public boolean signup(String username, String email, String password, String role) {
  
        if (!ValidationUtils.validateRequiredFields(username, email, password, role)) {
            System.out.println("Error: All fields are required!");
            return false;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            System.out.println("Error: Invalid email format!");
            return false;
        }

        if (!ValidationUtils.isValidUsername(username)) {
            System.out.println("Error: Invalid username! Use 3-20 alphanumeric characters.");
            return false;
        }

        if (!ValidationUtils.isPasswordStrong(password)) {
            System.out.println("Error: Password must be at least 6 characters!");
            return false;
        }

        if (userManager.getUserByEmail(email) != null) {
            System.out.println("Error: Email already registered!");
            return false;
        }

        String passwordHash = PasswordHasher.hashPassword(password);

        User newUser;
        if (role.equalsIgnoreCase("Student")) {
            newUser = new Student(username, email, passwordHash);
        } else if (role.equalsIgnoreCase("Instructor")) {
            newUser = new Instructor(username, email, passwordHash);
        } else {
            System.out.println("Error: Invalid role! Use 'Student' or 'Instructor'.");
            return false;
        }

        boolean success = userManager.addUser(newUser);
        if (success) {
            System.out.println(" User registered successfully!");
            System.out.println("User ID: " + newUser.getUserId());
        }
        return success;
    }
    public User login(String email, String password) {
       
        if (!ValidationUtils.validateRequiredFields(email, password)) {
            System.out.println("Error: Email and password are required!");
            return null;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            System.out.println("Error: Invalid email format!");
            return null;
        }

        User user = userManager.getUserByEmail(email);
        if (user == null) {
            System.out.println("Error: User not found!");
            return null;
        }

        if (!PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            System.out.println("Error: Incorrect password!");
            return null;
        }

        // Login successful
        currentUser = user;
        System.out.println(" Login successful!");
        System.out.println("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        return user;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println(" User " + currentUser.getUsername() + " logged out.");
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }
    public boolean validateEmail(String email) {
        return ValidationUtils.isValidEmail(email);
    }

    public boolean validateRequiredFields(String... fields) {
        return ValidationUtils.validateRequiredFields(fields);
    }

    public UserManager getUserManager() {
        return userManager;
    }
}