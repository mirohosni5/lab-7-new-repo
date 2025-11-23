/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Engyz
 */
import java.util.UUID;

public class IdGenerator {

    public static String generateUserId() {
        return "USER_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


    public static String generateId(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateSimpleId() {
        return String.valueOf(System.currentTimeMillis());
    }
}