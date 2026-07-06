package chickenInvaders.ui;

import chickenInvaders.GameMain;
import chickenInvaders.model.SoundSettings;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private final GameMain app;

    private final JCheckBox backgroundMusicBox;
    private final JCheckBox shotSoundBox;
    private final JCheckBox explosionSoundBox;
    private final JCheckBox endSoundBox;

    public SettingsPanel(GameMain app) {
        this.app = app;

        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 45));

        JLabel titleLabel = new JLabel("Sound Settings", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        backgroundMusicBox = new JCheckBox("Background Music");
        shotSoundBox = new JCheckBox("Shot Sound");
        explosionSoundBox = new JCheckBox("Explosion / Crash Sound");
        endSoundBox = new JCheckBox("Game Over / Win Sound");

        JCheckBox[] boxes = {
            backgroundMusicBox,
            shotSoundBox,
            explosionSoundBox,
            endSoundBox
        };

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 250, 100, 250));

        for (JCheckBox box : boxes) {
            box.setOpaque(false);
            box.setForeground(Color.WHITE);
            box.setFont(new Font("Arial", Font.PLAIN, 16));
            centerPanel.add(box);
        }

        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        saveButton.addActionListener(e -> saveSettings());
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(saveButton);
        bottomPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadCurrentSettings() {
        SoundSettings settings = app.getCurrentSoundSettings();

        backgroundMusicBox.setSelected(settings.isBackgroundMusic());
        shotSoundBox.setSelected(settings.isShotSound());
        explosionSoundBox.setSelected(settings.isExplosionSound());
        endSoundBox.setSelected(settings.isEndSound());
    }

    private void saveSettings() {
        SoundSettings settings = new SoundSettings(
            backgroundMusicBox.isSelected(),
            shotSoundBox.isSelected(),
            explosionSoundBox.isSelected(),
            endSoundBox.isSelected()
        );

        app.saveCurrentSoundSettings(settings);
    }
}
