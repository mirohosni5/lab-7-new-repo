package instructorFrontEnd;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAYAR
 */



import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LessonManagerDialog extends JDialog {
    private JList<LessonData> jListLessons;
    private JButton btnAdd, btnEdit, btnDelete, btnClose;
    private DefaultListModel<LessonData> model;
    private String courseTitle; 

  
    public LessonManagerDialog(Frame parent, boolean modal, String courseTitle, List<LessonData> initial) {
        super(parent, modal);
        this.courseTitle = courseTitle;
        initComponents();
        setLocationRelativeTo(parent);

        if (initial != null) {
            for (LessonData ld : initial) model.addElement(ld);
        }
    }

    private void initComponents() {
        model = new DefaultListModel<>();
        jListLessons = new JList<>(model);
        jListLessons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListLessons.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LessonData) setText(((LessonData) value).getTitle());
                return this;
            }
        });

        btnAdd = new JButton("Add");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnClose = new JButton("Close");

        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnClose.addActionListener(e -> dispose());

        JScrollPane scroll = new JScrollPane(jListLessons);

        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 6, 6));
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClose);

        setTitle("Manage Lessons - " + (courseTitle == null ? "" : courseTitle));
        setLayout(new BorderLayout(8, 8));
        add(scroll, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        setSize(450, 300);
    }

    // --- actions ---
    private void onAdd() {
        LessonEditorDialog ed = new LessonEditorDialog((Frame) getOwner(), true, null);
        ed.setLocationRelativeTo(this);
        ed.setVisible(true);
        if (!ed.isSaved()) return;
        LessonData ld = ed.getLesson();
        model.addElement(ld);
        jListLessons.setSelectedIndex(model.size() - 1);
    }

    private void onEdit() {
        int idx = jListLessons.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Select a lesson first.");
            return;
        }
        LessonData current = model.getElementAt(idx);
        LessonEditorDialog ed = new LessonEditorDialog((Frame) getOwner(), true, current);
        ed.setLocationRelativeTo(this);
        ed.setVisible(true);
        if (!ed.isSaved()) return;
        LessonData updated = ed.getLesson();
        model.set(idx, updated);
        jListLessons.setSelectedIndex(idx);
    }

    private void onDelete() {
        int idx = jListLessons.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Select a lesson first.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Delete selected lesson?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        model.remove(idx);
    }

    
    public List<LessonData> getLessons() {
        List<LessonData> out = new ArrayList<>();
        for (int i = 0; i < model.size(); i++) out.add(model.getElementAt(i));
        return out;
    }

    
    public static class LessonData {
        private String id;
        private String title;
        private String content;

        public LessonData() {}
        public LessonData(String id, String title, String content) { this.id=id; this.title=title; this.content=content; }

        public String getId(){ return id; }
        public void setId(String id){ this.id = id; }
        public String getTitle(){ return title; }
        public void setTitle(String title){ this.title = title; }
        public String getContent(){ return content; }
        public void setContent(String content){ this.content = content; }

        @Override public String toString(){ return title == null ? "<no title>" : title; }
    }

    private static class LessonEditorDialog extends JDialog {
        private JTextField txtTitle;
        private JTextArea txtContent;
        private JButton btnSave, btnCancel;
        private boolean saved = false;
        private LessonData lesson;

        LessonEditorDialog(Frame parent, boolean modal, LessonData initial) {
            super(parent, modal);
            initComponents();
            if (initial != null) {
                this.lesson = new LessonData(initial.getId(), initial.getTitle(), initial.getContent());
                txtTitle.setText(lesson.getTitle());
                txtContent.setText(lesson.getContent());
            } else {
                this.lesson = new LessonData();
                this.lesson.setId(java.util.UUID.randomUUID().toString());
            }
            setLocationRelativeTo(parent);
        }

        private void initComponents() {
            txtTitle = new JTextField();
            txtContent = new JTextArea(8, 30);
            JScrollPane sp = new JScrollPane(txtContent);
            btnSave = new JButton("Save");
            btnCancel = new JButton("Cancel");
            btnSave.addActionListener(e -> onSave());
            btnCancel.addActionListener(e -> onCancel());

            JPanel top = new JPanel(new BorderLayout(6,6));
            top.add(new JLabel("Title:"), BorderLayout.WEST);
            top.add(txtTitle, BorderLayout.CENTER);

            JPanel btnp = new JPanel();
            btnp.add(btnSave);
            btnp.add(btnCancel);

            setLayout(new BorderLayout(8,8));
            add(top, BorderLayout.NORTH);
            add(sp, BorderLayout.CENTER);
            add(btnp, BorderLayout.SOUTH);
            pack();
            setTitle("Lesson Editor");
        }

        private void onSave() {
            String t = txtTitle.getText().trim();
            if (t.isEmpty()) { JOptionPane.showMessageDialog(this, "Title required."); txtTitle.requestFocus(); return; }
            lesson.setTitle(t);
            lesson.setContent(txtContent.getText());
            saved = true;
            dispose();
        }
        private void onCancel() { saved = false; dispose(); }

        public boolean isSaved() { return saved; }
        public LessonData getLesson() { return lesson; }
    }
}
