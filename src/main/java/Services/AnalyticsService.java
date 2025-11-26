/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import models.Course;
import models.Lesson;
import models.QuizAttempt;
import models.Student;

/**
 *
 * @author M
 */
public class AnalyticsService {
    private CourseManager manager=new CourseManager();
    private UserManager userManager=new UserManager();
    
    private final List<QuizAttempt> allAttempts = new ArrayList<>();

    public void recordAttempt(QuizAttempt attempt, int totalPoints) {
        allAttempts.add(attempt);
    }

    public double lessonAverageScore(String courseId, String lessonId) {
        double sum = 0.0;
        int count = 0;

        for (QuizAttempt a : allAttempts) {
            if (a.getCourseId().equals(courseId) && a.getLessonId().equals(lessonId)) {
                sum += a.getScore();
                count++;
            }
        }

        return count == 0 ? 0.0 : sum / count;
    }

    public double completionPercent(int studentsEnrolled, int studentsCompletedCourse) {
        if (studentsEnrolled == 0) return 0.0;

        return (100.0 * studentsCompletedCourse) / studentsEnrolled;
    }

    public List<QuizAttempt> getAttemptsByCourseID(String courseId) {
        List<QuizAttempt> result = new ArrayList<>();
        for (QuizAttempt a : allAttempts) {
            if (a.getCourseId().equals(courseId)) {
                result.add(a);
            }
        }
        return result;
    }

    public List<QuizAttempt> getAttemptsByLessonID(String courseId, String lessonId) {
        List<QuizAttempt> result = new ArrayList<>();
        for (QuizAttempt a : allAttempts) {
            if (a.getCourseId().equals(courseId) && a.getLessonId().equals(lessonId)) {
                result.add(a);
            }
        }
        return result;
    }
       public Map<String, List<Number>> getStudentPerformanceByStudent(int courseId) {
        Map<String, List<Number>> performance = new LinkedHashMap<>();
        
        try {
            Course course = manager.getCourseById(courseId);
            if (course == null) return performance;
            List<String> studentIds = course.getStudents();
            if (studentIds == null) return performance;
            for (String studentId : studentIds) {
                Student student = (Student) userManager.getUserById(studentId);
                if (student == null) continue;
                List<Number> scores = new ArrayList<>();
                if (student.getQuizAttempts() != null) {
                    for (QuizAttempt attempt : student.getQuizAttempts()) {
                        if (attempt.getCourseId().equals(String.valueOf(courseId))) {
                            scores.add(attempt.getScore());}}}
                if (scores.isEmpty()) {
                    scores.add(0);}
                performance.put(student.getUsername(), scores);}
        } catch (Exception e) {
            System.err.println("Error getting student performance: " + e.getMessage());
        }
        return performance;
    }
        public Map<String, Double> getQuizAveragesByLesson(int courseId) {
        Map<String, Double> quizAverages = new LinkedHashMap<>();
        
        try {
            Course course = manager.getCourseById(courseId);
            if (course == null || course.getLessons() == null) return quizAverages;
            
            List<String> studentIds = course.getStudents();
            if (studentIds == null) return quizAverages;
            for (Lesson lesson : course.getLessons()) {
                String lessonId = String.valueOf(lesson.getLessonId());
                List<Integer> scores = new ArrayList<>();
                for (String studentId : studentIds) {
                    Student student = (Student) userManager.getUserById(studentId);
                    if (student != null && student.getQuizAttempts() != null) {
                        for (QuizAttempt attempt : student.getQuizAttempts()) {
                            if (attempt.getCourseId().equals(String.valueOf(courseId)) &&
                                attempt.getLessonId().equals(lessonId)) {
                                scores.add(attempt.getScore());}}}}
                if (!scores.isEmpty()) {
                    double sum = 0;
                    for (Integer score : scores) {
                        sum += score;
                    }
                    double avg = sum / scores.size();
                    quizAverages.put(lesson.getTitle(), avg);
                } else {
                    quizAverages.put(lesson.getTitle(), 0.0);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error calculating quiz averages: " + e.getMessage());
        }
        
        return quizAverages;
    }
         public Map<String, Double> getCompletionPercentByLesson(int courseId) {
        Map<String, Double> completionPercent = new LinkedHashMap<>();
        
        try {
            Course course = manager.getCourseById(courseId);
            if (course == null || course.getLessons() == null) return completionPercent;
            
            List<String> studentIds = course.getStudents();
            if (studentIds == null || studentIds.isEmpty()) return completionPercent;
            
            int totalStudents = studentIds.size();
            
            // For each lesson, count how many students completed it
            for (Lesson lesson : course.getLessons()) {
                String lessonId = String.valueOf(lesson.getLessonId());
                int completedCount = 0;
                
                for (String studentId : studentIds) {
                    Student student = (Student) userManager.getUserById(studentId);
                    if (student != null && student.getProgress() != null) {
                        Object completed = student.getProgress().get(lessonId);
                        if (completed != null && completed.equals(true)) {
                            completedCount++;}}}
                double percentage = (completedCount * 100.0) / totalStudents;
                completionPercent.put(lesson.getTitle(), percentage);
            } 
        } catch (Exception e) {
            System.err.println("Error calculating completion percentages: " + e.getMessage());
        }
        return completionPercent;
    }
          public CourseStatistics getCourseStatistics(int courseId) {
        CourseStatistics stats = new CourseStatistics();
        try {
            Course course = manager.getCourseById(courseId);
            if (course == null) return stats;
            
            List<String> studentIds = course.getStudents();
            stats.totalEnrolled = studentIds != null ? studentIds.size() : 0;
            
            if (studentIds != null && !studentIds.isEmpty()) {
                int totalScore = 0;
                int scoreCount = 0;
                int completedStudents = 0;
                
                for (String studentId : studentIds) {
                    Student student = (Student) userManager.getUserById(studentId);
                    if (student != null) {
                        CertificateService certService = new CertificateService();
                        if (certService.hasCourseCompleted(studentId, String.valueOf(courseId))) {
                            completedStudents++;
                        }
                        if (student.getQuizAttempts() != null) {
                            for (QuizAttempt attempt : student.getQuizAttempts()) {
                                if (attempt.getCourseId().equals(String.valueOf(courseId))) {
                                    totalScore += attempt.getScore();
                                    scoreCount++;}}}}}
                stats.averageScore = scoreCount > 0 ? (double) totalScore / scoreCount : 0.0;
                stats.completionRate = (completedStudents * 100.0) / stats.totalEnrolled;
            }
        } catch (Exception e) {
            System.err.println("Error calculating course statistics: " + e.getMessage());
        }
        return stats;
    }
    public static class CourseStatistics {
        public int totalEnrolled = 0;
        public double averageScore = 0.0;
        public double completionRate = 0.0;
        
        @Override
        public String toString() {
            return String.format("Enrolled: %d, Avg Score: %.2f, Completion: %.2f%%",totalEnrolled, averageScore, completionRate);
        }
    }
}

