package chickenInvaders.enemy;

import chickenInvaders.model.EnemyType;


public class EnemyCell {

    private final int row;
    private final int col;
    private final EnemyType kind;
    private final int homeX;
    private final int homeY;

    private int remaining;
    private Enemy occupant;
    private boolean arriving;
    private double wobblePhase;

    public EnemyCell(int row, int col, EnemyType kind, int homeX, int homeY, int remaining) {
        this.row = row;
        this.col = col;
        this.kind = kind;
        this.homeX = homeX;
        this.homeY = homeY;
        this.remaining = remaining;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public EnemyType getKind() {
        return kind;
    }

    public int getHomeX() {
        return homeX;
    }

    public int getHomeY() {
        return homeY;
    }

    public int getRemaining() {
        return remaining;
    }

    public void decrementRemaining() {
        remaining--;
    }

    public Enemy getOccupant() {
        return occupant;
    }

    public void setOccupant(Enemy occupant) {
        this.occupant = occupant;
    }

    public boolean isArriving() {
        return arriving;
    }

    public void setArriving(boolean arriving) {
        this.arriving = arriving;
    }

    public void resetWobble() {
        wobblePhase = 0;
    }

    public void advanceWobble() {
        wobblePhase += 0.3;
    }

    public double getWobblePhase() {
        return wobblePhase;
    }

    public boolean isCleared() {
        return remaining <= 0 && occupant == null;
    }
}
