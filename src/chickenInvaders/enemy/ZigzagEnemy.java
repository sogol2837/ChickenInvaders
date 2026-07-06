package chickenInvaders.enemy;

import java.awt.Color;
import java.awt.Graphics;

public class ZigzagEnemy extends Enemy {

    public ZigzagEnemy(int x, int y, int level) {
        super(x, y, 42, 34, healthForLevel(level), 20);
    }

    private static int healthForLevel(int level) {
        return level >= 5 ? 3 : 2;
    }

    @Override
    public double getArrivalWobble() {
        return 2.5;
    }

    @Override
    public boolean dropsZigzagEggs() {
        return true;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0xCC8899));
        g.fillOval(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);

        g.setColor(Color.WHITE);
        g.drawString("Z", x + 15, y + 21);
    }
}
