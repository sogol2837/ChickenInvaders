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
        background.setBorder(BorderFactory.createEmptyBorder(22, 40, 30, 40));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        topPanel.add(UiStyle.title("PILOT LOGIN"));
        topPanel.add(UiStyle.subtitle("enter your account to start the mission"));

        JPanel card = UiStyle.card();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 310));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        UiStyle.styleTextField(usernameField);
        UiStyle.styleTextField(passwordField);

        JButton loginButton = UiStyle.primaryButton("LOGIN");
        JButton registerButton = UiStyle.secondaryButton("CREATE ACCOUNT");
        JButton backButton = UiStyle.secondaryButton("BACK");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 5, 8, 5);
        card.add(labeled("USERNAME", usernameField), gbc);

        gbc.gridy++;
        card.add(labeled("PASSWORD", passwordField), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(16, 5, 6, 5);
        card.add(loginButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(6, 5, 6, 5);
        card.add(registerButton, gbc);

        gbc.gridy++;
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
