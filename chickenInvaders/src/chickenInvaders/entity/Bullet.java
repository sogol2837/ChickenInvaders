package chickenInvaders.entity;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends GameObject {
    private final int speed;
    private boolean active;

    public Bullet(int x, int y) {
        super(x, y, 6, 14);
        this.speed = 9;
        this.active = true;
    }

    @Override
    public void update() {
        y -= speed;
        if (y + height < 0) {
            active = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }
}
