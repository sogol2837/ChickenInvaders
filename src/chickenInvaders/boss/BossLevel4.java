package chickenInvaders.boss;

import chickenInvaders.AppConfig;
import chickenInvaders.entity.Egg;

import java.awt.Color;
import java.awt.Graphics;

public class BossLevel4 extends Boss {

    private static final int WIDTH = 250;
    private static final int HEIGHT = 200;
    private static final double H_SPEED = 2.2;
    private static final double V_RANGE = 55;
    private static final int EGG_SPEED = 4;

    private int hDirection = -1;
    private double vPhase;
    private final int baseY;

    public BossLevel4() {
        super(AppConfig.WINDOW_WIDTH - WIDTH - 40, 120, WIDTH, HEIGHT, 50, 1500, 500);
        baseY = y;
    }

    @Override
    protected void moveBoss() {
        int nextX = x + (int) Math.round(hDirection * H_SPEED);
        if (nextX <= 10 || nextX + width >= AppConfig.WINDOW_WIDTH - 10) {
            hDirection *= -1;
        }
        else {
            x = nextX;
        }
        vPhase += 0.04;
        y = (int) Math.round(baseY + Math.sin(vPhase) * V_RANGE);
    }

    @Override
    protected void fireAttack() {
        int cx = x + width / 2;
        int cy = y + height / 2;

        eggs.add(Egg.withVelocity(cx, cy, 0, -EGG_SPEED));
        eggs.add(Egg.withVelocity(cx, cy, 0, EGG_SPEED));
        eggs.add(Egg.withVelocity(cx, cy, -EGG_SPEED, 0));
        eggs.add(Egg.withVelocity(cx, cy, EGG_SPEED, 0));
    }

    @Override
    protected void drawBody(Graphics g) {

        if (drawSprite(g, "boss4.png")) {
            return;
        }

        g.setColor(new Color(0x8B4513));
        g.fillOval(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);

        g.setColor(Color.YELLOW);
        g.fillOval(x + width / 4, y + height / 3, 18, 18);
        g.fillOval(x + width * 3 / 4 - 18, y + height / 3, 18, 18);

        g.setColor(Color.BLACK);
        g.fillOval(x + width / 4 + 6, y + height / 3 + 6, 6, 6);
        g.fillOval(x + width * 3 / 4 - 12, y + height / 3 + 6, 6, 6);
    }
}
