package Services;

import models.Course;

import java.util.*;

public class CourseManager {
    private int generateCourseId(List<Course> courses) {
        return (int) (Math.random() * 9000) + 1000;
    }

    public Course createCourse(String title, String description, int instructorId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        int newId = generateCourseId(courses);

        Course course = new Course(newId, title, description, instructorId);
        courses.add(course);

        JsonDatabaseManager.writeCourses(courses);
        return course;
    }

    public void updateCourse(int courseId, String newTitle, String newDescription) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        for (Course c : courses) {
            if (c.getCourseId() == courseId) {
                c.setTitle(newTitle);
                c.setDescription(newDescription);
                break;
            }
        }
        JsonDatabaseManager.writeCourses(courses);
    }

    public void deleteCourse(int courseId) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        courses.removeIf(c -> c.getCourseId() == courseId);

        JsonDatabaseManager.writeCourses(courses);
    }

    public Course getCourseById(int courseId) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        for (Course c : courses) {
            if (c.getCourseId() == courseId)
                return c;
        }
        return null;
    }

    public List<Course> getCoursesByInstructor(int instructorId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        List<Course> result = new ArrayList<>();

        for (Course c : courses) {
            if (c.getInstructorId() == instructorId) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Course> getAllAvailableCourses() {
        return JsonDatabaseManager.readCourses();
    }

    public boolean enrollStudentInCourse(int studentId, int courseId) {
        List<Course> courses = JsonDatabaseManager.readCourses();

        for (Course c : courses) {
            if (c.getCourseId() == courseId) {
                if (!c.getStudents().contains(studentId)) {
                    c.getStudents().add(studentId);
                    JsonDatabaseManager.writeCourses(courses);
                    return true;
                } else {
                    return false;  //student already  previously registered
                }
            }
        }
        return false;  //if course is not found
    }

    public List<Course> getEnrolledCourses(int studentId) {
        List<Course> courses = JsonDatabaseManager.readCourses();
        List<Course> enrolledCourses = new ArrayList<>();

        for (Course c : courses) {
            if (c.getStudents().contains(studentId)) {
                enrolledCourses.add(c);
            }
        }
        return enrolledCourses;
    }


}
