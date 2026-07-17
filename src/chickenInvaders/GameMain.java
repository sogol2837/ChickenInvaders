package chickenInvaders;

import chickenInvaders.audio.SoundManager;
import chickenInvaders.data.UserRepository;
import chickenInvaders.game.GamePanel;
import chickenInvaders.model.SoundSettings;
import chickenInvaders.model.User;
import chickenInvaders.ui.*;

import javax.swing.*;
import java.awt.*;

public class GameMain extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel rootPanel;

    private final UserRepository userRepository;

    private User currentUser;

    private final MainMenuPanel mainMenuPanel;
    private final LoginPanel loginPanel;
    private final RegisterPanel registerPanel;
    private final HighScore highScore;
    private final SettingsPanel settingsPanel;
    private final HowToPlayPanel howToPlayPanel;
    private final StorePanel storePanel;

    public GameMain() {
        userRepository = new UserRepository();

        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);

        mainMenuPanel = new MainMenuPanel(this);
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        highScore = new HighScore(this);
        settingsPanel = new SettingsPanel(this);
        howToPlayPanel = new HowToPlayPanel(this);
        storePanel = new StorePanel(this);

        rootPanel.add(mainMenuPanel, "menu");
        rootPanel.add(loginPanel, "login");
        rootPanel.add(registerPanel, "register");
        rootPanel.add(highScore, "highScores");
        rootPanel.add(settingsPanel, "settings");
        rootPanel.add(howToPlayPanel, "howToPlay");
        rootPanel.add(storePanel, "store");

        setTitle("StarWars Invaders");
        setSize(AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);

        showMainMenu();

        SoundManager.getInstance().applySettings(getCurrentSoundSettings());
        SoundManager.getInstance().playMusic();

        setVisible(true);
    }

    public void showMainMenu() {
        mainMenuPanel.refreshUserLabel();
        SoundManager.getInstance().applySettings(getCurrentSoundSettings());
        SoundManager.getInstance().playMusic();
        cardLayout.show(rootPanel, "menu");
    }

    public void showLogin() {
        loginPanel.clearFields();
        cardLayout.show(rootPanel, "login");
    }

    public void showRegister() {
        registerPanel.clearFields();
        cardLayout.show(rootPanel, "register");
    }

    public void showHighScores() {
        highScore.refreshTable();
        cardLayout.show(rootPanel, "highScores");
    }

    public void showSettings() {
        settingsPanel.loadCurrentSettings();
        cardLayout.show(rootPanel, "settings");
    }

    public void showHowToPlay() {
        cardLayout.show(rootPanel, "howToPlay");
    }

    public void showStore() {
        storePanel.refresh();
        cardLayout.show(rootPanel, "store");
    }

    public void startNewGame() {
        if (currentUser == null) {
            showLogin();
            return;
        }

        GamePanel gamePanel = new GamePanel(this, currentUser);
        rootPanel.add(gamePanel, "game");
        cardLayout.show(rootPanel, "game");

        SoundManager.getInstance().applySettings(getCurrentSoundSettings());
        SoundManager.getInstance().playMusic();

        gamePanel.startGame();

        SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
    }

    public void exitGame() {
        System.exit(0);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        SoundManager.getInstance().applySettings(getCurrentSoundSettings());
    }

    public SoundSettings getCurrentSoundSettings() {
        if (currentUser == null) {
            return new SoundSettings();
        }
        return currentUser.getSoundSettings();
    }

    public void saveCurrentSoundSettings(SoundSettings settings) {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "login to your account first");
            return;
        }

        currentUser.setSoundSettings(settings);
        userRepository.updateUser(currentUser);
        SoundManager.getInstance().applySettings(settings);

        JOptionPane.showMessageDialog(this, "saved new settings!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMain::new);
    }
}
