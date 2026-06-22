package chickenInvaders.ui;

import chickenInvaders.GameMain;
import chickenInvaders.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HighScore extends JPanel {

    private final GameMain app;
    private final DefaultTableModel tableModel;

    public HighScore(GameMain app) {
        this.app = app;

        setLayout(new BorderLayout());
        setBackground(new Color(20, 24, 35));

        JLabel titleLabel = new JLabel("High Scores", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        tableModel = new DefaultTableModel(
            new Object[]{"Username", "High Score", "Last Level"},
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

        List<User> users = app.getUserRepository().getHighScoreUsers();

        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getUsername(),
                user.getHighScore(),
                user.getLastLevel()
            });
        }
    }
}
