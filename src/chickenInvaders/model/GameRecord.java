package chickenInvaders.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GameRecord {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String username;
    private final int score;
    private final int levelReached;
    private final LocalDateTime timestamp;
    private final SoundSettings soundSettings;

    public GameRecord(String username, int score, int levelReached, LocalDateTime timestamp, SoundSettings soundSettings) {
        this.username = username;
        this.score = score;
        this.levelReached = levelReached;
        this.timestamp = timestamp;
        this.soundSettings = soundSettings;
    }

    public static GameRecord newRecord(String username, int score, int levelReached, SoundSettings soundSettings) {
        return new GameRecord(username, score, levelReached, LocalDateTime.now(), soundSettings);
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getLevelReached() {
        return levelReached;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public SoundSettings getSoundSettings() {
        return soundSettings;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(FORMAT);
    }

    public String toFileLine() {
        return username + "|" + score + "|" + levelReached + "|" + timestamp.format(FORMAT) + "|" + soundSettings.toFileText();
    }

    public static GameRecord fromFileLine(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\|", -1);
        if (parts.length != 5) {
            return null;
        }

        try {
            String username = parts[0];
            int score = Integer.parseInt(parts[1]);
            int level = Integer.parseInt(parts[2]);
            LocalDateTime timestamp = LocalDateTime.parse(parts[3], FORMAT);
            SoundSettings settings = SoundSettings.fromFileText(parts[4]);
            return new GameRecord(username, score, level, timestamp, settings);
        } catch (Exception e) {
            return null;
        }
    }
}
