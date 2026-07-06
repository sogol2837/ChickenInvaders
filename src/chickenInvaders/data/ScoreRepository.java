package chickenInvaders.data;

import chickenInvaders.AppConfig;
import chickenInvaders.model.GameRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScoreRepository {

    private final Path scoresPath;

    public ScoreRepository() {
        new DatabaseManager();
        scoresPath = Path.of(AppConfig.SCORES_FILE);
    }

    public void saveRecord(GameRecord record) {
        try {
            Files.writeString(
                scoresPath,
                record.toFileLine() + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.out.println("save record error: " + e.getMessage());
        }
    }

    public List<GameRecord> getAllRecords() {
        List<GameRecord> records = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(scoresPath);
            for (String line : lines) {
                GameRecord record = GameRecord.fromFileLine(line);
                if (record != null) {
                    records.add(record);
                }
            }
        } catch (IOException e) {
            System.out.println("read scores error: " + e.getMessage());
        }
        return records;
    }

    public List<GameRecord> getBestRecordPerUser() {
        Map<String, GameRecord> best = new HashMap<>();

        for (GameRecord record : getAllRecords()) {
            GameRecord current = best.get(record.getUsername());
            if (current == null || record.getScore() > current.getScore()) {
                best.put(record.getUsername(), record);
            }
        }

        List<GameRecord> result = new ArrayList<>(best.values());
        result.sort(Comparator.comparingInt(GameRecord::getScore).reversed());
        return result;
    }
}
