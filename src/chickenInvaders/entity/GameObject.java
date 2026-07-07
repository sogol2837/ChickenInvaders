package chickenInvaders.entity;

import chickenInvaders.AppConfig;
import chickenInvaders.util.ImageLoader;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();

    public abstract void draw(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean intersects(GameObject other) {
        return getBounds().intersects(other.getBounds());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    //add images
    protected boolean drawSprite(Graphics g, String fileName) {
        Image image = ImageLoader.load(AppConfig.IMAGE_DIR + fileName);

        if (image == null) {
            return false;
        }

        g.drawImage(image, x, y, width, height, null);
        return true;
    }
}
