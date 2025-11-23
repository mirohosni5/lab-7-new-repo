package Services;

import models.*;

public class QuizManager {
    private final JsonDatabaseManager db;
    private final AnalyticsService analytics;

    public QuizManager(JsonDatabaseManager db, AnalyticsService analytics) {
        this.db = db;
        this.analytics = analytics;
    }

}
