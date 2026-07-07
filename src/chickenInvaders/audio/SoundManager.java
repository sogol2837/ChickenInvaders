package chickenInvaders.audio;

import chickenInvaders.AppConfig;
import chickenInvaders.model.SoundSettings;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;


public final class SoundManager {

    private static SoundManager instance;

    private final Map<SoundType, Clip> clips = new EnumMap<>(SoundType.class);
    private final MusicPlayer musicPlayer;

    private SoundSettings settings = new SoundSettings();

    private SoundManager() {
        clips.put(SoundType.SHOT, loadClip("shoot.wav"));
        clips.put(SoundType.EXPLOSION, loadClip("explosion.wav"));
        clips.put(SoundType.GAME_OVER, loadClip("gameover.wav"));
        clips.put(SoundType.WIN, loadClip("win.wav"));
        musicPlayer = new MusicPlayer(AppConfig.SOUND_DIR + "bgm.wav");
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private Clip loadClip(String fileName) {
        try {
            File file = new File(AppConfig.SOUND_DIR + fileName);
            if (!file.exists()) {
                return null;
            }
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            return clip;
        } catch (Exception e) {
            // Covers UnsupportedAudioFileException/IOException/LineUnavailableException,
            // plus AudioSystem.getClip() can also throw an unchecked
            // IllegalArgumentException when no matching audio line/hardware
            // is available at all (headless machines, CI, some sandboxes).
            // A missing sound must never crash the game, so every audio
            // failure here is swallowed the same way.
            return null;
        }
    }

    public void applySettings(SoundSettings settings) {
        this.settings = settings;
        musicPlayer.setEnabled(settings.isBackgroundMusic());
    }

    public void playMusic() {
        if (settings.isBackgroundMusic()) {
            musicPlayer.play();
        }
    }

    public void stopMusic() {
        musicPlayer.stop();
    }

    public void playShot() {
        play(SoundType.SHOT, settings.isShotSound());
    }

    public void playExplosion() {
        play(SoundType.EXPLOSION, settings.isExplosionSound());
    }

    public void playGameOver() {
        play(SoundType.GAME_OVER, settings.isEndSound());
    }

    public void playWin() {
        play(SoundType.WIN, settings.isEndSound());
    }

    private void play(SoundType type, boolean enabled) {
        if (!enabled) {
            return;
        }
        Clip clip = clips.get(type);
        if (clip == null) {
            return;
        }
        try {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            clips.put(type, null);
        }
    }
}
