package chickenInvaders.ui;

import chickenInvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {

    public HowToPlayPanel(GameMain app) {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel background = UiStyle.createBackgroundPanel();
        background.setBorder(BorderFactory.createEmptyBorder(22, 40, 30, 40));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        topPanel.add(UiStyle.title("HOW TO PLAY"));
        topPanel.add(UiStyle.subtitle("mission controls and objective"));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(5, 8, 14));
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(UiStyle.CYAN);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setMargin(new Insets(18, 20, 18, 20));
        textArea.setText(
            "CONTROLS\n" +
                "────────────────────────────────────────\n" +
                "Move Right     : D or Right Arrow\n" +
                "Move Left      : A or Left Arrow\n" +
                "Move Up        : W or Up Arrow\n" +
                "Move Down      : S or Down Arrow\n" +
                "Shoot          : Space\n" +
                "Pause          : P\n" +
                "Back to Menu   : ESC\n\n" +
                "MISSION\n" +
                "────────────────────────────────────────\n" +
                "Destroy all the empire's ships, collect power-ups, survive all 8 levels, " +
                "defeat both bosses, and finish the galaxy mission." +
                "────────────────────────────────────────\n\n" +
                "don't forget to ACHIVE\n\n" +
                " BB8    to be your SHIELD!\n" +
                " R2D2   to FREEZE whole galaxy for you!\n" +
                " WALL-E to EXPAND your lasers!\n" +
                " BAYMAX to HEAL your wounds (physically and emotionally)!\n" +
                " BEEMO  to BOOST your lasers!\n"
        );

        JScrollPane scrollPane = UiStyle.scrollPane(textArea);

        JButton backButton = UiStyle.secondaryButton("BACK");
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        bottomPanel.add(backButton);

        JPanel center = UiStyle.card();
        center.setLayout(new BorderLayout());
        center.add(scrollPane, BorderLayout.CENTER);

        background.add(topPanel, BorderLayout.NORTH);
        background.add(center, BorderLayout.CENTER);
        background.add(bottomPanel, BorderLayout.SOUTH);
    }
}
