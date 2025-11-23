package Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Course;

import java.io.*;
import java.util.*;
import java.lang.reflect.Type;

public class JsonDatabaseManager {
    private static final String COURSES_FILE = "courses.json";
    private static final Gson gson = new Gson();

    public static List<Course> readCourses() {
        try (FileReader reader = new FileReader(COURSES_FILE)) {
            Type listType = new TypeToken<List<Course>>() {
            }.getType();
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

}
