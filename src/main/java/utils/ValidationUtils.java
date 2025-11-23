/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Engyz
 */
import java.util.regex.Pattern;

public class ValidationUtils {
   
    private static final String EMAIL_REGEX = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    public static boolean isPasswordStrong(String password) {
        if (password == null) {
            return false;
        }
        return password.length() >= 6;
    }


    public static boolean validateRequiredFields(String... fields) {
        for (String field : fields) {
            if (isFieldEmpty(field)) {
                return false;
            }
        }
        return true;
    }

 
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        String cleanUsername = username.trim();
        return cleanUsername.length() >= 3 && 
               cleanUsername.length() <= 20 && 
               cleanUsername.matches("^[a-zA-Z0-9_]+$");
    }
}

