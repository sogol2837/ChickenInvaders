package chickenInvaders.ui;

import chickenInvaders.AppConfig;
import chickenInvaders.GameMain;
import chickenInvaders.model.PlaneType;
import chickenInvaders.model.User;
import chickenInvaders.util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class StorePanel extends JPanel {

    private final GameMain app;
    private final JPanel listPanel;
    private final JLabel creditLabel;

    public StorePanel(GameMain app) {
        this.app = app;

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel background = UiStyle.createBackgroundPanel();
        background.setLayout(new BorderLayout(0, 10));
        background.setBorder(BorderFactory.createEmptyBorder(8, 34, 14, 34));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.setOpaque(false);
        topPanel.add(UiStyle.title("SPACE STORE"));
        topPanel.add(UiStyle.subtitle("use your high score as credits to equip a ship"));

        creditLabel = UiStyle.subtitle("");
        creditLabel.setForeground(UiStyle.WARNING);
        creditLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        topPanel.add(creditLabel);

        listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(PlaneType.values().length, 1, 12, 12));
        listPanel.setOpaque(false);
        listPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scrollPane = UiStyle.scrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        JPanel listCard = UiStyle.card();
        listCard.setLayout(new BorderLayout());
        listCard.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = UiStyle.secondaryButton("BACK");
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        bottomPanel.add(backButton);

        background.add(topPanel, BorderLayout.NORTH);
        background.add(listCard, BorderLayout.CENTER);
        background.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        listPanel.removeAll();

        User user = app.getCurrentUser();
        int highScore = (user == null) ? 0 : user.getHighScore();
        PlaneType current = (user == null) ? PlaneType.DEFAULT : user.getSelectedPlane();

        if (user == null) {
            creditLabel.setText("LOGIN REQUIRED  |  CREDITS: 0");
        } else {
            creditLabel.setText("PILOT: " + user.getUsername() + "  |  CREDITS: " + highScore + "  |  EQUIPPED: " + current.name());
        }

        for (PlaneType type : PlaneType.values()) {
            listPanel.add(buildRow(type, highScore, current, user));
        }

        revalidate();
        repaint();
    }

    private JPanel buildRow(PlaneType type, int highScore, PlaneType current, User user) {
        JPanel row = UiStyle.card();
        row.setLayout(new BorderLayout(16, 0));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(type == current ? UiStyle.CYAN : new Color(150, 160, 170), type == current ? 3 : 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel imageLabel = buildPlaneImage(type);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(type.name());
        nameLabel.setForeground(type == current ? UiStyle.CYAN : Color.WHITE);
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 19));

        String special = type.hasDoubleBossDamage() ? "  |  SPECIAL: 2x boss damage" : "";
        JLabel statsLabel = new JLabel(
            "Cost " + type.getCost() +
                "  |  Speed " + type.getSpeed() +
                "  |  Fire " + type.getFireDelayMs() + "ms" +
                "  |  Lives " + type.getStartingLives()
        );
        statsLabel.setForeground(UiStyle.SILVER);
        statsLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JLabel specialLabel = new JLabel(special.isEmpty() ? "Balanced ship configuration" : special.trim());
        specialLabel.setForeground(type.hasDoubleBossDamage() ? UiStyle.WARNING : UiStyle.MUTED);
        specialLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));

        infoPanel.add(nameLabel);
        infoPanel.add(statsLabel);
        infoPanel.add(specialLabel);

        JButton actionButton;
        boolean isCurrent = type == current;
        boolean canAfford = highScore >= type.getCost();

        if (isCurrent) {
            actionButton = UiStyle.secondaryButton("EQUIPPED");
            actionButton.setEnabled(false);
        } else if (user == null) {
            actionButton = UiStyle.secondaryButton("LOGIN");
            actionButton.addActionListener(e -> app.showLogin());
        } else if (!canAfford) {
            actionButton = UiStyle.secondaryButton("LOCKED");
            actionButton.setEnabled(false);
        } else {
            actionButton = UiStyle.primaryButton("SELECT");
            actionButton.addActionListener(e -> selectPlane(type, user));
        }

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        actionButton.setPreferredSize(new Dimension(120, 42));
        buttonPanel.add(actionButton);

        row.add(imageLabel, BorderLayout.WEST);
        row.add(infoPanel, BorderLayout.CENTER);
        row.add(buttonPanel, BorderLayout.EAST);

        return row;
    }

    private JLabel buildPlaneImage(PlaneType type) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(110, 80));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(80, 95, 105), 1));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(3, 5, 10));

        Image image = ImageLoader.load(AppConfig.IMAGE_DIR + imageNameFor(type));

        if (image != null) {
            Image scaledImage = image.getScaledInstance(86, 68, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            imageLabel.setForeground(Color.WHITE);
            imageLabel.setFont(new Font("Monospaced", Font.BOLD, 11));
            imageLabel.setText(type.name());
        }

        return imageLabel;
    }

    private String imageNameFor(PlaneType type) {
        return switch (type) {
            case DEFAULT -> "mainPlane.png";
            case FAST -> "plane2.png";
            case HEAVY -> "plane3.png";
            case SNIPER -> "plane4.png";
        };
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
