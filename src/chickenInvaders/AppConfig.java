package chickenInvaders;

public class AppConfig {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    //logic game and ui update every 16ms
    public static final int FPS_DELAY = 16;

    public static final String DATA_DIR = "data";
    public static final String USERS_FILE = "data/users.txt";
    public static final String SCORES_FILE = "data/scores.txt";

    public static final String SOUND_DIR = "assets/sounds/";
    public static final String IMAGE_DIR = "assets/images/";

    private AppConfig() {
    }
}
