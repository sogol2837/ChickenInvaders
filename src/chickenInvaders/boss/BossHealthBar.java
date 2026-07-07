package chickenInvaders.boss;

import java.awt.Color;
import java.awt.Graphics;

public class BossHealthBar {

    private BossHealthBar() {
    }

    public static void draw(Graphics g, int x, int y, int width, int height, int current, int max) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);

        int filled = max == 0 ? 0 : (int) (width * (current / (double) max));
        g.setColor(healthColor(current, max));
        g.fillRect(x, y, filled, height);

        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);
    }

    private static Color healthColor(int current, int max) {
        double ratio = max == 0 ? 0 : current / (double) max;
        if (ratio > 0.5) {
            return Color.GREEN;
        }
        if (ratio > 0.2) {
            return Color.ORANGE;
        }
        return Color.RED;
    }
}
