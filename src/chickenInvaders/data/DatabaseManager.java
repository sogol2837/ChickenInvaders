package chickenInvaders.data;

import chickenInvaders.AppConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatabaseManager {

    public DatabaseManager() {
        initialize();
    }

    private void initialize() {
        try {
            Path dataDir = Path.of(AppConfig.DATA_DIR);
            Path usersFile = Path.of(AppConfig.USERS_FILE);
            Path scoresFile = Path.of(AppConfig.SCORES_FILE);

            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }

            if (!Files.exists(usersFile)) {
                Files.createFile(usersFile);
            }

            if (!Files.exists(scoresFile)) {
                Files.createFile(scoresFile);
            }

        } catch (IOException e) {
            System.out.println("database initialization error: " + e.getMessage());
        }
    }
}
