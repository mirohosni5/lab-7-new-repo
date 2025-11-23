package Services;

import models.Course;
import models.Lesson;

import java.util.*;
import java.lang.Math;

public class LessonManager {
    /* private int generateLessonId(List<Lesson> Lesson) {
        return (int) (Math.random() * 9000) + 1000;
    }
    momken a3melha keda ba 2olt a3'yar 3n tare2t el courseID */

    private int generateLessonId(Course course) {
        List<Lesson> lessons = course.getLessons();
        int maxId = 0;
        for (int i = 0; i < lessons.size(); i++) {
            maxId = Math.max(maxId, lessons.get(i).getLessonId());
        }
        return maxId + 1;
    }

    public void addLesson(int courseId, String title, String content) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        for (Course c : courses) {
            if (c.getCourseId() == courseId) {
                int newId = generateLessonId(c);
                Lesson lesson = new Lesson(newId, title, content, List.of());
                c.getLessons().add(lesson);
                break;
            }
        }
        JsonDatabaseManager.writeCourses(courses);
    }

    public void updateLesson(int courseId, int lessonId, String newTitle, String newContent) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        for (Course c : courses) {
            if (c.getCourseId() == courseId) {
                for (Lesson l : c.getLessons()) {
                    if (l.getLessonId() == lessonId) {
                        l.setTitle(newTitle);
                        l.setContent(newContent);
                        break;
                    }
                }
            }
        }
        JsonDatabaseManager.writeCourses(courses);
    }

    public void deleteLesson(int courseId, int lessonId) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        for (Course c : courses) {
            if (c.getCourseId() == courseId) {
                c.getLessons().removeIf(l -> l.getLessonId() == lessonId);
            }
        }
        JsonDatabaseManager.writeCourses(courses);
    }

    public List<Lesson> getLessons(int courseId) {
        CourseManager cm = new CourseManager();
        Course c = cm.getCourseById(courseId);

        if (c != null)
            return c.getLessons();

        return List.of();
    }

}
