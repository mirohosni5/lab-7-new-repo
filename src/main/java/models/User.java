package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import models.QuizAttempt;

public abstract class User {
    protected String userId;
    protected String role;
    protected String username;
    protected String email;
    protected String passwordHash;

    // قائمة محاولات الكويز الخاصة بالمستخدم
    protected List<QuizAttempt> quizAttempts = new ArrayList<>();

    // قائمة الدروس المكتملة، كل عنصر "courseId::lessonId"
    protected List<String> completedLessons = new ArrayList<>();

    public User(String username, String email, String passwordHash, String role) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(String userId, String username, String email, String passwordHash, String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public abstract String getDetails();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<QuizAttempt> getQuizAttempts() {
        return quizAttempts;
    }

    public void addQuizAttempt(QuizAttempt attempt) {
        if (quizAttempts == null) {
            quizAttempts = new ArrayList<>();
        }
        quizAttempts.add(attempt);
    }

    public List<String> getCompletedLessons() {
        return completedLessons;
    }

    public void markLessonCompleted(String courseId, String lessonId) {
        if (completedLessons == null) {
            completedLessons = new ArrayList<>();
        }
        String key = courseId + "::" + lessonId;
        if (!completedLessons.contains(key)) {
            completedLessons.add(key);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", quizAttempts=" + quizAttempts.size() +
                ", completedLessons=" + completedLessons.size() +
                '}';
    }
}
