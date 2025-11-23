/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Engyz
**/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {
    private List<String> enrolledCourses;
    private Map<String, Object> progress;

    // Constructor for new student
    public Student(String username, String email, String passwordHash) {
        super(username, email, passwordHash, "Student");
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
    }

    public Student(String userId, String username, String email, String passwordHash, 
                   List<String> enrolledCourses, Map<String, Object> progress) {
        super(userId, username, email, passwordHash, "Student");
        this.enrolledCourses = enrolledCourses != null ? enrolledCourses : new ArrayList<>();
        this.progress = progress != null ? progress : new HashMap<>();
    }

    @Override
    public String getDetails() {
        return "Student: " + username + " (" + email + ") - Enrolled in " + 
               enrolledCourses.size() + " courses";
    }

    public void enrollInCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }

   
    public void updateProgress(String key, Object value) {
        progress.put(key, value);
    }

  
    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public Map<String, Object> getProgress() {
        return progress;
    }

    public void setProgress(Map<String, Object> progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Student{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enrolledCourses=" + enrolledCourses.size() +
                '}';
    }
}