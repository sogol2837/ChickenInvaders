package chickenInvaders.ui;

import chickenInvaders.GameMain;
import chickenInvaders.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final GameMain app;

    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginPanel(GameMain app) {
        this.app = app;

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel background = UiStyle.createBackgroundPanel();
        background.setLayout(new BorderLayout());
        background.setBorder(BorderFactory.createEmptyBorder(8, 40, 12, 40));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(1, 150));

        JLabel titleLabel = UiStyle.title("PILOT LOGIN");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(14, 20, 6, 20));

        JLabel subtitleLabel = UiStyle.subtitle("enter your account to start the mission");
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(14));
        topPanel.add(subtitleLabel);

        JPanel card = UiStyle.card();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(520, 340));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        UiStyle.styleTextField(usernameField);
        UiStyle.styleTextField(passwordField);

        usernameField.setPreferredSize(new Dimension(460, 38));
        passwordField.setPreferredSize(new Dimension(460, 38));

        JButton loginButton = UiStyle.primaryButton("LOGIN");
        JButton registerButton = UiStyle.secondaryButton("CREATE ACCOUNT");
        JButton backButton = UiStyle.secondaryButton("BACK");

        loginButton.setPreferredSize(new Dimension(460, 42));
        registerButton.setPreferredSize(new Dimension(460, 42));
        backButton.setPreferredSize(new Dimension(460, 42));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.insets = new Insets(2, 5, 8, 5);
        card.add(labeled("USERNAME", usernameField), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(4, 5, 10, 5);
        card.add(labeled("PASSWORD", passwordField), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 5, 6, 5);
        card.add(loginButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 5, 5);
        card.add(registerButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 2, 5);
        card.add(backButton, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(card);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> app.showRegister());
        backButton.addActionListener(e -> app.showMainMenu());

        background.add(topPanel, BorderLayout.NORTH);
        background.add(center, BorderLayout.CENTER);
    }

    private JPanel labeled(String text, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(UiStyle.fieldLabel(text), BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = app.getUserRepository().login(username, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "username or password is incorrect!");
            return;
        }

        app.setCurrentUser(user);
        JOptionPane.showMessageDialog(this, "logged in successfully.");
        app.showMainMenu();
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
