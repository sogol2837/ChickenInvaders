package chickenInvaders.ui;

import chickenInvaders.GameMain;
import chickenInvaders.model.User;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    private final GameMain app;
    private final JLabel userLabel;

    public MainMenuPanel(GameMain app) {
        this.app = app;

        setLayout(new BorderLayout());
        setBackground(new Color(20, 24, 35));

        JLabel titleLabel = new JLabel("Chicken Invaders", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));

        userLabel = new JLabel("", SwingConstants.CENTER);
        userLabel.setForeground(Color.LIGHT_GRAY);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        topPanel.add(titleLabel);
        topPanel.add(userLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 250, 40, 250));

        JButton newGameButton = new JButton("New Game");
        JButton highScoresButton = new JButton("High Scores");
        JButton settingsButton = new JButton("Settings");
        JButton howToPlayButton = new JButton("How To Play");
        JButton storeButton = new JButton("Store");
        JButton loginButton = new JButton("Login / Register");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(newGameButton);
        buttonPanel.add(highScoresButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(howToPlayButton);
        buttonPanel.add(storeButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        newGameButton.addActionListener(e -> app.startNewGame());
        highScoresButton.addActionListener(e -> app.showHighScores());
        settingsButton.addActionListener(e -> app.showSettings());
        howToPlayButton.addActionListener(e -> app.showHowToPlay());
        storeButton.addActionListener(e -> app.showStore());
        loginButton.addActionListener(e -> app.showLogin());
        exitButton.addActionListener(e -> app.exitGame());

        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        refreshUserLabel();
    }

    public void refreshUserLabel() {
        User user = app.getCurrentUser();

        if (user == null) {
            userLabel.setText("not logged in.");
        } else {
            userLabel.setText("logged in as: " + user.getUsername());
        }
    }
}
