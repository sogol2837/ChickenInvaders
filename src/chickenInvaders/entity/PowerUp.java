package chickenInvaders.entity;

import chickenInvaders.AppConfig;
import chickenInvaders.model.PowerUpType;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class PowerUp extends GameObject {

    private static final int SPEED = 2;

    private final PowerUpType type;
    private boolean active = true;

    public PowerUp(int centerX, int centerY, PowerUpType type) {
        super(
            centerX - widthFor(type) / 2,
            centerY - heightFor(type) / 2,
            widthFor(type),
            heightFor(type)
        );

        this.type = type;
    }

    @Override
    public void update() {
        y += SPEED;

        if (y > AppConfig.WINDOW_HEIGHT) {
            active = false;
        }
    }

    @Override
    public void draw(Graphics g) {

        if (drawSprite(g, imageNameFor(type))) {
            return;
        }


        g.setColor(colorFor(type));
        g.fillRoundRect(x, y, width, height, 10, 10);

        g.setColor(Color.BLACK);

        String label = labelFor(type);
        FontMetrics fm = g.getFontMetrics();

        int textX = x + (width - fm.stringWidth(label)) / 2;
        int textY = y + (height + fm.getAscent()) / 2 - 3;

        g.drawString(label, textX, textY);
    }

    private static int widthFor(PowerUpType type) {
        return switch (type) {
            case ADD_FIRE -> 36;
            case RAPID_FIRE -> 32;
            case EXTRA_LIFE -> 65;
            case SHIELD -> 42;
            case FREEZE_BOMB -> 50;
        };
    }

    private static int heightFor(PowerUpType type) {
        return switch (type) {
            case ADD_FIRE -> 36;
            case RAPID_FIRE -> 32;
            case EXTRA_LIFE -> 60;
            case SHIELD -> 42;
            case FREEZE_BOMB -> 38;
        };
    }

    private String imageNameFor(PowerUpType type) {
        return switch (type) {
            case ADD_FIRE -> "power_add_fire.png";
            case RAPID_FIRE -> "power_rapid_fire.png";
            case EXTRA_LIFE -> "power_extra_life.png";
            case SHIELD -> "power_shield.png";
            case FREEZE_BOMB -> "power_freeze.png";
        };
    }

    private Color colorFor(PowerUpType type) {
        return switch (type) {
            case ADD_FIRE -> Color.YELLOW;
            case RAPID_FIRE -> Color.ORANGE;
            case EXTRA_LIFE -> Color.PINK;
            case SHIELD -> Color.CYAN;
            case FREEZE_BOMB -> Color.BLUE;
        };
    }

    private String labelFor(PowerUpType type) {
        return switch (type) {
            case ADD_FIRE -> "F+";
            case RAPID_FIRE -> "R";
            case EXTRA_LIFE -> "+1";
            case SHIELD -> "S";
            case FREEZE_BOMB -> "Z";
        };
    }

    public PowerUpType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void collect() {
        active = false;
    }
}
