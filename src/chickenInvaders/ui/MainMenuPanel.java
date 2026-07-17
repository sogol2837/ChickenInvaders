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
        setOpaque(false);

        JPanel background = UiStyle.createBackgroundPanel();
        background.setLayout(new BorderLayout(0, 10));
        background.setBorder(BorderFactory.createEmptyBorder(10, 28, 16, 28));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(3, 1, 0, 2));
        topPanel.setOpaque(false);

        JLabel titleLabel = UiStyle.title("STARWARS INVADERS");
        JLabel subtitleLabel = UiStyle.subtitle("RETRO SPACE SHOOTER");
        userLabel = UiStyle.subtitle("");
        userLabel.setForeground(UiStyle.CYAN);
        userLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

        topPanel.add(titleLabel);
        topPanel.add(subtitleLabel);
        topPanel.add(userLabel);

        JPanel menuCard = UiStyle.card();
        menuCard.setLayout(new GridLayout(7, 1, 0, 7));

        JButton newGameButton = UiStyle.primaryButton("NEW GAME");
        JButton highScoresButton = UiStyle.secondaryButton("★ HIGH SCORES");
        JButton settingsButton = UiStyle.secondaryButton("SETTINGS");
        JButton howToPlayButton = UiStyle.secondaryButton("? HOW TO PLAY");
        JButton storeButton = UiStyle.secondaryButton("STORE");
        JButton loginButton = UiStyle.secondaryButton("LOGIN / REGISTER");
        JButton exitButton = UiStyle.secondaryButton("EXIT");

        menuCard.add(newGameButton);
        menuCard.add(highScoresButton);
        menuCard.add(settingsButton);
        menuCard.add(howToPlayButton);
        menuCard.add(storeButton);
        menuCard.add(loginButton);
        menuCard.add(exitButton);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        menuCard.setPreferredSize(new Dimension(330, 345));
        centerWrapper.add(menuCard, gbc);

        newGameButton.addActionListener(e -> app.startNewGame());
        highScoresButton.addActionListener(e -> app.showHighScores());
        settingsButton.addActionListener(e -> app.showSettings());
        howToPlayButton.addActionListener(e -> app.showHowToPlay());
        storeButton.addActionListener(e -> app.showStore());
        loginButton.addActionListener(e -> app.showLogin());
        exitButton.addActionListener(e -> app.exitGame());

        background.add(topPanel, BorderLayout.NORTH);
        background.add(centerWrapper, BorderLayout.CENTER);

        refreshUserLabel();
    }

    public void refreshUserLabel() {
        User user = app.getCurrentUser();

        if (user == null) {
            userLabel.setText("STATUS: NOT LOGGED IN");
        } else {
            userLabel.setText("PILOT: " + user.getUsername() + "  |  HIGH SCORE: " + user.getHighScore());
        }
    }
}
