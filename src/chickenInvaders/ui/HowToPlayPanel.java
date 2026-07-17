package chickenInvaders.ui;

import chickenInvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {

    public HowToPlayPanel(GameMain app) {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel background = UiStyle.createBackgroundPanel();
        background.setLayout(new BorderLayout(0, 10));
        background.setBorder(BorderFactory.createEmptyBorder(8, 40, 14, 40));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(1, 130));

        JLabel titleLabel = UiStyle.title("HOW TO PLAY");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 6, 20));

        JLabel subtitleLabel = UiStyle.subtitle("mission controls and objective");
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(subtitleLabel);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);
        textArea.setBackground(new Color(5, 8, 14));
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(UiStyle.CYAN);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        textArea.setMargin(new Insets(16, 20, 16, 20));
        textArea.setText(
            "CONTROLS\n" +
                "────────────────────────────────────────\n" +
                "Move Right     : D or Right Arrow\n" +
                "Move Left      : A or Left Arrow\n" +
                "Move Up        : W or Up Arrow\n" +
                "Move Down      : S or Down Arrow\n" +
                "Shoot          : Space\n" +
                "Pause          : P\n" +
                "Back to Menu   : ESC\n" +
                "────────────────────────────────────────\n\n" +
                "MISSION\n" +
                "────────────────────────────────────────\n" +
                "Destroy all the empire's ships, collect power-ups, survive all 8 levels, \n" +
                "defeat both bosses, and finish the galaxy mission.\n" +
                "────────────────────────────────────────\n\n" +
                "don't forget to ACHIVE\n\n" +
                " BB8    to be your SHIELD!\n" +
                " R2D2   to FREEZE whole galaxy for you!\n" +
                " WALL-E to EXPAND your lasers!\n" +
                " BAYMAX to HEAL your wounds (physically and emotionally)!\n" +
                " BEEMO  to BOOST your lasers!\n"
        );

        JScrollPane scrollPane = UiStyle.scrollPane(textArea);

        JPanel centerCard = UiStyle.card();
        centerCard.setLayout(new BorderLayout());
        centerCard.setPreferredSize(new Dimension(600, 320));
        centerCard.add(scrollPane, BorderLayout.CENTER);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(centerCard);

        JButton backButton = UiStyle.secondaryButton("BACK");
        backButton.setPreferredSize(new Dimension(130, 44));
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(1, 56));
        bottomPanel.add(backButton);

        background.add(topPanel, BorderLayout.NORTH);
        background.add(center, BorderLayout.CENTER);
        background.add(bottomPanel, BorderLayout.SOUTH);
    }
}
