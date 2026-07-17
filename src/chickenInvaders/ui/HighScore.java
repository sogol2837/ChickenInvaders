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
        background.setLayout(new BorderLayout(0, 10));
        background.setBorder(BorderFactory.createEmptyBorder(8, 40, 14, 40));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(1, 135));

        JLabel titleLabel = UiStyle.title("HIGH SCORES");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 6, 20));

        JLabel subtitleLabel = UiStyle.subtitle("best saved record for each pilot");
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(subtitleLabel);

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
        tableCard.setPreferredSize(new Dimension(620, 300));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(tableCard);

        JButton backButton = UiStyle.secondaryButton("BACK");
        backButton.setPreferredSize(new Dimension(130, 44));
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(1, 56));
        bottomPanel.add(backButton);

        background.add(topPanel, BorderLayout.NORTH);
        background.add(center, BorderLayout.CENTER);
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
        table.setFillsViewportHeight(true);

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
