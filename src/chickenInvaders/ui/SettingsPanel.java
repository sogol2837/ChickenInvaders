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
        setOpaque(false);

        JPanel background = UiStyle.createBackgroundPanel();
        background.setBorder(BorderFactory.createEmptyBorder(22, 40, 30, 40));
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        topPanel.add(UiStyle.title("SOUND SETTINGS"));
        topPanel.add(UiStyle.subtitle("toggle each audio channel separately"));

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

        JPanel card = UiStyle.card();
        card.setLayout(new GridLayout(6, 1, 10, 10));
        card.setPreferredSize(new Dimension(440, 340));

        for (JCheckBox box : boxes) {
            box.setOpaque(false);
            box.setForeground(Color.WHITE);
            box.setFont(new Font("Monospaced", Font.BOLD, 16));
            box.setFocusPainted(false);
            box.setIconTextGap(12);
            card.add(box);
        }

        JButton saveButton = UiStyle.primaryButton("SAVE SETTINGS");
        JButton backButton = UiStyle.secondaryButton("BACK");

        card.add(saveButton);
        card.add(backButton);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(card);

        saveButton.addActionListener(e -> saveSettings());
        backButton.addActionListener(e -> app.showMainMenu());

        background.add(topPanel, BorderLayout.NORTH);
        background.add(center, BorderLayout.CENTER);
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
