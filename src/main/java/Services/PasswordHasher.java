/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author Engyz
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        String hashedInput = hashPassword(rawPassword);
        return hashedInput.equals(hashedPassword);
    }

    public static void main(String[] args) {
        String password = "mySecurePassword123";
        String hashed = hashPassword(password);
        
        System.out.println("Original: " + password);
        System.out.println("Hashed: " + hashed);
        System.out.println("Verification: " + verifyPassword(password, hashed));
        System.out.println("Wrong password: " + verifyPassword("wrongPassword", hashed));
    }
}

