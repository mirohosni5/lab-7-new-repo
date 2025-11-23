package models;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private List<Integer> students;
    private List<Lesson> lessons;

    public Course(int courseId, String title, String description, int instructorId){
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public List<Integer> getStudents() {
        return students;
    }

    public void setStudents(List<Integer> students) {
        this.students = students;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
     public String toString(){
     return title + " (ID: " + courseId + ")";
 }
}
