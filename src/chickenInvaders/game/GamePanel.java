package chickenInvaders.game;

import chickenInvaders.AppConfig;
import chickenInvaders.GameMain;
import chickenInvaders.GameState;
import chickenInvaders.audio.SoundManager;
import chickenInvaders.boss.Boss;
import chickenInvaders.boss.BossLevel4;
import chickenInvaders.boss.BossLevel8;
import chickenInvaders.data.ScoreRepository;
import chickenInvaders.entity.Bullet;
import chickenInvaders.entity.Explosion;
import chickenInvaders.entity.Plane;
import chickenInvaders.entity.PowerUp;
import chickenInvaders.enemy.EnemyGrid;
import chickenInvaders.model.GameRecord;
import chickenInvaders.model.PowerUpType;
import chickenInvaders.model.SoundSettings;
import chickenInvaders.model.User;
import chickenInvaders.ui.UiStyle;
import chickenInvaders.util.ImageLoader;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final long FREEZE_DURATION_MS = 3000;

    private final GameMain app;
    private final User user;
    private final Timer timer;

    private int gameState;
    private final LevelManager levelManager;
    private final ScoreManager scoreManager;

    private final Plane plane;
    private EnemyGrid enemyGrid;
    private Boss boss;
    private final List<Bullet> bullets;
    private final CollisionManager collisionManager;

    private final List<Explosion> explosions = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();

    private final ScoreRepository scoreRepository = new ScoreRepository();
    private boolean resultSaved;

    private long freezeUntil;
    private boolean showSettingsOverlay;

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    private final Image backgroundImage = ImageLoader.load(AppConfig.IMAGE_DIR + "background.png");

    public GamePanel(GameMain app, User user) {
        this.app = app;
        this.user = user;

        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(AppConfig.FPS_DELAY, this);

        gameState = GameState.RUNNING;
        levelManager = new LevelManager();
        scoreManager = new ScoreManager();

        plane = new Plane(AppConfig.WINDOW_WIDTH / 2 - 22, AppConfig.WINDOW_HEIGHT - 110, user.getSelectedPlane());
        bullets = new ArrayList<>();
        collisionManager = new CollisionManager();

        startLevel();
    }

    public void startGame() {
        requestFocusInWindow();
        timer.start();
    }

    private void startLevel() {
        int level = levelManager.getCurrentLevel();

        if (levelManager.isBossLevel()) {
            boss = (level == 4) ? new BossLevel4() : new BossLevel8();
            enemyGrid = null;
        } else if (enemyGrid == null) {
            enemyGrid = new EnemyGrid(level);
            boss = null;
        } else {
            enemyGrid.nextLevel(level);
            boss = null;
        }
    }

    private void updateGame() {
        if (gameState != GameState.RUNNING || showSettingsOverlay) {
            return;
        }

        plane.move(leftPressed, rightPressed, upPressed, downPressed);
        plane.update();

        updateBullets();

        boolean frozen = System.currentTimeMillis() < freezeUntil;

        if (levelManager.isBossLevel()) {
            if (boss != null) {
                boss.update(frozen);
            }
        } else {
            enemyGrid.update(frozen, plane);

            if (enemyGrid.hasEnemyReachedBottom()) {
                endGame(GameState.GAME_OVER);
                return;
            }
        }

        collisionManager.update(
            plane,
            bullets,
            levelManager.isBossLevel() ? null : enemyGrid,
            levelManager.isBossLevel() ? boss : null,
            scoreManager,
            explosions,
            powerUps
        );

        updatePowerUps();

        for (Explosion explosion : explosions) {
            explosion.update();
        }
        explosions.removeIf(Explosion::isFinished);

        checkLevelProgress();

        if (!plane.isAlive()) {
            endGame(GameState.GAME_OVER);
        }
    }

    private void endGame(int finalState) {
        gameState = finalState;
        timer.stop();

        if (resultSaved) {
            return;
        }
        resultSaved = true;

        if (finalState == GameState.WIN) {
            SoundManager.getInstance().playWin();
        } else {
            SoundManager.getInstance().playGameOver();
        }

        GameRecord record = GameRecord.newRecord(
            user.getUsername(),
            scoreManager.getScore(),
            levelManager.getCurrentLevel(),
            user.getSoundSettings()
        );
        scoreRepository.saveRecord(record);

        if (scoreManager.getScore() > user.getHighScore()) {
            user.setHighScore(scoreManager.getScore());
            user.setLastLevel(levelManager.getCurrentLevel());
            app.getUserRepository().updateUser(user);
        }
    }

    private void updatePowerUps() {
        for (PowerUp powerUp : powerUps) {
            powerUp.update();

            if (powerUp.isActive() && powerUp.intersects(plane)) {
                if (powerUp.getType() == PowerUpType.FREEZE_BOMB) {
                    freezeUntil = System.currentTimeMillis() + FREEZE_DURATION_MS;
                } else {
                    plane.applyPowerUp(powerUp.getType());
                }
                powerUp.collect();
            }
        }
        powerUps.removeIf(p -> !p.isActive());
    }

    private void checkLevelProgress() {
        if (levelManager.isBossLevel()) {
            if (boss != null && boss.isDefeated()) {
                if (levelManager.getCurrentLevel() == 8) {
                    endGame(GameState.WIN);
                } else {
                    levelManager.nextLevel();
                    startLevel();
                }
            }
        } else if (enemyGrid.isCleared()) {
            scoreManager.addScore(200);
            levelManager.nextLevel();
            startLevel();
        }
    }

    private void updateBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();
            if (!bullet.isActive()) {
                iterator.remove();
            }
        }
    }

    private void shoot() {
        if (gameState != GameState.RUNNING) {
            return;
        }
        List<Bullet> newBullets = plane.shoot();
        if (!newBullets.isEmpty()) {
            bullets.addAll(newBullets);
            SoundManager.getInstance().playShot();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT, null);
        }

        if (levelManager.isBossLevel()) {
            if (boss != null) {
                boss.draw(g);
            }
        } else {
            enemyGrid.draw(g);
        }

        plane.draw(g);
        drawBullets(g);

        for (Explosion explosion : explosions) {
            explosion.draw(g);
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.draw(g);
        }

        drawHud(g);

        if (gameState == GameState.PAUSED) {
            drawPause(g);
        }
        if (gameState == GameState.GAME_OVER) {
            drawGameOver(g);
        }
        if (gameState == GameState.WIN) {
            drawWin(g);
        }
        if (showSettingsOverlay) {
            drawSettingsOverlay(g);
        }
    }

    private void drawHud(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawHudBox(g2, 14, 10, 520, 34);
        drawHudBox(g2, 540, 10, 246, 34);
        drawHudBox(g2, 14, AppConfig.WINDOW_HEIGHT - 38, 380, 24);

        g2.setFont(new Font("Monospaced", Font.BOLD, 15));
        g2.setColor(Color.WHITE);
        g2.drawString("USER: " + user.getUsername(), 25, 32);
        g2.drawString("LEVEL: " + levelManager.getCurrentLevel(), 155, 32);
        g2.drawString("SCORE: " + scoreManager.getScore(), 285, 32);
        g2.drawString("LIVES: " + plane.getLives(), 445, 32);

        g2.setColor(UiStyle.WARNING);
        g2.drawString("FIRE: " + plane.getFireCount(), 555, 32);

        int statusX = 650;
        if (plane.isShieldActive()) {
            g2.setColor(UiStyle.CYAN);
            g2.drawString("SHIELD " + plane.getShieldSecondsLeft() + "s", statusX, 32);
            statusX += 105;
        }
        if (plane.isRapidFireActive()) {
            g2.setColor(new Color(255, 180, 90));
            g2.drawString("RAPID " + plane.getRapidFireSecondsLeft() + "s", statusX, 32);
        }
        if (System.currentTimeMillis() < freezeUntil) {
            g2.setColor(new Color(170, 210, 255));
            g2.drawString("ENEMIES FROZEN", 620, 32);
        }

        g2.setColor(new Color(230, 235, 245));
        g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g2.drawString("P Pause   M Sound Settings   ESC Menu   SPACE Shoot", 24, AppConfig.WINDOW_HEIGHT - 21);
        g2.dispose();
    }

    private void drawHudBox(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(new Color(0, 0, 0, 145));
        g2.fillRoundRect(x, y, w, h, 14, 14);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(220, 225, 235, 215));
        g2.drawRoundRect(x, y, w, h, 14, 14);
        g2.setColor(new Color(0, 210, 245, 105));
        g2.drawRoundRect(x + 2, y + 2, w - 4, h - 4, 12, 12);
    }

    private void drawSettingsOverlay(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 205));
        g2.fillRect(0, 0, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);

        int cardW = 420;
        int cardH = 250;
        int cardX = (AppConfig.WINDOW_WIDTH - cardW) / 2;
        int cardY = 140;

        drawOverlayCard(g2, cardX, cardY, cardW, cardH, UiStyle.CYAN);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 28));
        drawCentered(g2, "SOUND SETTINGS", AppConfig.WINDOW_WIDTH / 2, cardY + 42);

        SoundSettings settings = user.getSoundSettings();
        g2.setFont(new Font("Monospaced", Font.PLAIN, 18));
        g2.drawString("1 - Background Music: " + onOff(settings.isBackgroundMusic()), cardX + 36, cardY + 88);
        g2.drawString("2 - Shot Sound:       " + onOff(settings.isShotSound()), cardX + 36, cardY + 118);
        g2.drawString("3 - Explosion Sound:  " + onOff(settings.isExplosionSound()), cardX + 36, cardY + 148);
        g2.drawString("4 - End Sound:        " + onOff(settings.isEndSound()), cardX + 36, cardY + 178);

        g2.setColor(UiStyle.MUTED);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        drawCentered(g2, "Press M to close", AppConfig.WINDOW_WIDTH / 2, cardY + 220);
        g2.dispose();
    }

    private String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }

    private void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    private void drawPause(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 175));
        g2.fillRect(0, 0, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);

        drawOverlayCard(g2, 210, 200, 380, 150, UiStyle.CYAN);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 42));
        drawCentered(g2, "PAUSED", AppConfig.WINDOW_WIDTH / 2, 255);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g2.setColor(UiStyle.MUTED);
        drawCentered(g2, "Press P to continue", AppConfig.WINDOW_WIDTH / 2, 305);
        g2.dispose();
    }

    private void drawGameOver(Graphics g) {
        drawResultOverlay((Graphics2D) g, false);
    }

    private void drawWin(Graphics g) {
        drawResultOverlay((Graphics2D) g, true);
    }

    private void drawResultOverlay(Graphics2D g, boolean win) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 210), 0, getHeight(), new Color(8, 12, 20, 235)));
        g2.fillRect(0, 0, getWidth(), getHeight());

        Color accent = win ? UiStyle.SUCCESS : UiStyle.DANGER;
        int cardW = 470;
        int cardH = 280;
        int cardX = (getWidth() - cardW) / 2;
        int cardY = 155;
        drawOverlayCard(g2, cardX, cardY, cardW, cardH, accent);

        g2.setColor(accent);
        g2.setFont(new Font("Monospaced", Font.BOLD, 40));
        drawCentered(g2, win ? "VICTORY" : "GAME OVER", getWidth() / 2, cardY + 52);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
        drawCentered(g2,
            win ? "The galaxy is safe. Great job, pilot." : "Your ship was destroyed. Try again.",
            getWidth() / 2,
            cardY + 82
        );

        drawStatRow(g2, cardX + 50, cardY + 120, cardW - 100, "PLAYER", user.getUsername());
        drawStatRow(g2, cardX + 50, cardY + 155, cardW - 100, "FINAL SCORE", String.valueOf(scoreManager.getScore()));
        drawStatRow(g2, cardX + 50, cardY + 190, cardW - 100, "LEVEL REACHED", String.valueOf(levelManager.getCurrentLevel()));
        drawStatRow(g2, cardX + 50, cardY + 225, cardW - 100, "BEST SCORE", String.valueOf(Math.max(user.getHighScore(), scoreManager.getScore())));

        g2.setColor(new Color(230, 235, 245));
        g2.setFont(new Font("Monospaced", Font.BOLD, 15));
        drawCentered(g2, "ENTER / R  PLAY AGAIN", getWidth() / 2, cardY + 260);
        g2.setColor(UiStyle.MUTED);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        drawCentered(g2, "ESC  MAIN MENU", getWidth() / 2, cardY + 282);

        g2.dispose();
    }

    private void drawOverlayCard(Graphics2D g2, int x, int y, int w, int h, Color accent) {
        g2.setColor(new Color(6, 10, 16, 235));
        g2.fillRoundRect(x, y, w, h, 24, 24);

        g2.setStroke(new BasicStroke(2.4f));
        g2.setColor(new Color(225, 230, 238, 220));
        g2.drawRoundRect(x, y, w, h, 24, 24);

        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 170));
        g2.drawRoundRect(x + 3, y + 3, w - 6, h - 6, 20, 20);

        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 35));
        g2.fillRoundRect(x + 10, y + 10, w - 20, 48, 16, 16);
    }

    private void drawStatRow(Graphics2D g2, int x, int y, int w, String label, String value) {
        g2.setColor(new Color(255, 255, 255, 22));
        g2.fillRoundRect(x, y - 18, w, 24, 10, 10);

        g2.setColor(UiStyle.SILVER);
        g2.setFont(new Font("Monospaced", Font.BOLD, 15));
        g2.drawString(label, x + 12, y);

        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.WHITE);
        g2.drawString(value, x + w - fm.stringWidth(value) - 12, y);
    }

    private void drawCentered(Graphics2D g2, String text, int centerX, int baselineY) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, centerX - fm.stringWidth(text) / 2, baselineY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
            if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_R) {
                app.startNewGame();
                return;
            }
            if (key == KeyEvent.VK_ESCAPE) {
                timer.stop();
                app.showMainMenu();
                return;
            }
        }

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) leftPressed = true;
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) rightPressed = true;
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) upPressed = true;
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) downPressed = true;

        if (key == KeyEvent.VK_SPACE) {
            shoot();
        }

        if (key == KeyEvent.VK_P) {
            if (gameState == GameState.RUNNING) {
                gameState = GameState.PAUSED;
            } else if (gameState == GameState.PAUSED) {
                gameState = GameState.RUNNING;
            }
        }

        if (key == KeyEvent.VK_ESCAPE) {
            timer.stop();
            app.showMainMenu();
        }

        if (key == KeyEvent.VK_M) {
            showSettingsOverlay = !showSettingsOverlay;
        } else if (showSettingsOverlay) {
            toggleSettingsFromKey(key);
        }
    }

    private void toggleSettingsFromKey(int key) {
        SoundSettings settings = user.getSoundSettings();
        boolean changed = true;

        switch (key) {
            case KeyEvent.VK_1 -> settings.setBackgroundMusic(!settings.isBackgroundMusic());
            case KeyEvent.VK_2 -> settings.setShotSound(!settings.isShotSound());
            case KeyEvent.VK_3 -> settings.setExplosionSound(!settings.isExplosionSound());
            case KeyEvent.VK_4 -> settings.setEndSound(!settings.isEndSound());
            default -> changed = false;
        }

        if (changed) {
            SoundManager.getInstance().applySettings(settings);
            app.getUserRepository().updateUser(user);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) leftPressed = false;
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) rightPressed = false;
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) upPressed = false;
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) downPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
