package models;

public class Answer {
    private String questionId;
    private int selectedIndex;

    public Answer(String questionId, int selectedIndex) {
        this.questionId = questionId;
        this.selectedIndex = selectedIndex;
    }

    public String getQuestionId() {
        return questionId;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

}
