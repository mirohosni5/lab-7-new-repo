package models;

import java.util.List;

public class Quiz {
    private String quizId;
    private String title;
    private List<Question> questions;
    private int passPercent = 50; // default 50%

    public Quiz(String quizId, String title, List<Question> questions, int passPercent) {
        this.quizId = quizId;
        this.title = title;
        this.questions = questions;
        this.passPercent = passPercent;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getPassPercent() {
        return passPercent;
    }

    public void setPassPercent(int passPercent) {
        this.passPercent = passPercent;
    }

    public int totalPoints() {
        int sum = 0;
        for (Question q : questions) {
            sum += q.getPoints();
        }
        return sum;
    }

}