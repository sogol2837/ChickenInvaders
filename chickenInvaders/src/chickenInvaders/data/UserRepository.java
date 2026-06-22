package chickenInvaders.data;

import chickenInvaders.AppConfig;
import chickenInvaders.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserRepository {

    private final Path usersPath;

    public UserRepository() {
        new DatabaseManager();
        usersPath = Path.of(AppConfig.USERS_FILE);
    }

    public boolean register(String username, String password) {
        if (!isValid(username) || !isValid(password)) {
            return false;
        }

        if (findByUsername(username) != null) {
            return false;
        }

        User user = new User(username, password);

        try {
            Files.writeString(
                usersPath,
                user.toFileLine() + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
            return true;
        } catch (IOException e) {
            System.out.println("register error: " + e.getMessage());
            return false;
        }
    }

    public User login(String username, String password) {
        User user = findByUsername(username);

        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }

    public User findByUsername(String username) {
        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(usersPath);

            for (String line : lines) {
                User user = User.fromFileLine(line);
                if (user != null) {
                    users.add(user);
                }
            }

        } catch (IOException e) {
            System.out.println("read users error: " + e.getMessage());
        }

        return users;
    }

    public List<User> getHighScoreUsers() {
        List<User> users = getAllUsers();

        users.sort(Comparator.comparingInt(User::getHighScore).reversed());

        return users;
    }

    public boolean updateUser(User updatedUser) {
        List<User> users = getAllUsers();
        List<String> newLines = new ArrayList<>();

        boolean found = false;

        for (User user : users) {
            if (user.getUsername().equals(updatedUser.getUsername())) {
                newLines.add(updatedUser.toFileLine());
                found = true;
            } else {
                newLines.add(user.toFileLine());
            }
        }

        if (!found) {
            return false;
        }

        try {
            Files.write(usersPath, newLines);
            return true;
        } catch (IOException e) {
            System.out.println("update user error: " + e.getMessage());
            return false;
        }
    }

    private boolean isValid(String value) {
        return value != null &&
            !value.trim().isEmpty() &&
            !value.contains("|");
    }
}
