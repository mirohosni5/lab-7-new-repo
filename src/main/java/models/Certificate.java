package models;

import java.time.LocalDate;

public class Certificate {
    private String certificateId;
    private String studentId;
    private String courseId;
    private LocalDate issueDate;
    private String studentName;
    private String courseName;

    public Certificate(String certificateId, String studentId, String courseId,
                       LocalDate issueDate, String studentName, String courseName) {
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.issueDate = issueDate;
        this.studentName = studentName;
        this.courseName = courseName;
    }

    public Certificate(String certificateId, String studentId, String courseId, LocalDate issueDate) {
        this(certificateId, studentId, courseId, issueDate, null, null);
    }
}
