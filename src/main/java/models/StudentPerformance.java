package models;

import java.util.HashMap;
import java.util.Map;

public class StudentPerformance {
    private String studentId;
    private String courseId;
    private Map<String, LessonProgress> lessonProgress;
    private double overallProgress;
    private double averageQuizScore;
    private int totalQuizzesTaken;
    private int totalQuizzesPassed;
    private int completedLessons;
    private int totalLessons;

    
    public StudentPerformance(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.lessonProgress = new HashMap<>();
        this.overallProgress = 0.0;
        this.averageQuizScore = 0.0;
        this.totalQuizzesTaken = 0;
        this.totalQuizzesPassed = 0;
        this.completedLessons = 0;
        this.totalLessons = 0;
    }

    public void addLessonProgress(String lessonId, LessonProgress progress) {
        lessonProgress.put(lessonId, progress);
        recalculateMetrics();
    }

    private void recalculateMetrics() {
        if (lessonProgress.isEmpty()) return;

        int totalScore = 0;
        int quizCount = 0;
        int completed = 0;

        for (LessonProgress progress : lessonProgress.values()) {
            if (progress.isCompleted()) {
                completed++;
            }
            if (progress.getQuizScore() > 0) {
                totalScore += progress.getQuizScore();
                quizCount++;
                if (progress.isQuizPassed()) {
                    totalQuizzesPassed++;
                }
            }
        }

        this.completedLessons = completed;
        this.totalLessons = lessonProgress.size();
        this.totalQuizzesTaken = quizCount;
        this.averageQuizScore = quizCount > 0 ? (double) totalScore / quizCount : 0.0;
        this.overallProgress = totalLessons > 0 ? 
            (double) completedLessons / totalLessons * 100 : 0.0;
    }

    public boolean isCourseCompleted() {
        return totalLessons > 0 && completedLessons == totalLessons;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public Map<String, LessonProgress> getLessonProgress() {
        return lessonProgress;
    }

    public double getOverallProgress() {
        return overallProgress;
    }

    public double getAverageQuizScore() {
        return averageQuizScore;
    }

    public int getTotalQuizzesTaken() {
        return totalQuizzesTaken;
    }

    public int getTotalQuizzesPassed() {
        return totalQuizzesPassed;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    @Override
    public String toString() {
        return "StudentPerformance{" +
                "studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", progress=" + String.format("%.2f", overallProgress) + "%" +
                ", avgQuizScore=" + String.format("%.2f", averageQuizScore) +
                ", completed=" + completedLessons + "/" + totalLessons +
                '}';
    }

    public static class LessonProgress {
        private String lessonId;
        private boolean completed;
        private int quizScore;
        private boolean quizPassed;
        private int quizAttempts;

        public LessonProgress(String lessonId) {
            this.lessonId = lessonId;
            this.completed = false;
            this.quizScore = 0;
            this.quizPassed = false;
            this.quizAttempts = 0;
        }

        public void markCompleted() {
            this.completed = true;
        }

        public void recordQuizAttempt(int score, boolean passed) {
            this.quizScore = Math.max(this.quizScore, score); //to give me the best score
            this.quizPassed = this.quizPassed || passed;
            this.quizAttempts++;
        }

        public String getLessonId() {
            return lessonId;
        }

        public boolean isCompleted() {
            return completed;
        }

        public int getQuizScore() {
            return quizScore;
        }

        public boolean isQuizPassed() {
            return quizPassed;
        }

        public int getQuizAttempts() {
            return quizAttempts;
        }
    }
}