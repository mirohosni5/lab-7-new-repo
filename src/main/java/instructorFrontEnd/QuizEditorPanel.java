package instructorFrontEnd;

import models.Question;
import models.Quiz;
import models.Lesson;
import Services.LessonManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Hand-coded Swing panel for instructors to create/edit a Quiz for a lesson.
 * Use setContext(courseId, lessonId) before saving if you want to load/save to backend.
 */
public class QuizEditorPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField txtQuizTitle;
    private JSpinner spinPassPercent;
    private DefaultListModel<QuestionItem> questionListModel;
    private JList<QuestionItem> lstQuestions;

    // right side editor fields
    private JTextField txtQuestionTitle;
    private JTextArea txtOptions;
    private JTextField txtCorrectAnswer;
    private JButton btnSaveQuiz;
    private JButton btnAddQuestion;
    private JButton btnDeleteQuestion;

    // backend
    private LessonManager lessonManager;
    // context (set by caller)
    private int courseId = -1;
    private int lessonId = -1;

    public QuizEditorPanel(LessonManager lessonManager1) {
        this(new LessonManager()); // default manager
    }

    public QuizEditorPanel(LessonManager lessonManager) {
        this.lessonManager = lessonManager;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8,8));

        // --- Top (quiz meta) ---
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.add(new JLabel("Quiz Title:"));
        txtQuizTitle = new JTextField(20);
        top.add(txtQuizTitle);
        top.add(new JLabel("Pass %:"));
        spinPassPercent = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));
        top.add(spinPassPercent);
        add(top, BorderLayout.NORTH);

        // --- Center (split: left list, right editor) ---
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.34);

        // left: question list + controls
        JPanel left = new JPanel(new BorderLayout(4,4));
        questionListModel = new DefaultListModel<>();
        lstQuestions = new JList<>(questionListModel);
        lstQuestions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        left.add(new JScrollPane(lstQuestions), BorderLayout.CENTER);

        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        btnAddQuestion = new JButton("Add");
        btnDeleteQuestion = new JButton("Delete");
        leftButtons.add(btnAddQuestion);
        leftButtons.add(btnDeleteQuestion);
        left.add(leftButtons, BorderLayout.SOUTH);

        // right: question editor
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        right.add(new JLabel("Question Title:"));
        txtQuestionTitle = new JTextField();
        txtQuestionTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtQuestionTitle.getPreferredSize().height));
        right.add(txtQuestionTitle);

        right.add(Box.createVerticalStrut(6));
        right.add(new JLabel("Options (one per line):"));
        txtOptions = new JTextArea(7, 30);
        right.add(new JScrollPane(txtOptions));

        right.add(Box.createVerticalStrut(6));
        right.add(new JLabel("Correct Answer (exact text from options):"));
        txtCorrectAnswer = new JTextField();
        txtCorrectAnswer.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtCorrectAnswer.getPreferredSize().height));
        right.add(txtCorrectAnswer);

        split.setLeftComponent(left);
        split.setRightComponent(right);
        add(split, BorderLayout.CENTER);

        // --- Bottom (actions) ---
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnSaveQuiz = new JButton("Save Quiz");
        JButton btnLoadSample = new JButton("Load Sample");
        bottom.add(btnLoadSample);
        bottom.add(btnSaveQuiz);
        add(bottom, BorderLayout.SOUTH);

        // --- events ---
        btnAddQuestion.addActionListener(e -> onAddQuestion());
        btnDeleteQuestion.addActionListener(e -> onDeleteQuestion());
        lstQuestions.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // make sure edits for previously selected item are saved
                updateSelectedFromEditor();
                populateEditorFromSelected();
            }
        });
        btnSaveQuiz.addActionListener(e -> onSaveQuiz());
        btnLoadSample.addActionListener(e -> loadSample());

        // double click a list item to edit (optional UX)
        lstQuestions.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) populateEditorFromSelected();
            }
        });
    }

    // set context (caller must provide course/lesson ids before saving)
    // This method will try to load an existing quiz for the lesson (if exists).
    public void setContext(int courseId, int lessonId) {
        this.courseId = courseId;
        this.lessonId = lessonId;

        // load existing quiz from backend (if available)
        try {
            java.util.List<Lesson> lessons = lessonManager.getLessons(courseId);
            if (lessons != null) {
                for (Lesson l : lessons) {
                    if (l.getLessonId() == lessonId) {
                        models.Quiz q = l.getQuiz();
                        if (q != null) {
                            loadQuizIntoPanel(q);
                        } else {
                            // clear editor for empty quiz
                            clearEditor();
                        }
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            // ignore: fallback to empty editor
            ex.printStackTrace();
        }
    }

    private void clearEditor() {
        txtQuizTitle.setText("");
        spinPassPercent.setValue(50);
        questionListModel.clear();
        txtQuestionTitle.setText("");
        txtOptions.setText("");
        txtCorrectAnswer.setText("");
    }

    private void loadQuizIntoPanel(models.Quiz q) {
        if (q == null) {
            clearEditor();
            return;
        }

        txtQuizTitle.setText(q.getTitle() == null ? "" : q.getTitle());
        try {
            spinPassPercent.setValue(q.getPassPercent());
        } catch (Throwable t) {
            spinPassPercent.setValue(50);
        }

        questionListModel.clear();
        if (q.getQuestions() != null) {
            for (models.Question mq : q.getQuestions()) {
                String qTitle = mq.getTitle() == null ? "" : mq.getTitle();
                java.util.List<String> opts = mq.getOptions() == null ? java.util.List.of() : mq.getOptions();
                String correct = "";
                try { correct = mq.getCorrect(); } catch (Throwable ignore) {}
                if (correct == null) correct = "";
                questionListModel.addElement(new QuestionItem(qTitle, opts, correct));
            }
        }
        if (!questionListModel.isEmpty()) lstQuestions.setSelectedIndex(0);
    }

    private void onAddQuestion() {
        // create an empty question item and select it
        QuestionItem qi = new QuestionItem("New question", Arrays.asList("Option 1","Option 2"), "Option 1");
        questionListModel.addElement(qi);
        lstQuestions.setSelectedIndex(questionListModel.size()-1);
    }

    private void onDeleteQuestion() {
        int idx = lstQuestions.getSelectedIndex();
        if (idx < 0) return;
        int ok = JOptionPane.showConfirmDialog(this, "Delete selected question?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        questionListModel.remove(idx);
    }

    private void populateEditorFromSelected() {
        QuestionItem qi = lstQuestions.getSelectedValue();
        if (qi == null) {
            txtQuestionTitle.setText("");
            txtOptions.setText("");
            txtCorrectAnswer.setText("");
            return;
        }
        txtQuestionTitle.setText(qi.title);
        txtOptions.setText(String.join("\n", qi.options));
        txtCorrectAnswer.setText(qi.correct);
    }

    private void updateSelectedFromEditor() {
        int idx = lstQuestions.getSelectedIndex();
        if (idx < 0) return;
        QuestionItem qi = questionListModel.get(idx);
        if (qi == null) return;
        qi.title = txtQuestionTitle.getText().trim();
        qi.options = Arrays.stream(txtOptions.getText().split("\\R"))
                           .map(String::trim)
                           .filter(s -> !s.isEmpty())
                           .collect(Collectors.toList());
        qi.correct = txtCorrectAnswer.getText().trim();
        // refresh list display
        questionListModel.set(idx, qi); // replace to notify model listeners
    }

    private void onSaveQuiz() {
        // ensure editor selection is committed
        updateSelectedFromEditor();

        String quizTitle = txtQuizTitle.getText().trim();
        if (quizTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quiz title required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // basic validation: ensure correct answer is present in options
        for (int i=0;i<questionListModel.size();i++) {
            QuestionItem qi = questionListModel.get(i);
            if (qi.correct != null && !qi.correct.isEmpty() && !qi.options.contains(qi.correct)) {
                int ok = JOptionPane.showConfirmDialog(this,
                        "Question \"" + qi.title + "\" has a correct answer not present in options. Continue?",
                        "Validation", JOptionPane.YES_NO_OPTION);
                if (ok != JOptionPane.YES_OPTION) return;
            }
        }

        if (questionListModel.isEmpty()) {
            int ok = JOptionPane.showConfirmDialog(this, "No questions were added. Save empty quiz?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;
        }

        int pass = (Integer) spinPassPercent.getValue();

        // build Quiz model (uses your models.Quiz)
        List<models.Question> qlist = new ArrayList<>();
        int qid = 1;
        for (int i = 0; i < questionListModel.size(); i++) {
            QuestionItem qi = questionListModel.get(i);
            models.Question mq = new models.Question(qid++, qi.title, qi.options, qi.correct, 1); // using 1 point default
            qlist.add(mq);
        }

        String quizId = UUID.randomUUID().toString();
        models.Quiz quizModel = new models.Quiz(quizId, quizTitle, qlist, pass);

        // Save to backend (LessonManager.saveQuizToLesson implemented in updated LessonManager)
        try {
            if (courseId < 0 || lessonId < 0) {
                JOptionPane.showMessageDialog(this, "Missing context: course/lesson id. Provide setContext(courseId, lessonId) before saving.", "Context missing", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean ok = lessonManager.saveQuizToLesson(courseId, lessonId, quizModel);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Quiz saved.", "Saved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save quiz (lessonManager returned false).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NoSuchMethodError nsme) {
            JOptionPane.showMessageDialog(this, "Backend method saveQuizToLesson not implemented. Add it to LessonManager or change QuizEditorPanel to call your API.", "Not Implemented", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save quiz:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadSample() {
        txtQuizTitle.setText("Sample Quiz");
        spinPassPercent.setValue(60);
        questionListModel.clear();
        questionListModel.addElement(new QuestionItem("What is 2+2?", Arrays.asList("3","4","5"), "4"));
        questionListModel.addElement(new QuestionItem("Select true statement", Arrays.asList("Cats fly","Water is wet","Sun is cold"), "Water is wet"));
        if (!questionListModel.isEmpty()) lstQuestions.setSelectedIndex(0);
    }

    // small internal holder to keep display simple
    private static class QuestionItem {
        String title;
        List<String> options;
        String correct;
        QuestionItem(String title, List<String> options, String correct) {
            this.title = title; this.options = new ArrayList<>(options); this.correct = correct;
        }
        @Override public String toString() {
            String t = title == null || title.isEmpty() ? "<no title>" : title;
            return t + " [" + options.size() + " opts]";
        }
    }
}
