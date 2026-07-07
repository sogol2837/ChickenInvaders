package chickenInvaders.entity;

import chickenInvaders.AppConfig;

import java.awt.Color;
import java.awt.Graphics;


public class Egg extends GameObject {

    private boolean active = true;
    private double vx;
    private double vy;
    private boolean zigzag;
    private double zigzagPhase;

    public Egg(int x, int y) {
        this(x, y, 0, 4);
    }

    private Egg(int x, int y, double vx, double vy) {
        super(x, y, 24, 27);
        this.vx = vx;
        this.vy = vy;
    }

    public static Egg withVelocity(int x, int y, double vx, double vy) {
        return new Egg(x, y, vx, vy);
    }

    public static Egg aimedAt(int fromX, int fromY, int targetX, int targetY, double speed) {
        double dx = targetX - fromX;
        double dy = targetY - fromY;
        double distance = Math.max(1.0, Math.hypot(dx, dy));
        return new Egg(fromX, fromY, dx / distance * speed, dy / distance * speed);
    }

    public void setZigzag(boolean zigzag) {
        this.zigzag = zigzag;
    }

    @Override
    public void update() {
        x += Math.round(vx);
        y += Math.round(vy);

        if (zigzag) {
            zigzagPhase += 0.2;
            x += Math.round(Math.sin(zigzagPhase) * 2);
        }

        if (y < -height || y > AppConfig.WINDOW_HEIGHT + height
            || x < -width || x > AppConfig.WINDOW_WIDTH + width) {
            active = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (drawSprite(g, "eggs.png")) {
            return;
        }

        g.setColor(Color.WHITE);
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void destroy() {
        active = false;
    }
}
