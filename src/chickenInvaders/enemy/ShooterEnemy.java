package chickenInvaders.enemy;

import java.awt.Color;
import java.awt.Graphics;

public class ShooterEnemy extends Enemy {

    public ShooterEnemy(int x, int y, int level) {
        super(x, y, 44, 36, healthForLevel(level), 25);
    }

    private static int healthForLevel(int level) {
        return level >= 5 ? 2 : 3;
    }

    @Override
    public boolean canFireAtPlane() {
        return true;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0xE3F988));
        g.fillOval(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);

        g.setColor(Color.WHITE);
        g.drawString("S", x + 16, y + 22);
    }
}
