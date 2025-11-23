package Services;

import models.*;

public class QuizManager {
    private final CourseManager courseManager;
    private final AnalyticsService analytics;

    public QuizManager(CourseManager courseManager, AnalyticsService analytics) {
        this.courseManager = courseManager;
        this.analytics = analytics;
    }

    public synchronized QuizAttempt submitAttempt(QuizAttempt attempt) {

        Course course = courseManager.getCourseById(attempt.getCourseId());
        if (course == null)
            throw new RuntimeException("Course not found");

        Lesson lesson = null;
        int targetLessonId = Integer.parseInt(attempt.getLessonId());

        for (Lesson l : course.getLessons()) {
            if (l.getLessonId() == targetLessonId) {
                lesson = l;
                break;
            }
        }

        if (lesson == null) {
            throw new RuntimeException("Lesson/Quiz not found");
        }

    }
}
