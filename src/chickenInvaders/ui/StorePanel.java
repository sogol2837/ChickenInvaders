package chickenInvaders.ui;

import chickenInvaders.GameMain;
import chickenInvaders.model.PlaneType;
import chickenInvaders.model.User;

import javax.swing.*;
import java.awt.*;


public class StorePanel extends JPanel {

    private final GameMain app;
    private final JPanel listPanel;

    public StorePanel(GameMain app) {
        this.app = app;

        setLayout(new BorderLayout());
        setBackground(new Color(20, 24, 35));

        JLabel titleLabel = new JLabel("Store", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(PlaneType.values().length, 1, 10, 10));
        listPanel.setOpaque(false);
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        listPanel.removeAll();

        User user = app.getCurrentUser();
        int highScore = (user == null) ? 0 : user.getHighScore();
        PlaneType current = (user == null) ? PlaneType.DEFAULT : user.getSelectedPlane();

        for (PlaneType type : PlaneType.values()) {
            listPanel.add(buildRow(type, highScore, current, user));
        }

        revalidate();
        repaint();
    }

    private JPanel buildRow(PlaneType type, int highScore, PlaneType current, User user) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(35, 40, 55));
        row.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String special = type.hasDoubleBossDamage() ? "  |  2x damage to bosses" : "";
        String info = String.format(
            "<html><b>%s</b> &nbsp; cost %d &nbsp; | &nbsp; speed %d &nbsp; | &nbsp; fire rate %dms &nbsp; | &nbsp; lives %d%s</html>",
            type.name(), type.getCost(), type.getSpeed(), type.getFireDelayMs(), type.getStartingLives(), special
        );

        JLabel label = new JLabel(info);
        label.setForeground(Color.WHITE);

        JButton actionButton = new JButton();
        boolean isCurrent = type == current;
        boolean canAfford = highScore >= type.getCost();

        if (isCurrent) {
            actionButton.setText("Equipped");
            actionButton.setEnabled(false);
        } else if (!canAfford) {
            actionButton.setText("Locked");
            actionButton.setEnabled(false);
        } else {
            actionButton.setText("Select");
            actionButton.addActionListener(e -> selectPlane(type, user));
        }

        row.add(label, BorderLayout.CENTER);
        row.add(actionButton, BorderLayout.EAST);

        return row;
    }

    private void selectPlane(PlaneType type, User user) {
        if (user == null) {
            JOptionPane.showMessageDialog(this, "login to your account first");
            return;
        }

        user.setSelectedPlane(type);
        app.getUserRepository().updateUser(user);
        JOptionPane.showMessageDialog(this, "Equipped " + type.name() + "!");
        refresh();
    }
}
