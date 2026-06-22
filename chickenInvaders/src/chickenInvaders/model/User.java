package chickenInvaders.model;

public class User {

    private String username;
    private String password;
    private int highScore;
    private int lastLevel;
    private SoundSettings soundSettings;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.highScore = 0;
        this.lastLevel = 1;
        this.soundSettings = new SoundSettings();
    }

    public User(String username, String password, int highScore, int lastLevel, SoundSettings soundSettings) {
        this.username = username;
        this.password = password;
        this.highScore = highScore;
        this.lastLevel = lastLevel;
        this.soundSettings = soundSettings;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public SoundSettings getSoundSettings() {
        return soundSettings;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }

    public void setSoundSettings(SoundSettings soundSettings) {
        this.soundSettings = soundSettings;
    }

    public String toFileLine() {
        return username + "|" +
            password + "|" +
            highScore + "|" +
            lastLevel + "|" +
            soundSettings.toFileText();
    }

    public static User fromFileLine(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\|", -1);

        if (parts.length != 5) {
            return null;
        }

        String username = parts[0];
        String password = parts[1];

        int highScore;
        int lastLevel;

        try {
            highScore = Integer.parseInt(parts[2]);
            lastLevel = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            highScore = 0;
            lastLevel = 1;
        }

        SoundSettings settings = SoundSettings.fromFileText(parts[4]);

        return new User(username, password, highScore, lastLevel, settings);
    }
}
