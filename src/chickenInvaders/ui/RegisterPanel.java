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
        setOpaque(false);

        JPanel background = UiStyle.createBackgroundPanel();
        background.setBorder(BorderFactory.createEmptyBorder(22, 40, 30, 40));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        topPanel.add(UiStyle.title("CREATE PILOT"));
        topPanel.add(UiStyle.subtitle("register a new commander profile"));

        JPanel card = UiStyle.card();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 270));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        UiStyle.styleTextField(usernameField);
        UiStyle.styleTextField(passwordField);

        JButton registerButton = UiStyle.primaryButton("REGISTER");
        JButton backButton = UiStyle.secondaryButton("BACK TO LOGIN");

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
        card.add(registerButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(6, 5, 6, 5);
        card.add(backButton, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(card);

        registerButton.addActionListener(e -> register());
        backButton.addActionListener(e -> app.showLogin());

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
