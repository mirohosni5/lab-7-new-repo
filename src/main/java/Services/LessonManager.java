package Services;

import models.Course;
import models.Lesson;
import models.Quiz;

import java.util.*;
import java.util.stream.Collectors;

/**
 * LessonManager - basic CRUD for lessons plus saving a Quiz to a lesson.
 * Relies on JsonDatabaseManager.readCourses() / writeCourses(List<Course>) existing in your project.
 */
public class LessonManager {

    private int generateLessonId(Course course) {
        List<Lesson> lessons = course.getLessons();
        int maxId = 0;
        if (lessons != null) {
            for (int i = 0; i < lessons.size(); i++) {
                maxId = Math.max(maxId, lessons.get(i).getLessonId());
            }
        }
        return maxId + 1;
    }

    public void addLesson(int courseId, String title, String content) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        for (Course c : courses) {
            if (c.getCourseId() == courseId) {
                int newId = generateLessonId(c);
                Lesson lesson = new Lesson(newId, title, content, List.of());
                if (c.getLessons() == null) c.setLessons(new ArrayList<>());
                c.getLessons().add(lesson);
                break;
            }
        }
        JsonDatabaseManager.writeCourses(courses);
    }

    public void updateLesson(int courseId, int lessonId, String newTitle, String newContent) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        for (Course c : courses) {
            if (c.getCourseId() == courseId && c.getLessons() != null) {
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
            if (c.getCourseId() == courseId && c.getLessons() != null) {
                c.getLessons().removeIf(l -> l.getLessonId() == lessonId);
            }
        }
        JsonDatabaseManager.writeCourses(courses);
    }

    public List<Lesson> getLessons(int courseId) {
        CourseManager cm = new CourseManager();
        Course c = cm.getCourseById(courseId);

        if (c != null && c.getLessons() != null)
            return c.getLessons();

        return List.of();
    }

    /**
     * Save a Quiz to a specific lesson inside a course and persist to JSON.
     * Returns true if save succeeded.
     */
   // Save quiz into the lesson object and persist courses.json via JsonDatabaseManager
public boolean saveQuizToLesson(int courseId, int lessonId, models.Quiz quiz) {
    try {
        List<models.Course> courses = JsonDatabaseManager.readCourses();
        boolean changed = false;
        for (models.Course c : courses) {
            if (c.getCourseId() == courseId) {
                for (models.Lesson l : c.getLessons()) {
                    if (l.getLessonId() == lessonId) {
                        l.setQuiz(quiz);
                        changed = true;
                        break;
                    }
                }
            }
            if (changed) break;
        }
        if (changed) {
            JsonDatabaseManager.writeCourses(courses);
            return true;
        } else {
            return false;
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}

    /**
     * Helper: find lesson object by ids (nullable)
     */
    public Lesson findLesson(int courseId, int lessonId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        for (Course c : courses) {
            if (c.getCourseId() == courseId && c.getLessons() != null) {
                for (Lesson l : c.getLessons()) {
                    if (l.getLessonId() == lessonId) return l;
                }
            }
        }
        return null;
    }
}
