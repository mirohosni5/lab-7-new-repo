package Services;

import models.QuizAttempt;

import java.util.*;

public class AnalyticsService {
    private final List<QuizAttempt> allAttempts = new ArrayList<>();

    public void recordAttempt(QuizAttempt attempt, int totalPoints) {
        allAttempts.add(attempt);
    }

    
}