import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StudentMentalHealthGUI extends JFrame {

    // ── Colors ──
    final Color BG          = new Color(235, 240, 255);
    final Color WHITE       = Color.WHITE;
    final Color BLUE        = new Color(67, 97, 238);
    final Color BLUE_DARK   = new Color(45, 70, 200);
    final Color TEXT_DARK   = new Color(25, 30, 60);
    final Color TEXT_MUTED  = new Color(110, 120, 160);
    final Color GREEN       = new Color(22, 163, 74);
    final Color ORANGE      = new Color(234, 88, 12);
    final Color RED         = new Color(200, 30, 30);
    final Color CARD_BORDER = new Color(210, 218, 248);

    // ── Data ──
    ArrayList<StudentRecord> records = new ArrayList<>();
    final String FILE_NAME = "mental_health_records.txt";
    DefaultTableModel tableModel;
    DefaultTableModel historyModel;
    int editingIndex = -1;

    // ── Form fields ──
    JTextField nameField, ageField;
    JSlider sleepSlider, stressSlider, moodSlider, socialSlider, focusSlider;
    JLabel sleepVal, stressVal, moodVal, socialVal, focusVal;
    JButton saveBtn;
    JLabel formTitle;

    JTabbedPane tabs;

    public StudentMentalHealthGUI() {
        setTitle("Student Mental Health Checker");
        setSize(720, 9850);
        setMinimumSize(new Dimension(720, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        loadFromFile();

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(BG);
        tabs.addTab("  Check  ",   buildFormTab());
        tabs.addTab("  Records  ", buildRecordsTab());
        tabs.addTab("  History  ", buildHistoryTab());
        setContentPane(tabs);
        setVisible(true);
    }

    // ══════════════════════════════════════════
    //  TAB 1 — FORM
    // ══════════════════════════════════════════
    JPanel buildFormTab() {
        JPanel root = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(225,232,255), 0, getHeight(), new Color(240,245,255)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Header
        JPanel header = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BLUE, getWidth(), getHeight(), BLUE_DARK));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255,255,255,20));
                g2.fillOval(getWidth()-120,-40,160,160);
                g2.fillOval(getWidth()-60,30,100,100);
            }
        };
        header.setPreferredSize(new Dimension(700, 90));
        header.setBorder(BorderFactory.createEmptyBorder(18, 28, 18, 28));

        formTitle = new JLabel("Add Student Record");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        formTitle.setForeground(WHITE);
        JLabel sub = new JLabel("Fill in the details and drag sliders to assess");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(190,205,255));

        JPanel ht = new JPanel(new GridLayout(2,1,0,4)); ht.setOpaque(false);
        ht.add(formTitle); ht.add(sub);
        header.add(ht, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // Info card
        JPanel infoInner = new JPanel(new GridLayout(0,1,0,8));
        infoInner.setOpaque(false);
        infoInner.add(makeFieldLabel("Full Name"));
        nameField = makeTextField("e.g. Anoosha Khan");
        infoInner.add(nameField);
        infoInner.add(makeFieldLabel("Age"));
        ageField = makeTextField("e.g. 19");
        infoInner.add(ageField);
        content.add(wrapCard("Student Information", infoInner));
        content.add(Box.createVerticalStrut(14));

        // Sliders card
        JPanel sliderInner = new JPanel();
        sliderInner.setLayout(new BoxLayout(sliderInner, BoxLayout.Y_AXIS));
        sliderInner.setOpaque(false);

        JLabel hint = new JLabel("Drag each slider:  1 = best    5 = worst");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(TEXT_MUTED);
        hint.setAlignmentX(LEFT_ALIGNMENT);
        sliderInner.add(hint);
        sliderInner.add(Box.createVerticalStrut(12));

        sleepVal=makeValLabel(); stressVal=makeValLabel();
        moodVal=makeValLabel();  socialVal=makeValLabel(); focusVal=makeValLabel();
        sleepSlider=makeSlider(sleepVal);   stressSlider=makeSlider(stressVal);
        moodSlider=makeSlider(moodVal);     socialSlider=makeSlider(socialVal);
        focusSlider=makeSlider(focusVal);

        sliderInner.add(makeSliderRow("Sleep Quality",         "Great vs Poor",        sleepSlider,  sleepVal,  new Color(99,102,241)));
        sliderInner.add(Box.createVerticalStrut(12));
        sliderInner.add(makeSliderRow("Stress Level",          "Calm vs Stressed",     stressSlider, stressVal, new Color(239,68,68)));
        sliderInner.add(Box.createVerticalStrut(12));
        sliderInner.add(makeSliderRow("Mood",                  "Happy vs Depressed",   moodSlider,   moodVal,   new Color(234,179,8)));
        sliderInner.add(Box.createVerticalStrut(12));
        sliderInner.add(makeSliderRow("Social Interaction",    "Active vs Isolated",   socialSlider, socialVal, new Color(20,184,166)));
        sliderInner.add(Box.createVerticalStrut(12));
        sliderInner.add(makeSliderRow("Focus / Concentration", "Sharp vs Distracted",  focusSlider,  focusVal,  new Color(168,85,247)));
        content.add(wrapCard("Assessment Questions", sliderInner));
        content.add(Box.createVerticalStrut(14));

        // Save button
        saveBtn = makeButton("CHECK MENTAL HEALTH", BLUE);
        saveBtn.addActionListener(e -> saveRecord());
        content.add(saveBtn);

       JScrollPane scroll = new JScrollPane(content);
scroll.setBorder(null);
scroll.setOpaque(false);
scroll.getViewport().setOpaque(false);
scroll.getVerticalScrollBar().setUnitIncrement(16);
scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
root.add(scroll, BorderLayout.CENTER);
        return root;
    }

    // ══════════════════════════════════════════
    //  TAB 2 — RECORDS TABLE
    // ══════════════════════════════════════════
    JPanel buildRecordsTab() {
        JPanel root = new JPanel(new BorderLayout(0,0));
        root.setBackground(BG);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BLUE);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("All Student Records");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(WHITE);
        topBar.add(title, BorderLayout.WEST);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnBar.setOpaque(false);
        JButton addNewBtn    = makeSmallButton("+ Add New",        WHITE,           BLUE);
        JButton deleteTopBtn = makeSmallButton("Delete Selected",  new Color(255,80,80), WHITE);
        addNewBtn.addActionListener(e -> { clearForm(); tabs.setSelectedIndex(0); });
        btnBar.add(addNewBtn);
        btnBar.add(deleteTopBtn);
        topBar.add(btnBar, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Name","Age","Score","Status","Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = buildStyledTable(tableModel);
        refreshTable();

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(10,14,0,14));
        tableScroll.setBackground(BG);
        root.add(tableScroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        btnPanel.setBackground(BG);

        JButton editBtn   = makeSmallButton("  Edit Selected",   BLUE,  WHITE);
        JButton deleteBtn = makeSmallButton("  Delete Selected", RED,   WHITE);
        JButton exportBtn = makeSmallButton("  Export to File",  GREEN, WHITE);

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a record to edit."); return; }
            loadRecordIntoForm(row);
            tabs.setSelectedIndex(0);
        });

        ActionListener doDelete = e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a record to delete."); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete record for: " + records.get(row).getName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                records.remove(row); saveToFile(); refreshTable(); refreshHistory();
            }
        };
        deleteBtn.addActionListener(doDelete);
        deleteTopBtn.addActionListener(doDelete);
        exportBtn.addActionListener(e -> exportSummary());

        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(exportBtn);
        root.add(btnPanel, BorderLayout.SOUTH);
        return root;
    }

    // ══════════════════════════════════════════
    //  TAB 3 — HISTORY
    // ══════════════════════════════════════════
    JPanel buildHistoryTab() {
        JPanel root = new JPanel(new BorderLayout(0,0));
        root.setBackground(BG);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BLUE);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("Patient History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(WHITE);
        topBar.add(title, BorderLayout.WEST);
        JLabel sub = new JLabel("All checks — most recent first");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(190,205,255));
        topBar.add(sub, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Name","Age","Score","Status","Date"};
        historyModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable histTable = buildStyledTable(historyModel);
        refreshHistory();

        JScrollPane scroll = new JScrollPane(histTable);
        scroll.setBorder(BorderFactory.createEmptyBorder(10,14,0,14));
        scroll.setBackground(BG);
        root.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottom.setBackground(BG);
        JButton delBtn = makeSmallButton("  Delete Selected", RED, WHITE);
        delBtn.addActionListener(e -> {
            int row = histTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a record to delete."); return; }
            String hName = (String) historyModel.getValueAt(row, 0);
            String hDate = (String) historyModel.getValueAt(row, 4);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete history entry for: " + hName + " on " + hDate + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                records.removeIf(r -> r.getName().equals(hName) && r.getDate().equals(hDate));
                saveToFile(); refreshTable(); refreshHistory();
            }
        });
        JButton exportBtn = makeSmallButton("  Export History", GREEN, WHITE);
        exportBtn.addActionListener(e -> exportSummary());
        bottom.add(delBtn);
        bottom.add(exportBtn);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    // ══════════════════════════════════════════
    //  RESULT POPUP WINDOW
    // ══════════════════════════════════════════
    void showResultWindow(String name, int total, String status, String suggestion) {
        JDialog dialog = new JDialog(this, "Mental Health Result", true);
        dialog.setSize(460, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        Color statusColor = total <= 10 ? GREEN : (total <= 17 ? ORANGE : RED);
        String emoji      = total <= 10 ? "✅" : (total <= 17 ? "⚠️" : "🔴");

        // Header bar
        JPanel header = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, statusColor, getWidth(), getHeight(), statusColor.darker()));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(460, 80));
        header.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JLabel nameLabel = new JLabel(emoji + "  " + name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(WHITE);

        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(255,255,255,200));

        JPanel hl = new JPanel(new GridLayout(2,1,0,4)); hl.setOpaque(false);
        hl.add(nameLabel); hl.add(statusLabel);
        header.add(hl, BorderLayout.CENTER);

        // Score badge on the right
        JLabel scoreBadge = new JLabel(total + "/25", SwingConstants.CENTER);
        scoreBadge.setFont(new Font("Segoe UI", Font.BOLD, 22));
        scoreBadge.setForeground(WHITE);
        scoreBadge.setPreferredSize(new Dimension(80, 50));
        header.add(scoreBadge, BorderLayout.EAST);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(new Color(248, 250, 255));
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Score bar
        JLabel scoreTitle = new JLabel("Score Breakdown");
        scoreTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        scoreTitle.setForeground(TEXT_MUTED);
        scoreTitle.setAlignmentX(LEFT_ALIGNMENT);
        body.add(scoreTitle);
        body.add(Box.createVerticalStrut(8));

        // Progress bar
        JPanel barBg = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 225, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                int filled = (int)((total / 25.0) * getWidth());
                g2.setColor(statusColor);
                g2.fillRoundRect(0, 0, filled, getHeight(), 10, 10);
            }
        };
        barBg.setOpaque(false);
        barBg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));
        barBg.setAlignmentX(LEFT_ALIGNMENT);
        body.add(barBg);
        body.add(Box.createVerticalStrut(18));

        // Suggestion
        JLabel suggTitle = new JLabel("Suggestion");
        suggTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        suggTitle.setForeground(TEXT_MUTED);
        suggTitle.setAlignmentX(LEFT_ALIGNMENT);
        body.add(suggTitle);
        body.add(Box.createVerticalStrut(6));

        JTextArea suggArea = new JTextArea(suggestion);
        suggArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        suggArea.setForeground(TEXT_DARK);
        suggArea.setLineWrap(true);
        suggArea.setWrapStyleWord(true);
        suggArea.setEditable(false);
        suggArea.setOpaque(false);
        suggArea.setBorder(null);
        suggArea.setAlignmentX(LEFT_ALIGNMENT);
        body.add(suggArea);
        body.add(Box.createVerticalStrut(20));

        // Close button
        JButton closeBtn = new JButton("Close") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, statusColor, getWidth(), getHeight(), statusColor.darker()));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setForeground(WHITE);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setAlignmentX(LEFT_ALIGNMENT);
        closeBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        closeBtn.addActionListener(e -> dialog.dispose());
        body.add(closeBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(body,   BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // Shared table styling
    JTable buildStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setGridColor(new Color(230,234,250));
        table.setSelectionBackground(new Color(220,228,255));
        table.setSelectionForeground(TEXT_DARK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240,243,255));
        table.getTableHeader().setForeground(BLUE);
        table.setShowVerticalLines(false);

        int statusCol = model.getColumnCount() - 2;

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(245,247,255));
                    String status = (String) model.getValueAt(row, statusCol);
                    if (col == statusCol) {
                        if (status.contains("Good"))           c.setForeground(GREEN);
                        else if (status.contains("Moderate"))  c.setForeground(ORANGE);
                        else                                   c.setForeground(RED);
                    } else {
                        c.setForeground(TEXT_DARK);
                    }
                }
                setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
                return c;
            }
        });
        return table;
    }

    // ══════════════════════════════════════════
    //  CRUD
    // ══════════════════════════════════════════
    void saveRecord() {
        String name = getVal(nameField, "e.g. Anoosha Khan");
        String ageT = getVal(ageField,  "e.g. 19");

        if (name.isEmpty() || ageT.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int age;
        try { age = Integer.parseInt(ageT); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number.", "Invalid Age", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int sleep=sleepSlider.getValue(), stress=stressSlider.getValue();
        int mood=moodSlider.getValue(),   social=socialSlider.getValue();
        int focus=focusSlider.getValue();
        int total = sleep + stress + mood + social + focus;

        MentalHealthResult r = new MentalHealthResult();
        String status     = r.getStatus(total).replace("✅","").replace("⚠️","").replace("🔴","").trim();
        String suggestion = r.getSuggestion(total);
        String date       = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        if (editingIndex >= 0) {
            StudentRecord rec = records.get(editingIndex);
            rec.setName(name); rec.setAge(age);
            rec.setScores(sleep, stress, mood, social, focus);
            rec.setStatus(status); rec.setDate(date);
            editingIndex = -1;
            formTitle.setText("Add Student Record");
            saveBtn.setText("CHECK MENTAL HEALTH");
        } else {
            records.add(new StudentRecord(name, age, sleep, stress, mood, social, focus, total, status, date));
        }

        saveToFile();
        refreshTable();
        refreshHistory();

        // Show result in a new popup window
        showResultWindow(name, total, status, suggestion);
    }

    void refreshTable() {
        tableModel.setRowCount(0);
        for (StudentRecord rec : records) tableModel.addRow(rec.toTableRow());
    }

    void refreshHistory() {
        if (historyModel == null) return;
        historyModel.setRowCount(0);
        for (int i = records.size()-1; i >= 0; i--) {
            StudentRecord rec = records.get(i);
            historyModel.addRow(new Object[]{
                rec.getName(),
                rec.getAge(),
                rec.getTotalScore() + "/25",
                rec.getStatus(),
                rec.getDate()
            });
        }
    }

    void loadRecordIntoForm(int index) {
        editingIndex = index;
        StudentRecord rec = records.get(index);
        setField(nameField, rec.getName(),             "e.g. Anoosha Khan");
        setField(ageField,  String.valueOf(rec.getAge()), "e.g. 19");
        sleepSlider.setValue(rec.getSleepScore());
        stressSlider.setValue(rec.getStressScore());
        moodSlider.setValue(rec.getMoodScore());
        socialSlider.setValue(rec.getSocialScore());
        focusSlider.setValue(rec.getFocusScore());
        formTitle.setText("Edit Record: " + rec.getName());
        saveBtn.setText("  UPDATE RECORD");
    }

    void exportSummary() {
        if (records.isEmpty()) { JOptionPane.showMessageDialog(this, "No records to export."); return; }
        try (PrintWriter pw = new PrintWriter(new FileWriter("summary_report.txt"))) {
            pw.println("========================================");
            pw.println("   STUDENT MENTAL HEALTH SUMMARY REPORT");
            pw.println("========================================");
            pw.println("Total Students: " + records.size());
            long good = records.stream().filter(r -> r.getStatus().contains("Good")).count();
            long mod  = records.stream().filter(r -> r.getStatus().contains("Moderate")).count();
            long high = records.stream().filter(r -> r.getStatus().contains("High")).count();
            pw.println("Good: " + good + "  |  Moderate: " + mod + "  |  High Stress: " + high);
            pw.println("----------------------------------------");
            for (StudentRecord rec : records) {
                pw.println("Name: "+rec.getName()+"  |  Age: "+rec.getAge());
                pw.println("Score: "+rec.getTotalScore()+"/25  |  Status: "+rec.getStatus()+"  |  Date: "+rec.getDate());
                pw.println("----------------------------------------");
            }
            JOptionPane.showMessageDialog(this, "Exported to summary_report.txt!", "Done", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
        }
    }

    // ══════════════════════════════════════════
    //  FILE I/O
    // ══════════════════════════════════════════
    void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (StudentRecord r : records) pw.println(r.toFileLine());
        } catch (IOException e) { e.printStackTrace(); }
    }

    void loadFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                StudentRecord r = StudentRecord.fromFileLine(line);
                if (r != null) records.add(r);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ══════════════════════════════════════════
    //  UI HELPERS
    // ══════════════════════════════════════════
    void clearForm() {
        editingIndex = -1;
        setField(nameField, "", "e.g. Anoosha Khan");
        setField(ageField,  "", "e.g. 19");
        sleepSlider.setValue(3); stressSlider.setValue(3);
        moodSlider.setValue(3);  socialSlider.setValue(3); focusSlider.setValue(3);
        formTitle.setText("Add Student Record");
        saveBtn.setText("CHECK MENTAL HEALTH");
    }

    void setField(JTextField f, String val, String placeholder) {
        if (val.isEmpty()) { f.setText(placeholder); f.setForeground(TEXT_MUTED); }
        else               { f.setText(val);          f.setForeground(TEXT_DARK); }
    }

    String getVal(JTextField f, String placeholder) {
        String t = f.getText().trim();
        return t.equals(placeholder) ? "" : t;
    }

    JPanel wrapCard(String title, JPanel inner) {
        JPanel card = new JPanel(new BorderLayout(0,12));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER,1),
            BorderFactory.createEmptyBorder(16,16,16,16)));
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(BLUE);
        JSeparator sep = new JSeparator(); sep.setForeground(CARD_BORDER);
        JPanel top = new JPanel(new BorderLayout(0,8)); top.setOpaque(false);
        top.add(lbl, BorderLayout.NORTH); top.add(sep, BorderLayout.SOUTH);
        card.add(top, BorderLayout.NORTH);
        card.add(inner, BorderLayout.CENTER);

        JPanel outer = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE); g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.setColor(CARD_BORDER); g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,16,16);
            }
        };
        outer.setOpaque(false); outer.setAlignmentX(LEFT_ALIGNMENT);
        outer.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    JPanel makeSliderRow(String label, String hint, JSlider slider, JLabel val, Color accent) {
        JPanel row = new JPanel(new BorderLayout(0,4));
        row.setOpaque(false); row.setAlignmentX(LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel labelRow = new JPanel(new BorderLayout()); labelRow.setOpaque(false);
        JLabel lbl  = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl.setForeground(TEXT_DARK);
        JLabel hint2 = new JLabel(hint);
        hint2.setFont(new Font("Segoe UI", Font.PLAIN, 11)); hint2.setForeground(TEXT_MUTED);
        labelRow.add(lbl, BorderLayout.WEST); labelRow.add(hint2, BorderLayout.EAST);

        val.setFont(new Font("Segoe UI", Font.BOLD, 12));
        val.setForeground(WHITE); val.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel badge = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
            }
        };
        badge.setOpaque(false); badge.setPreferredSize(new Dimension(34,26));
        val.setOpaque(false); badge.add(val, BorderLayout.CENTER);

        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
            public void paintThumb(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent);
                g2.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
                g2.setColor(WHITE); g2.setStroke(new BasicStroke(2));
                g2.drawOval(thumbRect.x+2, thumbRect.y+2, thumbRect.width-4, thumbRect.height-4);
            }
            public void paintTrack(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Rectangle t = trackRect; int cy = t.y + t.height/2;
                g2.setColor(new Color(220,225,245));
                g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(t.x, cy, t.x+t.width, cy);
                g2.setColor(accent);
                g2.drawLine(t.x, cy, thumbRect.x+thumbRect.width/2, cy);
            }
        });

        JPanel sliderRow = new JPanel(new BorderLayout(8,0)); sliderRow.setOpaque(false);
        sliderRow.add(slider, BorderLayout.CENTER);
        sliderRow.add(badge,  BorderLayout.EAST);
        row.add(labelRow, BorderLayout.NORTH);
        row.add(sliderRow, BorderLayout.CENTER);
        return row;
    }

    JSlider makeSlider(JLabel val) {
        JSlider s = new JSlider(1,5,3);
        s.setMajorTickSpacing(1); s.setPaintTicks(false);
        s.setPaintLabels(true); s.setOpaque(false);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(TEXT_MUTED);
        s.addChangeListener(e -> val.setText(String.valueOf(s.getValue())));
        return s;
    }

    JLabel makeValLabel() {
        JLabel l = new JLabel("3"); l.setHorizontalAlignment(SwingConstants.CENTER); return l;
    }

    JLabel makeFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_MUTED); return l;
    }

    JTextField makeTextField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setForeground(TEXT_MUTED); f.setBackground(new Color(248,250,255));
        f.setText(placeholder);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER,1),
            BorderFactory.createEmptyBorder(9,12,9,12)));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(TEXT_DARK); }
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(TEXT_MUTED); }
            }
        });
        return f;
    }

    JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,bg,getWidth(),getHeight(),bg.darker()));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(WHITE); b.setContentAreaFilled(false);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(580,48));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE,48));
        b.setAlignmentX(LEFT_ALIGNMENT);
        return b;
    }

    JButton makeSmallButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(),1),
            BorderFactory.createEmptyBorder(7,16,7,16)));
        return b;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(StudentMentalHealthGUI::new);
    }
}