package Services;

import models.Course;
import models.QuizAttempt;

import java.util.*;

public class CourseManager {

    private String generateCourseId() {
        return "COURSE_" + System.currentTimeMillis();
    }

    public Course createCourse(String title, String description, String instructorId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        String newId = generateCourseId();

        Course course = new Course(newId, title, description, instructorId, null, null, null, null, null, null, null);
        courses.add(course);

        JsonDatabaseManager.writeCourses(courses);
        return course;
    }

    public void updateCourse(String courseId, String newTitle, String newDescription) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        for (Course c : courses) {
            if (Objects.equals(c.getCourseId(), courseId)) {
                c.setTitle(newTitle);
                c.setDescription(newDescription);
                break;
            }
        }
        JsonDatabaseManager.writeCourses(courses);
    }

    public void deleteCourse(String courseId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        courses.removeIf(c -> Objects.equals(c.getCourseId(), courseId));
        JsonDatabaseManager.writeCourses(courses);
    }

    public Course getCourseById(String courseId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        for (Course c : courses) {
            if (Objects.equals(c.getCourseId(), courseId))
                return c;
        }
        return null;
    }

    public List<Course> getCoursesByInstructor(String instructorId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            if (Objects.equals(c.getCourseId(), instructorId)) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Course> getAllAvailableCourses() {
        return JsonDatabaseManager.readCourses();
    }

    public boolean enrollStudentInCourse(String studentId, String courseId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        for (Course c : courses) {
            if (Objects.equals(c.getCourseId(), courseId)) {
                if (c.getEnrolledStudents() == null) {
                    c.setEnrolledStudents(new ArrayList<>());
                }
                if (!c.getEnrolledStudents().contains(studentId)) {
                    c.getEnrolledStudents().add(studentId);
                    JsonDatabaseManager.writeCourses(courses);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public List<Course> getEnrolledCourses(String studentId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        List<Course> enrolledCourses = new ArrayList<>();
        for (Course c : courses) {
            if (c.getEnrolledStudents().contains(studentId)) {
                enrolledCourses.add(c);
            }
        }
        return enrolledCourses;
    }

}

