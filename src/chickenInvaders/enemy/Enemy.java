package chickenInvaders.enemy;

import chickenInvaders.entity.GameObject;

public abstract class Enemy extends GameObject {

    protected int health;
    protected int scoreValue;
    protected boolean active;

    public Enemy(int x, int y, int width, int height, int health, int scoreValue) {
        super(x, y, width, height);
        this.health = health;
        this.scoreValue = scoreValue;
        this.active = true;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void damage() {
        health--;
        if (health <= 0) {
            active = false;
        }
    }

    public void forceKill() {
        health = 0;
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public int getHealth() {
        return health;
    }

    public int getArrivalSpeed() {
        return 5;
    }

    public double getArrivalWobble() {
        return 0;
    }

    public boolean dropsZigzagEggs() {
        return false;
    }
    //shooter
    public boolean canFireAtPlane() {
        return false;
    }
}
