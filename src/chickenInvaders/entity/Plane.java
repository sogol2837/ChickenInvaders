package chickenInvaders.entity;

import chickenInvaders.AppConfig;
import chickenInvaders.model.PlaneType;
import chickenInvaders.model.PowerUpType;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Plane extends GameObject {

    private static final int MAX_LIVES = 5;
    private static final int MAX_FIRE_COUNT = 5;
    private static final int RAPID_FIRE_DELAY = 120;
    private static final long RAPID_FIRE_DURATION_MS = 8000;
    private static final long SHIELD_DURATION_MS = 10000;

    private final PlaneType planeType;
    private final int speed;
    private final int baseFireDelay;

    private int lives;
    private int fireCount;
    private int fireDelay;
    private long lastShotTime;

    private long shieldUntil;
    private long rapidFireUntil;

    private long timedPauseStartedAt;

    public Plane(int x, int y) {
        this(x, y, PlaneType.DEFAULT);
    }

    public Plane(int x, int y, PlaneType planeType) {
        super(x, y, 80, 100);
        this.planeType = planeType;
        this.speed = planeType.getSpeed();
        this.lives = planeType.getStartingLives();
        this.fireCount = 1;
        this.baseFireDelay = planeType.getFireDelayMs();
        this.fireDelay = baseFireDelay;
        this.lastShotTime = 0;
    }

    @Override
    public void update() {
        long now = currentTime();

        if (rapidFireUntil != 0 && now >= rapidFireUntil) {
            rapidFireUntil = 0;
            fireDelay = baseFireDelay;
        }
    }

    public void move(boolean left, boolean right, boolean up, boolean down) {
        if (left) {
            x -= speed;
        }
        if (right) {
            x += speed;
        }
        if (up) {
            y -= speed;
        }
        if (down) {
            y += speed;
        }

        keepInsideScreen();
    }

    private void keepInsideScreen() {
        if (x < 0) {
            x = 0;
        }
        if (x > AppConfig.WINDOW_WIDTH - width) {
            x = AppConfig.WINDOW_WIDTH - width;
        }
        if (y < 40) {
            y = 40;
        }
        if (y > AppConfig.WINDOW_HEIGHT - height - 40) {
            y = AppConfig.WINDOW_HEIGHT - height - 40;
        }
    }

    public List<Bullet> shoot() {
        List<Bullet> newBullets = new ArrayList<>();

        long now = currentTime();
        if (now - lastShotTime < fireDelay) {
            return newBullets;
        }
        lastShotTime = now;

        int centerX = x + width / 2;
        int startX = centerX - ((fireCount - 1) * 12) / 2;

        for (int i = 0; i < fireCount; i++) {
            int bulletX = startX + i * 12 - 3;
            int bulletY = y - 10;
            newBullets.add(new Bullet(bulletX, bulletY));
        }

        return newBullets;
    }

    @Override
    public void draw(Graphics g) {
        if (isShieldActive()) {
            g.setColor(new Color(0, 200, 255, 90));
            g.fillOval(x - 10, y - 10, width + 20, height + 20);
            g.setColor(Color.CYAN);
            g.drawOval(x - 10, y - 10, width + 20, height + 20);
        }

        if (drawSprite(g, planeImageName())) {
            return;
        }

        g.setColor(bodyColor());

        Polygon body = new Polygon();
        body.addPoint(x + width / 2, y);
        body.addPoint(x + width, y + height);
        body.addPoint(x + width / 2, y + height - 10);
        body.addPoint(x, y + height);

        g.fillPolygon(body);

        g.setColor(Color.WHITE);
        g.drawPolygon(body);
    }

    private Color bodyColor() {
        return switch (planeType) {
            case DEFAULT -> Color.CYAN;
            case FAST -> Color.YELLOW;
            case HEAVY -> Color.ORANGE;
            case SNIPER -> Color.RED;
        };
    }

    public PlaneType getPlaneType() {
        return planeType;
    }

    public boolean hasDoubleBossDamage() {
        return planeType.hasDoubleBossDamage();
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public int getFireCount() {
        return fireCount;
    }

    public void applyPowerUp(PowerUpType type) {
        long now = currentTime();

        switch (type) {
            case ADD_FIRE -> fireCount = Math.min(MAX_FIRE_COUNT, fireCount + 1);
            case RAPID_FIRE -> {
                rapidFireUntil = now + RAPID_FIRE_DURATION_MS;
                fireDelay = RAPID_FIRE_DELAY;
            }
            case EXTRA_LIFE -> lives = Math.min(MAX_LIVES, lives + 1);
            case SHIELD -> shieldUntil = now + SHIELD_DURATION_MS;
            case FREEZE_BOMB -> {
                // Freezing enemies/eggs is tracked by GamePanel, not the plane.
            }
        }
    }

    public boolean isShieldActive() {
        return currentTime() < shieldUntil;
    }

    public boolean isRapidFireActive() {
        return currentTime() < rapidFireUntil;
    }

    public long getShieldSecondsLeft() {
        return Math.max(0, (shieldUntil - currentTime()) / 1000 + 1);
    }

    public long getRapidFireSecondsLeft() {
        return Math.max(0, (rapidFireUntil - currentTime()) / 1000 + 1);
    }

    private String planeImageName() {
        return switch (planeType) {
            case DEFAULT -> "mainPlane.png";
            case FAST -> "plane2.png";
            case HEAVY -> "plane3.png";
            case SNIPER -> "plane4.png";
        };
    }


    private long currentTime() {

        if (timedPauseStartedAt != 0) {
            return timedPauseStartedAt;
        }

        return System.currentTimeMillis();
    }

    public void pauseTimedEffects(long pauseTime) {
        if (timedPauseStartedAt == 0) {
            timedPauseStartedAt = pauseTime;
        }
    }

    public void resumeTimedEffects(long resumeTime) {
        if (timedPauseStartedAt == 0) {
            return;
        }
        long pausedDuration = resumeTime - timedPauseStartedAt;

        if (shieldUntil > timedPauseStartedAt) {
            shieldUntil += pausedDuration;
        }
        if (rapidFireUntil > timedPauseStartedAt) {
            rapidFireUntil += pausedDuration;
        }
        if (lastShotTime > 0) {
            lastShotTime += pausedDuration;
        }

        timedPauseStartedAt = 0;
    }

    public void clearTemporaryPowerUps() {
        shieldUntil = 0;
        rapidFireUntil = 0;

        fireDelay = baseFireDelay;
        lastShotTime = 0;

        timedPauseStartedAt = 0;
    }
}
