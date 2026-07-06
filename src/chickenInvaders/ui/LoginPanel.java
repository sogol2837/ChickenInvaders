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
        setBackground(new Color(25, 30, 45));

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(80, 250, 80, 250));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Create Account");
        JButton backButton = new JButton("Back");

        formPanel.add(labeled("Username", usernameField));
        formPanel.add(labeled("Password", passwordField));
        formPanel.add(loginButton);
        formPanel.add(registerButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> app.showRegister());
        backButton.addActionListener(e -> app.showMainMenu());

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel labeled(String text, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);

        panel.add(label, BorderLayout.NORTH);
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
