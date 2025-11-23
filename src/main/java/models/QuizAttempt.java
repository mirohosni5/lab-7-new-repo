package models;

import java.time.Instant;
import java.util.List;

public class QuizAttempt {
    private String attemptId;
    private String studentId;
    private String courseId;
    private String lessonId;
    private String quizId;
    private List<Answer> answers;
    private int score;
    private boolean passed;
    private Instant timestamp;
    private int attemptNumber = 1;

    public QuizAttempt(String attemptId, String studentId, String courseId, String lessonId, String quizId, List<Answer> answers, int attemptNumber) {
        this.attemptId = attemptId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.quizId = quizId;
        this.answers = answers;
        this.attemptNumber = attemptNumber;
        this.timestamp = Instant.now();
    }

    public String getAttemptId() {
        return attemptId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getQuizId() {
        return quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }
}
