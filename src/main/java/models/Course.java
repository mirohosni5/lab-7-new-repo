package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private List<Lesson> lessons;
    private List<String> enrolledStudents;
    private ApprovalStatus approvalStatus;
    private String createdDate;
    private String reviewedBy;
    private String reviewDate;
    private String rejectionReason;

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }

    public Course(String title, String description, String instructorId) {
        this.courseId = generateCourseId();
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.approvalStatus = ApprovalStatus.PENDING;
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public Course(String courseId, String title, String description, String instructorId,
                  List<Lesson> lessons, List<String> enrolledStudents, String approvalStatus,
                  String createdDate, String reviewedBy, String reviewDate, String rejectionReason) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = lessons != null ? lessons : new ArrayList<>();
        this.enrolledStudents = enrolledStudents != null ? enrolledStudents : new ArrayList<>();
        this.approvalStatus = approvalStatus != null ?ApprovalStatus.valueOf(approvalStatus) : ApprovalStatus.PENDING;
        this.createdDate = createdDate;
        this.reviewedBy = reviewedBy;
        this.reviewDate = reviewDate;
        this.rejectionReason = rejectionReason;
    }

    private String generateCourseId() {
        return "COURSE_" + System.currentTimeMillis();
    }
    public void addLesson(Lesson lesson) {
        if (lesson != null && !lessons.contains(lesson)) {
            lessons.add(lesson);
        }
    }
    public boolean removeLesson(String lessonId) {
   return lessons.removeIf(lesson ->
        String.valueOf(lesson.getLessonId()).equals(String.valueOf(lessonId)));
    }

    public boolean enrollStudent(String studentId) {
        if (approvalStatus != ApprovalStatus.APPROVED) {
            System.out.println("Cannot enroll: Course is not approved");
            return false;
        }
        if (!enrolledStudents.contains(studentId)) {
            enrolledStudents.add(studentId);
            return true;
        }
        return false;
    }


    public boolean unenrollStudent(String studentId) {
        return enrolledStudents.remove(studentId);
    }

    public void approveCourse(String adminId) {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.reviewedBy = adminId;
        this.reviewDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.rejectionReason = null;
    }

    public void rejectCourse(String adminId, String reason) {
        this.approvalStatus = ApprovalStatus.REJECTED;
        this.reviewedBy = adminId;
        this.reviewDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.rejectionReason = reason;
    }

    public boolean isApproved() {
        return approvalStatus == ApprovalStatus.APPROVED;
    }

    public Lesson getLessonById(String lessonId) {
        for (Lesson lesson : lessons) {
           if (String.valueOf(lesson.getLessonId()).equals(lessonId)) {
    return lesson;
}

        }
        return null;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<String> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<String> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", title='" + title + '\'' +
                ", instructorId='" + instructorId + '\'' +
                ", lessons=" + lessons.size() +
                ", students=" + enrolledStudents.size() +
                ", status=" + approvalStatus +
                '}';
    }
}