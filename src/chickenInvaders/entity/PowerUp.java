package chickenInvaders.entity;

import chickenInvaders.AppConfig;
import chickenInvaders.model.PowerUpType;

import java.awt.Color;
import java.awt.Graphics;

public class PowerUp extends GameObject {

    private static final int SPEED = 2;

    private final PowerUpType type;
    private boolean active = true;

    public PowerUp(int x, int y, PowerUpType type) {
        super(x, y, 22, 22);
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
        g.setColor(colorFor(type));
        g.fillRoundRect(x, y, width, height, 8, 8);

        g.setColor(Color.BLACK);
        g.drawString(labelFor(type), x + 3, y + 15);
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
