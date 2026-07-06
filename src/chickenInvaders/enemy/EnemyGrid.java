package chickenInvaders.enemy;

import chickenInvaders.AppConfig;
import chickenInvaders.entity.Egg;
import chickenInvaders.entity.Plane;
import chickenInvaders.model.EnemyType;
import chickenInvaders.util.RandomUtils;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;


public class EnemyGrid {

    private static final int ROWS = 5;
    private static final int COLS = 8;
    private static final int START_X = 90;
    private static final int START_Y = 70;
    private static final int GAP_X = 75;
    private static final int GAP_Y = 55;
    private static final int MAX_ENEMY_WIDTH = 44;
    private static final int MAX_ENEMY_HEIGHT = 36;
    private static final int EDGE_MARGIN = 20;


    private static final int SAFETY_MARGIN = 100;
    private static final int MAX_SHIFT_Y =
        AppConfig.WINDOW_HEIGHT - SAFETY_MARGIN - MAX_ENEMY_HEIGHT - (START_Y + (ROWS - 1) * GAP_Y);

    private final EnemyCell[] cells = new EnemyCell[ROWS * COLS];
    private final List<Egg> eggs = new ArrayList<>();

    private int currentLevel;
    private int direction = 1;
    private double speed;
    private int verticalStep;
    private long eggIntervalMs;
    private long eggTimer;

    private double shiftX;
    private double shiftY;

    public EnemyGrid(int level) {
        rebuild(level);
    }

    public void nextLevel(int level) {
        rebuild(level);
    }

    private void rebuild(int level) {
        currentLevel = level;
        direction = 1;
        shiftX = 0;
        shiftY = 0;
        eggTimer = 0;
        eggs.clear();

        setupLevelParams(level);
        int startingLives = initialCounterFor(level);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int homeX = START_X + col * GAP_X;
                int homeY = START_Y + row * GAP_Y;
                EnemyType kind = typeFor(level, row, col);

                EnemyCell cell = new EnemyCell(row, col, kind, homeX, homeY, startingLives);
                cell.setOccupant(createEnemy(kind, homeX, homeY, level));
                cells[row * COLS + col] = cell;
            }
        }
    }

    private void setupLevelParams(int level) {
        switch (level) {
            case 1 -> { speed = 1.5; verticalStep = 20; eggIntervalMs = 3000; }
            case 2 -> { speed = 2.0; verticalStep = 20; eggIntervalMs = 2000; }
            case 3 -> { speed = 1.5; verticalStep = 25; eggIntervalMs = 1500; }
            case 5 -> { speed = 2.5; verticalStep = 25; eggIntervalMs = 1000; }
            case 6 -> { speed = 3.0; verticalStep = 30; eggIntervalMs = 800; }
            case 7 -> { speed = 3.5; verticalStep = 30; eggIntervalMs = 700; }
            default -> { speed = 1.0; verticalStep = 20; eggIntervalMs = 3000; }
        }
    }

    private int initialCounterFor(int level) {
        return switch (level) {
            case 1, 2 -> 2;
            case 3, 5 -> 3;
            case 6, 7 -> 4;
            default -> 2;
        };
    }

    private EnemyType typeFor(int level, int row, int col) {
        int key = row + col;
        return switch (level) {
            case 1 -> EnemyType.NORMAL;
            case 2 -> key % 3 == 0 ? EnemyType.FAST : EnemyType.NORMAL;
            case 3 -> key % 3 == 0 ? EnemyType.ZIGZAG : EnemyType.NORMAL;
            case 5 -> key % 2 == 0 ? EnemyType.SHOOTER : EnemyType.FAST;
            case 6 -> key % 2 == 0 ? EnemyType.ZIGZAG : EnemyType.SHOOTER;
            case 7 -> switch (key % 4) {
                case 0 -> EnemyType.NORMAL;
                case 1 -> EnemyType.FAST;
                case 2 -> EnemyType.ZIGZAG;
                default -> EnemyType.SHOOTER;
            };
            default -> EnemyType.NORMAL;
        };
    }

    private Enemy createEnemy(EnemyType kind, int x, int y, int level) {
        return switch (kind) {
            case NORMAL -> new NormalEnemy(x, y, level);
            case FAST -> new FastEnemy(x, y, level);
            case ZIGZAG -> new ZigzagEnemy(x, y, level);
            case SHOOTER -> new ShooterEnemy(x, y, level);
        };
    }

    public void update(boolean frozen, Plane plane) {
        if (!frozen) {
            moveFormation();
            updateArrivals();
            handleRespawns();
            updateEggTimer(plane);
        }

        for (Egg egg : eggs) {
            if (!frozen) {
                egg.update();
            }
        }
        eggs.removeIf(egg -> !egg.isActive());
    }

    private void moveFormation() {
        double nextShiftX = shiftX + direction * speed;
        double leftEdge = START_X + nextShiftX;
        double rightEdge = START_X + (COLS - 1) * GAP_X + MAX_ENEMY_WIDTH + nextShiftX;

        if (leftEdge <= EDGE_MARGIN || rightEdge >= AppConfig.WINDOW_WIDTH - EDGE_MARGIN) {
            direction *= -1;
            shiftY = Math.min(MAX_SHIFT_Y, shiftY + verticalStep);
        } else {
            shiftX = nextShiftX;
        }

        for (EnemyCell cell : cells) {
            if (cell.isArriving() || cell.getOccupant() == null) {
                continue;
            }
            Enemy enemy = cell.getOccupant();
            enemy.setX((int) Math.round(cell.getHomeX() + shiftX));
            enemy.setY((int) Math.round(cell.getHomeY() + shiftY));
        }
    }

    private void updateArrivals() {
        for (EnemyCell cell : cells) {
            if (!cell.isArriving()) {
                continue;
            }

            Enemy enemy = cell.getOccupant();
            double targetX = cell.getHomeX() + shiftX;
            double targetY = cell.getHomeY() + shiftY;

            double dx = targetX - enemy.getX();
            double dy = targetY - enemy.getY();
            double distance = Math.hypot(dx, dy);
            int arrivalSpeed = enemy.getArrivalSpeed();

            if (distance <= arrivalSpeed) {
                enemy.setX((int) Math.round(targetX));
                enemy.setY((int) Math.round(targetY));
                cell.setArriving(false);
                continue;
            }

            double moveX = dx / distance * arrivalSpeed;
            double moveY = dy / distance * arrivalSpeed;

            double wobble = enemy.getArrivalWobble();
            if (wobble > 0) {
                cell.advanceWobble();
                moveX += Math.sin(cell.getWobblePhase()) * wobble;
            }

            enemy.move((int) Math.round(moveX), (int) Math.round(moveY));
        }
    }

    private void handleRespawns() {
        for (EnemyCell cell : cells) {
            Enemy occupant = cell.getOccupant();

            if (occupant != null && !occupant.isActive() && !cell.isArriving()) {
                cell.setOccupant(null);
                cell.decrementRemaining();

                if (cell.getRemaining() > 0) {
                    spawnReplacement(cell);
                }
            }
        }
    }

    private void spawnReplacement(EnemyCell cell) {
        boolean fromLeft = RandomUtils.coinFlip();
        int spawnX = fromLeft ? -MAX_ENEMY_WIDTH : AppConfig.WINDOW_WIDTH;
        int spawnY = 15;

        Enemy replacement = createEnemy(cell.getKind(), spawnX, spawnY, currentLevel);
        cell.setOccupant(replacement);
        cell.setArriving(true);
        cell.resetWobble();
    }

    private void updateEggTimer(Plane plane) {
        eggTimer += AppConfig.FPS_DELAY;
        if (eggTimer < eggIntervalMs) {
            return;
        }
        eggTimer = 0;

        List<EnemyCell> candidates = new ArrayList<>();
        for (EnemyCell cell : cells) {
            Enemy occ = cell.getOccupant();
            if (occ != null && occ.isActive() && !cell.isArriving()) {
                candidates.add(cell);
            }
        }
        if (candidates.isEmpty()) {
            return;
        }

        EnemyCell chosen = candidates.get(RandomUtils.between(0, candidates.size() - 1));
        Enemy enemy = chosen.getOccupant();

        int originX = enemy.getX() + enemy.getWidth() / 2;
        int originY = enemy.getY() + enemy.getHeight();

        if (enemy.canFireAtPlane() && plane != null && RandomUtils.chance(0.5)) {
            int targetX = plane.getX() + plane.getWidth() / 2;
            int targetY = plane.getY() + plane.getHeight() / 2;
            eggs.add(Egg.aimedAt(originX, originY, targetX, targetY, 5));
        } else {
            Egg egg = Egg.withVelocity(originX - 5, originY, 0, 4);
            egg.setZigzag(enemy.dropsZigzagEggs());
            eggs.add(egg);
        }
    }

    public void draw(Graphics g) {
        for (EnemyCell cell : cells) {
            Enemy occ = cell.getOccupant();
            if (occ != null && occ.isActive()) {
                occ.draw(g);
            }
        }
        for (Egg egg : eggs) {
            egg.draw(g);
        }
    }

    public List<Enemy> getActiveEnemies() {
        List<Enemy> active = new ArrayList<>();
        for (EnemyCell cell : cells) {
            Enemy occ = cell.getOccupant();
            if (occ != null && occ.isActive()) {
                active.add(occ);
            }
        }
        return active;
    }

    public boolean isCleared() {
        for (EnemyCell cell : cells) {
            if (!cell.isCleared()) {
                return false;
            }
        }
        return true;
    }

    public List<Egg> getEggs() {
        return eggs;
    }
}
