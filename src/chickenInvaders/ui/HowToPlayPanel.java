package chickenInvaders.ui;

import chickenInvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {

    public HowToPlayPanel(GameMain app) {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 24, 35));

        JLabel titleLabel = new JLabel("How To Play", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setText(
            "Controls:\n\n" +
                "Move Right: D or Right Arrow\n" +
                "Move Left: A or Left Arrow\n" +
                "Move Up: W or Up Arrow\n" +
                "Move Down: S or Down Arrow\n" +
                "Shoot: Space\n" +
                "Pause: P\n" +
                "Back to Menu: ESC\n\n" +
                "Goal:\n" +
                "Destroy all chickens, survive all 8 levels, defeat the bosses and yayyy you won the game!!!"
        );

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
