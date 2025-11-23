package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private List<String> students;
    private List<Lesson> lessons;

    // Admin workflow fields
    private String status; // "PENDING", "APPROVED", "REJECTED"
    private String reviewedBy;
    private String reviewDate;
    private String rejectionReason;

    // Constructor القديم
    public Course(int courseId, String title, String description, int instructorId){
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.status = "PENDING"; // افتراضي جديد
    }

    // Getters & Setters القديمة
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    public List<String> getStudents() { return students; }
    public void setStudents(List<String> students) { this.students = students; }

    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }

    // Admin workflow methods
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }

    public String getReviewDate() { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public void approveCourse(String adminId) {
        this.status = "APPROVED";
        this.reviewedBy = adminId;
        this.reviewDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.rejectionReason = null;
    }

    public void rejectCourse(String adminId, String reason) {
        this.status = "REJECTED";
        this.reviewedBy = adminId;
        this.reviewDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.rejectionReason = reason;
    }

    public boolean enrollStudent(String studentId) {
        if (!"APPROVED".equals(this.status)) {
            System.out.println("Cannot enroll: Course is not approved");
            return false;
        }
        if (!students.contains(studentId)) {
            students.add(studentId);
            return true;
        }
        return false;
    }

    public boolean unenrollStudent(String studentId) {
        return students.remove(studentId);
    }

    @Override
    public String toString() {
        return title + " (ID: " + courseId + ", Status: " + status + ")";
    }
    
    
    
}


