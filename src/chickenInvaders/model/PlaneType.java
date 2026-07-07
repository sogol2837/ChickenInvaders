package chickenInvaders.model;


public enum PlaneType {

    DEFAULT(0, 5, 300, 3, false),
    FAST(5000, 7, 250, 3, false),
    HEAVY(8000, 4, 200, 5, false),
    SNIPER(10000, 5, 150, 3, true);

    private final int cost;
    private final int speed;
    private final int fireDelayMs;
    private final int startingLives;
    private final boolean doubleBossDamage;

    PlaneType(int cost, int speed, int fireDelayMs, int startingLives, boolean doubleBossDamage) {
        this.cost = cost;
        this.speed = speed;
        this.fireDelayMs = fireDelayMs;
        this.startingLives = startingLives;
        this.doubleBossDamage = doubleBossDamage;
    }

    public int getCost() {
        return cost;
    }

    public int getSpeed() {
        return speed;
    }

    public int getFireDelayMs() {
        return fireDelayMs;
    }

    public int getStartingLives() {
        return startingLives;
    }

    public boolean hasDoubleBossDamage() {
        return doubleBossDamage;
    }
}
