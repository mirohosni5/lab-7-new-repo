package Services;

import models.QuizAttempt;

import java.util.*;

public class AnalyticsService {
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
        if (studentsEnrolled == 0)
            return 0.0;

        return (100.0 * studentsCompletedCourse) / studentsEnrolled;
    }
}