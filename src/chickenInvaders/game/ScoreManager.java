package chickenInvaders.game;

public class ScoreManager {
    private int score;

    public void addScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }
}
