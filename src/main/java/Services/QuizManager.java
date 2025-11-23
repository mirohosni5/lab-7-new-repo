package Services;

import models.*;

public class QuizManager {
    private final JsonDatabaseManager  courseManager;
    private final AnalyticsService analytics;

    public QuizManager(JsonDatabaseManager  courseManager, AnalyticsService analytics) {
        this.courseManager = courseManager;
        this.analytics = analytics;
    }

    public synchronized QuizAttempt submitAttempt(QuizAttempt attempt) {

        Course course = courseManager.findCourseById(attempt.getCourseId());
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

        Quiz quiz = lesson.getQuiz();

        int score = 0;
        int totalPoints = quiz.totalPoints();

        for (Question q : quiz.getQuestions()) {
            Integer selected = attempt.getAnswers().get(Integer.parseInt(q.getId())).getSelectedIndex();
            if (selected != null && selected == q.getCorrectIndex()) {
                score += q.getPoints();
            }
        }

        attempt.setScore(score);

        double percent = (totalPoints == 0) ? 0 : (score * 100.0 / totalPoints);
        attempt.setPassed(percent >= quiz.getPassPercent());

        courseManager.addQuizAttemptForUser(attempt);
        courseManager.addQuizAttemptForCourse(attempt);

    }
}
