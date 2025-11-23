/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Engyz
 */
import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {
    private List<String> createdCourses;

    public Instructor(String username, String email, String passwordHash) {
        super(username, email, passwordHash, "Instructor");
        this.createdCourses = new ArrayList<>();
    }

    public Instructor(String userId, String username, String email, String passwordHash, 
                      List<String> createdCourses) {
        super(userId, username, email, passwordHash, "Instructor");
        this.createdCourses = createdCourses != null ? createdCourses : new ArrayList<>();
    }

    @Override
    public String getDetails() {
        return "Instructor: " + username + " (" + email + ") - Created " + 
               createdCourses.size() + " courses";
    }

    public void addCreatedCourse(String courseId) {
        if (!createdCourses.contains(courseId)) {
            createdCourses.add(courseId);
        }
    }


    public void removeCourse(String courseId) {
        createdCourses.remove(courseId);
    }

    
    public List<String> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(List<String> createdCourses) {
        this.createdCourses = createdCourses;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdCourses=" + createdCourses.size() +
                '}';
    }
}