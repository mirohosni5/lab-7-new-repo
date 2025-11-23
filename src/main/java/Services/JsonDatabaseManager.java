package Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Course;
import models.QuizAttempt;
import models.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JsonDatabaseManager {
    private static final String COURSES_FILE = "courses.json";
    private static final String USERS_FILE = "users.json";
    private static final Gson gson = new Gson();

    // ======== Courses ========

    public static List<Course> readCourses() {
        try (FileReader reader = new FileReader(COURSES_FILE)) {
            Type listType = new TypeToken<List<Course>>() {}.getType();
            List<Course> courses = gson.fromJson(reader, listType);
            return courses == null ? new ArrayList<>() : courses;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void writeCourses(List<Course> courses) {
        try (FileWriter writer = new FileWriter(COURSES_FILE)) {
            gson.toJson(courses, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized Course findCourseById(String courseId) {
        List<Course> courses = readCourses();
        for (Course c : courses) {
            if (String.valueOf(c.getCourseId()).equals(courseId)) {
                return c;
            }
        }
        return null;
    }

    public synchronized void addQuizAttemptForCourse(QuizAttempt attempt) {
        List<Course> courses = readCourses();
        for (Course c : courses) {
            if (String.valueOf(c.getCourseId()).equals(attempt.getCourseId())) {
                c.addQuizAttempt(attempt); // لازم تضيفي method في Course: addQuizAttempt
                break;
            }
        }
        writeCourses(courses);
    }

    // ======== Users ========

    public static List<User> readUsers() {
        try (FileReader reader = new FileReader(USERS_FILE)) {
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(reader, listType);
            return users == null ? new ArrayList<>() : users;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void writeUsers(List<User> users) {
        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addQuizAttemptForUser(QuizAttempt attempt) {
        List<User> users = readUsers();
        User user = null;
        for (User u : users) {
            if (u.getUserId().equals(attempt.getStudentId())) {
                user = u;
                break;
            }
        }
        if (user == null) {
            user = new User(attempt.getStudentId()); // لازم تضيفي constructor مناسب في User
            users.add(user);
        }
        user.addQuizAttempt(attempt); // لازم تضيفي method addQuizAttempt في User
        writeUsers(users);
    }

    public synchronized void markLessonCompletedForUser(String studentId, String courseId, String lessonId) {
        List<User> users = readUsers();
        for (User u : users) {
            if (u.getUserId().equals(studentId)) {
                u.markLessonCompleted(courseId, lessonId); // لازم تضيفي method markLessonCompleted في User
                break;
            }
        }
        writeUsers(users);
    }
}

