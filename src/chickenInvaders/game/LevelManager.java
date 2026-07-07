package chickenInvaders.game;

public class LevelManager {

    private int currentLevel;

    public LevelManager() {
        currentLevel = 1;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void nextLevel() {
        currentLevel++;
    }

    public void reset() {
        currentLevel = 1;
    }

    public boolean isBossLevel() {
        return currentLevel == 4 || currentLevel == 8;
    }
}
