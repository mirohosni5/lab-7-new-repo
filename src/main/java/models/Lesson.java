package models;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private int lessonId;
    private String title;
    private String content;
    private List<String> resources;

    public Lesson(int lessonId, String title, String content, List<String> resources){
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = resources;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }
}
