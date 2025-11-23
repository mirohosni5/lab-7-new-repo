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

public class Admin extends User {
    private List<String> reviewedCourses;
    private int totalApprovals;
    private int totalRejections;

    public Admin(String username, String email, String passwordHash) {
        super(username, email, passwordHash, "Admin");
        this.reviewedCourses = new ArrayList<>();
        this.totalApprovals = 0;
        this.totalRejections = 0;
    }

    public Admin(String userId, String username, String email, String passwordHash,
                 List<String> reviewedCourses, int totalApprovals, int totalRejections) {
        super(userId, username, email, passwordHash, "Admin");
        this.reviewedCourses = reviewedCourses != null ? reviewedCourses : new ArrayList<>();
        this.totalApprovals = totalApprovals;
        this.totalRejections = totalRejections;
    }

    @Override
    public String getDetails() {
        return "Admin: " + username + " (" + email + ") - Reviewed " + 
               reviewedCourses.size() + " courses (Approved: " + totalApprovals + 
               ", Rejected: " + totalRejections + ")";
    }


    public void addReviewedCourse(String courseId, boolean approved) {
        if (!reviewedCourses.contains(courseId)) {
            reviewedCourses.add(courseId);
            if (approved) {
                totalApprovals++;
            } else {
                totalRejections++;
            }
        }
    }


    public List<String> getReviewedCourses() {
        return reviewedCourses;
    }

    public void setReviewedCourses(List<String> reviewedCourses) {
        this.reviewedCourses = reviewedCourses;
    }

    public int getTotalApprovals() {
        return totalApprovals;
    }

    public void setTotalApprovals(int totalApprovals) {
        this.totalApprovals = totalApprovals;
    }

    public int getTotalRejections() {
        return totalRejections;
    }

    public void setTotalRejections(int totalRejections) {
        this.totalRejections = totalRejections;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", reviewedCourses=" + reviewedCourses.size() +
                ", approvals=" + totalApprovals +
                ", rejections=" + totalRejections +
                '}';
    }
}
