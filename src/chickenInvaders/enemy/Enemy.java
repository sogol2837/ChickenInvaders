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

    // Hooks that EnemyGrid uses to give each type a distinct feel while it is
    // "arriving" (flying in from a corner as a respawn), without EnemyGrid
    // needing to hard-code per-type checks.
    //
    // This must stay comfortably above the fastest formation horizontal
    // speed used by any level (3.5 px/frame at level 7). Otherwise a
    // respawning enemy chasing a formation that happens to be drifting away
    // from it can end up in a speed-tie it can never close - it looked fine
    // in casual play, but a stress test (killing enemies continuously)
    // exposed that levels 6-7 could stall forever for non-Fast types.
    public int getArrivalSpeed() {
        return 5;
    }

    public double getArrivalWobble() {
        return 0;
    }

    public boolean dropsZigzagEggs() {
        return false;
    }

    public boolean canFireAtPlane() {
        return false;
    }
}
