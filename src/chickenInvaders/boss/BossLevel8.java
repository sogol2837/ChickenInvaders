package chickenInvaders.boss;

import chickenInvaders.AppConfig;
import chickenInvaders.entity.Egg;
import chickenInvaders.util.RandomUtils;

import java.awt.Color;
import java.awt.Graphics;

public class BossLevel8 extends Boss {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final double MAX_H_SPEED = 2.5;
    private static final double V_RANGE = 70;
    private static final double EGG_SPEED = 5;
    private static final int DIRECTIONS = 8;

    private double hVelocity = 1.3;
    private int hDirection = -1;
    private double vPhase;
    private final int baseY;
    private int directionChangeCooldown;

    public BossLevel8() {
        super(AppConfig.WINDOW_WIDTH - WIDTH - 40, 90, WIDTH, HEIGHT, 100, 1000, 1000);
        baseY = y;
    }

    @Override
    protected void moveBoss() {
        directionChangeCooldown++;
        if (directionChangeCooldown > 180 && RandomUtils.chance(0.02)) {
            hDirection *= -1;
            hVelocity = 1.0;
            directionChangeCooldown = 0;
        }

        hVelocity = Math.min(MAX_H_SPEED, hVelocity + 0.01);

        int nextX = x + (int) Math.round(hDirection * hVelocity);
        if (nextX <= 0 || nextX + width >= AppConfig.WINDOW_WIDTH) {
            hDirection *= -1;
            hVelocity = 1.0;
        } else {
            x = nextX;
        }

        vPhase += 0.02;
        y = (int) Math.round(baseY + Math.sin(vPhase) * V_RANGE);
    }

    @Override
    protected void fireAttack() {
        int cx = x + width / 2;
        int cy = y + height / 2;

        for (int i = 0; i < DIRECTIONS; i++) {
            double angle = Math.toRadians(360.0 / DIRECTIONS * i);
            double vx = Math.cos(angle) * EGG_SPEED;
            double vy = Math.sin(angle) * EGG_SPEED;
            eggs.add(Egg.withVelocity(cx, cy, vx, vy));
        }
    }

    @Override
    protected void drawBody(Graphics g) {

        if (drawSprite(g, "boss8.png")) {
            return;
        }

        g.setColor(new Color(0x4B0082));
        g.fillOval(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);

        g.setColor(Color.RED);
        g.fillOval(x + width / 4, y + height / 3, 22, 22);
        g.fillOval(x + width * 3 / 4 - 22, y + height / 3, 22, 22);

        g.setColor(Color.BLACK);
        g.fillOval(x + width / 4 + 7, y + height / 3 + 7, 8, 8);
        g.fillOval(x + width * 3 / 4 - 15, y + height / 3 + 7, 8, 8);
    }
}
