package Services;

import models.*;

public class QuizManager {
    private final CourseManager courseManager;
    private final AnalyticsService analytics;

    public QuizManager(CourseManager courseManager, AnalyticsService analytics) {
        this.courseManager = courseManager;
        this.analytics = analytics;
    }
}
