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
        background.setLayout(new BorderLayout());
        background.setBorder(BorderFactory.createEmptyBorder(8, 40, 12, 40));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(1, 150));

        JLabel titleLabel = UiStyle.title("CREATE PILOT");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(14, 20, 6, 20));

        JLabel subtitleLabel = UiStyle.subtitle("register a new commander profile");
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(14));
        topPanel.add(subtitleLabel);

        JPanel card = UiStyle.card();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(520, 300));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        UiStyle.styleTextField(usernameField);
        UiStyle.styleTextField(passwordField);

        usernameField.setPreferredSize(new Dimension(460, 38));
        passwordField.setPreferredSize(new Dimension(460, 38));

        JButton registerButton = UiStyle.primaryButton("REGISTER");
        JButton backButton = UiStyle.secondaryButton("BACK TO LOGIN");

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
        card.add(registerButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 2, 5);
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
