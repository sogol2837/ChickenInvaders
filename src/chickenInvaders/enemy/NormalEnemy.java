package chickenInvaders.enemy;

import java.awt.Color;
import java.awt.Graphics;

public class NormalEnemy extends Enemy {

    public NormalEnemy(int x, int y, int level) {
        super(x, y, 42, 34, healthForLevel(level), 10);
    }

    private static int healthForLevel(int level) {
        return level >= 5 ? 3 : 2;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0x7BA05B));
        g.fillOval(x, y, width, height);

        g.setColor(Color.ORANGE);
        g.fillOval(x + 6, y + 8, 8, 8);
        g.fillOval(x + width - 14, y + 8, 8, 8);

        g.setColor(Color.RED);
        g.fillPolygon(
            new int[]{x + width / 2 - 5, x + width / 2 + 5, x + width / 2},
            new int[]{y + height, y + height, y + height + 8},
            3
        );

        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);
    }
}
