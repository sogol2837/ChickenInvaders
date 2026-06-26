package chickenInvaders.entity;

import chickenInvaders.AppConfig;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Plane extends GameObject {
    private int speed;
    private int lives;
    private int fireCount;
    private int fireDelay;
    private long lastShotTime;

    public Plane(int x, int y) {
        super(x, y, 44, 44);
        this.speed = 5;
        this.lives = 3;
        this.fireCount = 1;
        this.fireDelay = 300;
        this.lastShotTime = 0;
    }

    @Override
    public void update() {
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

        long now = System.currentTimeMillis();
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
        g.setColor(Color.CYAN);

        Polygon body = new Polygon();
        body.addPoint(x + width / 2, y);
        body.addPoint(x + width, y + height);
        body.addPoint(x + width / 2, y + height - 10);
        body.addPoint(x, y + height);

        g.fillPolygon(body);

        g.setColor(Color.WHITE);
        g.drawPolygon(body);
    }

    public int getLives() {
        return lives;
    }

    public int getFireCount() {
        return fireCount;
    }

    public void addFire() {
        fireCount++;
    }

    public void setRapidFire(boolean rapid) {
        if (rapid) {
            fireDelay = 120;
        } else {
            fireDelay = 300;
        }
    }
}
