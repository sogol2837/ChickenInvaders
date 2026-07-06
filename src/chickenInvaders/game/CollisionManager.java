package chickenInvaders.game;

import chickenInvaders.audio.SoundManager;
import chickenInvaders.boss.Boss;
import chickenInvaders.enemy.Enemy;
import chickenInvaders.enemy.EnemyGrid;
import chickenInvaders.entity.Bullet;
import chickenInvaders.entity.Egg;
import chickenInvaders.entity.Explosion;
import chickenInvaders.entity.GameObject;
import chickenInvaders.entity.Plane;
import chickenInvaders.entity.PowerUp;
import chickenInvaders.model.PowerUpType;
import chickenInvaders.util.RandomUtils;

import java.util.Iterator;
import java.util.List;

public class CollisionManager {

    private static final double POWER_UP_DROP_CHANCE = 0.2;

    public void update(
        Plane plane,
        List<Bullet> bullets,
        EnemyGrid enemyGrid,
        Boss boss,
        ScoreManager scoreManager,
        List<Explosion> explosions,
        List<PowerUp> powerUps
    ) {
        handleBulletsVsEnemies(bullets, enemyGrid, scoreManager, explosions, powerUps);
        handleBulletsVsBoss(bullets, boss, plane, scoreManager, explosions);
        handleEnemiesVsPlane(enemyGrid, plane, explosions);
        handleEggsVsPlane(enemyGrid, boss, plane, explosions);

        bullets.removeIf(b -> !b.isActive());
    }

    private void handleBulletsVsEnemies(List<Bullet> bullets, EnemyGrid enemyGrid,
                                         ScoreManager scoreManager, List<Explosion> explosions,
                                         List<PowerUp> powerUps) {
        if (enemyGrid == null) {
            return;
        }

        List<Enemy> activeEnemies = enemyGrid.getActiveEnemies();

        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) {
                continue;
            }

            for (Enemy enemy : activeEnemies) {
                if (!enemy.isActive()) {
                    continue;
                }

                if (bullet.intersects(enemy)) {
                    bullet.deactivate();
                    enemy.damage();

                    if (!enemy.isActive()) {
                        scoreManager.addScore(enemy.getScoreValue());
                        spawnExplosion(explosions, centerX(enemy), centerY(enemy));
                        maybeDropPowerUp(enemy, powerUps);
                    }
                    break;
                }
            }
        }
    }

    private void handleBulletsVsBoss(List<Bullet> bullets, Boss boss, Plane plane,
                                      ScoreManager scoreManager, List<Explosion> explosions) {
        if (boss == null || boss.isDefeated()) {
            return;
        }

        int damage = plane.hasDoubleBossDamage() ? 2 : 1;

        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) {
                continue;
            }

            if (bullet.intersects(boss)) {
                bullet.deactivate();
                boss.takeDamage(damage);
                spawnExplosion(explosions, centerX(bullet), centerY(bullet));

                if (boss.isDefeated() && !boss.isBonusAwarded()) {
                    scoreManager.addScore(boss.getDefeatBonus());
                    boss.markBonusAwarded();
                    spawnExplosion(explosions, centerX(boss), centerY(boss));
                }
            }
        }
    }

    private void handleEnemiesVsPlane(EnemyGrid enemyGrid, Plane plane, List<Explosion> explosions) {
        if (enemyGrid == null) {
            return;
        }

        for (Enemy enemy : enemyGrid.getActiveEnemies()) {
            if (enemy.intersects(plane)) {
                enemy.forceKill();
                applyHitToPlane(plane, explosions);
                break;
            }
        }
    }

    private void handleEggsVsPlane(EnemyGrid enemyGrid, Boss boss, Plane plane, List<Explosion> explosions) {
        List<Egg> eggSource = enemyGrid != null ? enemyGrid.getEggs() : (boss != null ? boss.getEggs() : null);
        if (eggSource == null) {
            return;
        }

        Iterator<Egg> iterator = eggSource.iterator();
        while (iterator.hasNext()) {
            Egg egg = iterator.next();
            if (!egg.isActive()) {
                continue;
            }
            if (egg.intersects(plane)) {
                egg.destroy();
                applyHitToPlane(plane, explosions);
            }
        }
    }

    private void applyHitToPlane(Plane plane, List<Explosion> explosions) {
        spawnExplosion(explosions, centerX(plane), centerY(plane));
        if (!plane.isShieldActive()) {
            plane.loseLife();
        }
    }

    private void spawnExplosion(List<Explosion> explosions, int x, int y) {
        explosions.add(new Explosion(x, y));
        SoundManager.getInstance().playExplosion();
    }

    private void maybeDropPowerUp(Enemy enemy, List<PowerUp> powerUps) {
        if (RandomUtils.chance(POWER_UP_DROP_CHANCE)) {
            PowerUpType type = RandomUtils.pick(PowerUpType.values());
            powerUps.add(new PowerUp(centerX(enemy) - 11, centerY(enemy) - 11, type));
        }
    }

    private int centerX(GameObject obj) {
        return obj.getX() + obj.getWidth() / 2;
    }

    private int centerY(GameObject obj) {
        return obj.getY() + obj.getHeight() / 2;
    }
}
