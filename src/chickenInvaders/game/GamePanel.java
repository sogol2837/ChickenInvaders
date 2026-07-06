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

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        g.drawString("User: " + user.getUsername(), 20, 20);
        g.drawString("Level: " + levelManager.getCurrentLevel(), 160, 20);
        g.drawString("Score: " + scoreManager.getScore(), 250, 20);
        g.drawString("Lives: " + plane.getLives(), 360, 20);
        g.drawString("Fire: " + plane.getFireCount(), 450, 20);

        int statusX = 540;
        if (plane.isShieldActive()) {
            g.setColor(Color.CYAN);
            g.drawString("Shield " + plane.getShieldSecondsLeft() + "s", statusX, 20);
            statusX += 90;
        }
        if (plane.isRapidFireActive()) {
            g.setColor(Color.ORANGE);
            g.drawString("Rapid " + plane.getRapidFireSecondsLeft() + "s", statusX, 20);
        }
        if (System.currentTimeMillis() < freezeUntil) {
            g.setColor(Color.BLUE);
            g.drawString("Frozen!", 20, 40);
        }

        g.setColor(Color.WHITE);
        g.drawString("P: Pause | ESC: Menu | SPACE: Shoot | M: Sound Settings", 20, AppConfig.WINDOW_HEIGHT - 15);
    }

    private void drawSettingsOverlay(Graphics g) {
        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("Sound Settings", 300, 180);

        SoundSettings settings = user.getSoundSettings();
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("1 - Background Music: " + onOff(settings.isBackgroundMusic()), 260, 230);
        g.drawString("2 - Shot Sound: " + onOff(settings.isShotSound()), 260, 260);
        g.drawString("3 - Explosion / Crash Sound: " + onOff(settings.isExplosionSound()), 260, 290);
        g.drawString("4 - Game Over / Win Sound: " + onOff(settings.isEndSound()), 260, 320);

        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Press M to close", 320, 370);
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
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 42));
        g.drawString("PAUSED", 310, 300);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 46));
        g.drawString("GAME OVER", 250, 280);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + scoreManager.getScore() + "   Press ESC to return to menu", 190, 320);
    }

    private void drawWin(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 46));
        g.drawString("YOU WIN!", 290, 280);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + scoreManager.getScore() + "   Press ESC to return to menu", 190, 320);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

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
