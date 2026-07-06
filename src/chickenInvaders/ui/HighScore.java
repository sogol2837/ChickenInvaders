package chickenInvaders.ui;

import chickenInvaders.GameMain;
import chickenInvaders.data.ScoreRepository;
import chickenInvaders.model.GameRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HighScore extends JPanel {

    private final ScoreRepository scoreRepository;
    private final DefaultTableModel tableModel;

    public HighScore(GameMain app) {
        this.scoreRepository = new ScoreRepository();

        setLayout(new BorderLayout());
        setBackground(new Color(20, 24, 35));

        JLabel titleLabel = new JLabel("High Scores", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        tableModel = new DefaultTableModel(
            new Object[]{"Username", "High Score", "Level Reached", "Date"},
            0
        );

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);

        List<GameRecord> records = scoreRepository.getBestRecordPerUser();

        for (GameRecord record : records) {
            tableModel.addRow(new Object[]{
                record.getUsername(),
                record.getScore(),
                record.getLevelReached(),
                record.getFormattedTimestamp()
            });
        }
    }
}
