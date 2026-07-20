package chickenInvaders.enemy;

import java.awt.Color;
import java.awt.Graphics;

public class FastEnemy extends Enemy {

    private double exactFastX;

    //each fast enemy can start in a different direction
    private int fastDirection;

    //prevents using the old spawn position after arrival
    private boolean fastMovementStarted;

    public FastEnemy(int x, int y, int level) {
        super(x, y, 60, 50, healthForLevel(level), 15);

        exactFastX = x;

        if (Math.random() < 0.5) {
            fastDirection = -1;
        }
        else {
            fastDirection = 1;
        }

        fastMovementStarted = false;
    }

    private static int healthForLevel(int level) {
        return level >= 5 ? 2 : 1;
    }

    @Override
    public int getArrivalSpeed() {
        return 14;
    }

    //moves independently across the whole screen
    public void updateFastMovement(double formationSpeed, int screenWidth, int margin) {

        //after arrival, start from the enemy's current position
        if (!fastMovementStarted) {
            exactFastX = getX();
            fastMovementStarted = true;
        }

        //fast enemy is clearly faster than the normal formation
        double fastSpeed = formationSpeed * 2.0;

        exactFastX += fastDirection * fastSpeed;

        //left border
        if (exactFastX <= margin) {
            exactFastX = margin;
            fastDirection = 1;
        }

        //right border
        else if (exactFastX + width >= screenWidth - margin) {
            exactFastX = screenWidth - margin - width;
            fastDirection = -1;
        }

        setX((int) Math.round(exactFastX));
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        if (drawSprite(g, "fastEnemy.png")) {
            return;
        }

        g.setColor(new Color(0xE5E4E2));
        g.fillOval(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);

        g.setColor(Color.WHITE);
        g.drawString("F", x + 14, y + 20);
    }
}
