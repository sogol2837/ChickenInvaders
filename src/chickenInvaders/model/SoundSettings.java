package chickenInvaders.model;

public class SoundSettings {

    private boolean backgroundMusic;
    private boolean shotSound;
    private boolean explosionSound;
    private boolean endSound;

    public SoundSettings() {
        this.backgroundMusic = true;
        this.shotSound = true;
        this.explosionSound = true;
        this.endSound = true;
    }

    public SoundSettings(boolean backgroundMusic, boolean shotSound, boolean explosionSound, boolean endSound) {
        this.backgroundMusic = backgroundMusic;
        this.shotSound = shotSound;
        this.explosionSound = explosionSound;
        this.endSound = endSound;
    }

    public boolean isBackgroundMusic() {
        return backgroundMusic;
    }

    public void setBackgroundMusic(boolean backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }

    public boolean isShotSound() {
        return shotSound;
    }

    public void setShotSound(boolean shotSound) {
        this.shotSound = shotSound;
    }

    public boolean isExplosionSound() {
        return explosionSound;
    }

    public void setExplosionSound(boolean explosionSound) {
        this.explosionSound = explosionSound;
    }

    public boolean isEndSound() {
        return endSound;
    }

    public void setEndSound(boolean endSound) {
        this.endSound = endSound;
    }

    public String toFileText() {
        return boolToText(backgroundMusic) + "," +
            boolToText(shotSound) + "," +
            boolToText(explosionSound) + "," +
            boolToText(endSound);
    }

    public static SoundSettings fromFileText(String text) {
        if (text == null || text.isEmpty()) {
            return new SoundSettings();
        }

        String[] parts = text.split(",");

        if (parts.length != 4) {
            return new SoundSettings();
        }

        return new SoundSettings(
            textToBool(parts[0]),
            textToBool(parts[1]),
            textToBool(parts[2]),
            textToBool(parts[3])
        );
    }

    private static String boolToText(boolean value) {
        return value ? "1" : "0";
    }

    private static boolean textToBool(String value) {
        return "1".equals(value);
    }
}
