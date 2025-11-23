package instructorFrontEnd;

// ChartFrame.java
// Standalone JFrame that renders analytics charts using JFreeChart.
// Requires JFreeChart in pom.xml (org.jfree:jfreechart:1.5.3)

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class ChartFrame extends JFrame {

    public ChartFrame(String title,
                      Map<String, List<Number>> studentPerf,
                      Map<String, Double> quizAvg,
                      Map<String, Double> completion) {

        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        top.add(titleLabel);
        add(top, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 3, 8, 8));
        grid.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // datasets
        DefaultCategoryDataset studentDs = buildStudentDataset(studentPerf);
        DefaultCategoryDataset quizDs = buildQuizDataset(quizAvg);
        DefaultPieDataset completionDs = buildCompletionDataset(completion);

        // charts
        JFreeChart bar = ChartFactory.createBarChart(
                "Student Average Scores",
                "Student",
                "Score",
                studentDs,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        JFreeChart line = ChartFactory.createLineChart(
                "Quiz Averages per Lesson",
                "Lesson",
                "Average Score",
                quizDs,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        JFreeChart pie = ChartFactory.createPieChart(
                "Completion Percentages",
                completionDs,
                true,
                true,
                false
        );

        // panels
        grid.add(new ChartPanel(bar));
        grid.add(new ChartPanel(line));
        grid.add(new ChartPanel(pie));

        add(grid, BorderLayout.CENTER);
    }

    // -------- Dataset builders ----------

    private DefaultCategoryDataset buildStudentDataset(Map<String, List<Number>> data) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        if (data == null) return ds;

        for (Map.Entry<String, List<Number>> e : data.entrySet()) {
            String student = e.getKey();
            List<Number> scores = e.getValue();
            double sum = 0;
            for (Number n : scores) sum += n.doubleValue();
            double avg = scores.isEmpty() ? 0 : sum / scores.size();
            ds.addValue(avg, "Average", student);
        }
        return ds;
    }

    private DefaultCategoryDataset buildQuizDataset(Map<String, Double> quizAvg) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        if (quizAvg == null) return ds;

        for (Map.Entry<String, Double> e : quizAvg.entrySet()) {
            ds.addValue(e.getValue(), "Avg", e.getKey());
        }
        return ds;
    }

    private DefaultPieDataset buildCompletionDataset(Map<String, Double> completion) {
        DefaultPieDataset ds = new DefaultPieDataset();
        if (completion == null) return ds;

        for (Map.Entry<String, Double> e : completion.entrySet()) {
            ds.setValue(e.getKey(), e.getValue());
        }
        return ds;
    }

    // ---------- Sample Data (for testing) ----------

    public static ChartFrame withSampleData() {

        Map<String, List<Number>> perf = new LinkedHashMap<>();
        perf.put("Alice", new ArrayList<>(Arrays.asList(80, 90, 85)));
        perf.put("Bob",   new ArrayList<>(Arrays.asList(60, 70, 75)));
        perf.put("Caro",  new ArrayList<>(Arrays.asList(95, 92, 98)));

        Map<String, Double> quiz = new LinkedHashMap<>();
        quiz.put("Lesson 1", 78.0);
        quiz.put("Lesson 2", 64.5);
        quiz.put("Lesson 3", 82.0);

        Map<String, Double> comp = new LinkedHashMap<>();
        comp.put("Lesson 1", 90.0);
        comp.put("Lesson 2", 75.0);
        comp.put("Lesson 3", 60.0);

        return new ChartFrame("Course Insights", perf, quiz, comp);
    }
} 