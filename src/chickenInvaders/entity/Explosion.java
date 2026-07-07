package chickenInvaders.entity;

import chickenInvaders.AppConfig;
import chickenInvaders.util.ImageLoader;

import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;


public class Explosion extends GameObject {

    private static final int SIZE = 40;
    private static final int LIFESPAN_FRAMES = 18;

    private int age;

    public Explosion(int centerX, int centerY) {
        super(centerX - SIZE / 2, centerY - SIZE / 2, SIZE, SIZE);
    }

    @Override
    public void update() {
        age++;
    }

    @Override
    public void draw(Graphics g) {
        if (isFinished()) {
            return;
        }

        double progress = age / (double) LIFESPAN_FRAMES;
        int size = (int) (width * (0.4 + 0.6 * progress));
        int alpha = Math.max(0, (int) (255 * (1 - progress)));
        int offset = (width - size) / 2;

        Image image = ImageLoader.load(AppConfig.IMAGE_DIR + "explosion.png");

        if (image != null) {
            g.drawImage(image, x + offset, y + offset, size, size, null);
            return;
        }

        g.setColor(new Color(255, 165, 0, alpha));
        g.fillOval(x + offset, y + offset, size, size);

        g.setColor(new Color(255, 60, 0, alpha));
        g.drawOval(x + offset, y + offset, size, size);
    }

    public boolean isFinished() {
        return age >= LIFESPAN_FRAMES;
    }
}
