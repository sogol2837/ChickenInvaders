package chickenInvaders.enemy;

import java.awt.Color;
import java.awt.Graphics;

public class FastEnemy extends Enemy {

    public FastEnemy(int x, int y, int level) {
        super(x, y, 38, 30, healthForLevel(level), 15);
    }

    private static int healthForLevel(int level) {
        return level >= 5 ? 2 : 1;
    }

    @Override
    public int getArrivalSpeed() {
        return 10; // twice the base homing speed while flying back into formation
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0xE5E4E2));
        g.fillOval(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);

        g.setColor(Color.WHITE);
        g.drawString("F", x + 14, y + 20);
    }
}
