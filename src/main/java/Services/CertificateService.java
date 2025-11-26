package Services;


import models.Certificate;
import models.Course;
import models.Student;
import models.Lesson;
import java.time.LocalDate;
import java.util.*;

public class CertificateService {
    private CourseManager courseManager;
    private UserManager userManager;
    
    public CertificateService() {
        this.courseManager = new CourseManager();
        this.userManager = new UserManager();
    }
    public boolean hasCourseCompleted(String studentId, String courseId) {
        try {
            int cId = Integer.parseInt(courseId);
            Course course = courseManager.getCourseById(cId);
            
            if (course == null || course.getLessons() == null) {
                return false;
            }
            
            Student student = (Student) userManager.getUserById(studentId);
            if (student == null) {
                return false;
            }
            Map<String, Object> progress = student.getProgress();
            List<Lesson> lessons = course.getLessons();
            
            for (Lesson lesson : lessons) {
                String lessonKey = String.valueOf(lesson.getLessonId());
                Object completed = progress.get(lessonKey);
                if (completed == null || !completed.equals(true)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error checking course completion: " + e.getMessage());
            return false;
        }
    }
    public Certificate generateCertificate(String studentId, String courseId) {
        if (!hasCourseCompleted(studentId, courseId)) {
            System.out.println("Student has not completed all course requirements");
            return null;
        }
        try {
            int cId = Integer.parseInt(courseId);
            Course course = courseManager.getCourseById(cId);
            Student student = (Student) userManager.getUserById(studentId);
            
            if (course == null || student == null) {
                return null;
            }
            String certificateId = UUID.randomUUID().toString();
            LocalDate issueDate = LocalDate.now();
            Certificate cert = new Certificate(certificateId,studentId,courseId,issueDate,student.getUsername(),course.getTitle());
            student.getProgress().put("certificate_" + courseId, certificateId);
            userManager.updateUser(student);
            System.out.println("Certificate generated: " + certificateId);
            return cert;
        } catch (Exception e) {
            System.err.println("Error generating certificate: " + e.getMessage());
            return null;
        }
    }
    public List<Certificate> getStudentCertificates(String studentId) {
        List<Certificate> certificates = new ArrayList<>();
        try {
            Student student = (Student) userManager.getUserById(studentId);
            if (student == null) return certificates;
            Map<String, Object> progress = student.getProgress();
            List<String> enrolledCourses = student.getEnrolledCourses();
            for (String courseIdStr : enrolledCourses) {
                String certKey = "certificate_" + courseIdStr;
                if (progress.containsKey(certKey)) {
                    String certificateId = (String) progress.get(certKey);
                    
                    int cId = Integer.parseInt(courseIdStr);
                    Course course = courseManager.getCourseById(cId);
                    
                    if (course != null) {
                        Certificate cert = new Certificate(certificateId,studentId,courseIdStr,LocalDate.now(),student.getUsername(),course.getTitle());
                        certificates.add(cert);}}}
        } catch (Exception e) {
            System.err.println("Error retrieving certificates: " + e.getMessage());
        }
        return certificates;
    }
    public boolean hasCertificate(String studentId, String courseId) {
        try {
            Student student = (Student) userManager.getUserById(studentId);
            if (student == null) return false;
            return student.getProgress().containsKey("certificate_" + courseId);
        } catch (Exception e) {
            return false;
        }
    }
}