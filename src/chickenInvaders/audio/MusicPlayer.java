package chickenInvaders.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;


public class MusicPlayer {

    private Clip clip;
    private boolean available;
    private boolean enabled = true;

    public MusicPlayer(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(stream);
                available = true;
            }
        } catch (Exception e) {
            available = false;
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            play();
        } else {
            stop();
        }
    }

    public void play() {
        if (!available || !enabled || clip == null || clip.isRunning()) {
            return;
        }
        try {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            available = false;
        }
    }

    public void stop() {
        if (!available || clip == null || !clip.isRunning()) {
            return;
        }
        try {
            clip.stop();
        } catch (Exception e) {
            available = false;
        }
    }
}
