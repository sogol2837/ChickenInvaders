package chickenInvaders.ui;

import chickenInvaders.GameMain;
import chickenInvaders.data.ScoreRepository;
import chickenInvaders.model.GameRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class HighScore extends JPanel {

    private final ScoreRepository scoreRepository;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public HighScore(GameMain app) {
        this.scoreRepository = new ScoreRepository();

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel background = UiStyle.createBackgroundPanel();
        background.setBorder(BorderFactory.createEmptyBorder(22, 40, 30, 40));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        topPanel.add(UiStyle.title("HIGH SCORES"));
        topPanel.add(UiStyle.subtitle("best saved record for each pilot"));

        tableModel = new DefaultTableModel(
            new Object[]{"Pilot", "Score", "Level", "Date"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = UiStyle.scrollPane(table);

        JPanel tableCard = UiStyle.card();
        tableCard.setLayout(new BorderLayout());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = UiStyle.secondaryButton("BACK");
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        bottomPanel.add(backButton);

        background.add(topPanel, BorderLayout.NORTH);
        background.add(tableCard, BorderLayout.CENTER);
        background.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleTable(JTable table) {
        table.setBackground(new Color(5, 8, 14));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(45, 60, 75));
        table.setSelectionBackground(new Color(0, 90, 115));
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(32);
        table.setFont(new Font("Monospaced", Font.PLAIN, 14));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(18, 28, 40));
        header.setForeground(UiStyle.CYAN);
        header.setFont(new Font("Monospaced", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(UiStyle.CYAN_DARK));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setBackground(new Color(5, 8, 14));
        renderer.setForeground(Color.WHITE);
        table.setDefaultRenderer(Object.class, renderer);
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
