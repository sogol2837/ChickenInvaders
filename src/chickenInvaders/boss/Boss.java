package chickenInvaders.boss;

import chickenInvaders.AppConfig;
import chickenInvaders.entity.Egg;
import chickenInvaders.entity.GameObject;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;


public abstract class Boss extends GameObject {

    protected int maxHealth;
    protected int health;
    protected final int scoreBonus;
    protected final long attackIntervalMs;
    protected long attackTimer;
    protected boolean bonusAwarded;

    protected final List<Egg> eggs = new ArrayList<>();

    public Boss(int x, int y, int width, int height, int maxHealth, long attackIntervalMs, int scoreBonus) {
        super(x, y, width, height);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackIntervalMs = attackIntervalMs;
        this.scoreBonus = scoreBonus;
    }

    public void update(boolean frozen) {
        for (Egg egg : eggs) {
            if (!frozen) {
                egg.update();
            }
        }
        eggs.removeIf(egg -> !egg.isActive());

        if (frozen || isDefeated()) {
            return;
        }

        moveBoss();

        attackTimer += AppConfig.FPS_DELAY;
        if (attackTimer >= attackIntervalMs) {
            attackTimer = 0;
            fireAttack();
        }
    }

    protected abstract void moveBoss();

    protected abstract void fireAttack();

    protected abstract void drawBody(Graphics g);

    @Override
    public void draw(Graphics g) {
        drawBody(g);
        BossHealthBar.draw(g, x, y - 20, width, 10, health, maxHealth);
        for (Egg egg : eggs) {
            egg.draw(g);
        }
    }

    // GameObject requires a no-arg update(); the real per-frame logic needs
    // to know whether the freeze-bomb is active, so it lives in update(boolean)
    // above and GamePanel always calls that overload instead.
    @Override
    public void update() {
    }

    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
    }

    public boolean isDefeated() {
        return health <= 0;
    }

    public int getDefeatBonus() {
        return scoreBonus;
    }

    public boolean isBonusAwarded() {
        return bonusAwarded;
    }

    public void markBonusAwarded() {
        bonusAwarded = true;
    }

    public List<Egg> getEggs() {
        return eggs;
    }
}
