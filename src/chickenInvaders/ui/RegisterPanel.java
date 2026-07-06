package chickenInvaders.ui;

import chickenInvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    private final GameMain app;

    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public RegisterPanel(GameMain app) {
        this.app = app;

        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 45));

        JLabel titleLabel = new JLabel("Register", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(100, 250, 100, 250));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        formPanel.add(labeled("Username", usernameField));
        formPanel.add(labeled("Password", passwordField));
        formPanel.add(registerButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        registerButton.addActionListener(e -> register());
        backButton.addActionListener(e -> app.showLogin());

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

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        boolean result = app.getUserRepository().register(username, password);

        if (!result) {
            JOptionPane.showMessageDialog(this, "registration failed. Username is already taken or invalid!!!");
            return;
        }

        JOptionPane.showMessageDialog(this, "registration successful. please log in ^o^ ");
        app.showLogin();
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
