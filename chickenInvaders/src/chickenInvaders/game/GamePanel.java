package chickenInvaders.game;

import chickenInvaders.AppConfig;
import chickenInvaders.GameMain;
import chickenInvaders.GameState;
import chickenInvaders.entity.Bullet;
import chickenInvaders.entity.Plane;
import chickenInvaders.model.User;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private final GameMain app;
    private final User user;
    private final Timer timer;

    private int gameState;
    private int level;
    private int score;

    private Plane plane;
    private List<Bullet> bullets;

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    public GamePanel(GameMain app, User user) {
        this.app = app;
        this.user = user;

        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(AppConfig.FPS_DELAY, this);

        gameState = GameState.RUNNING;
        level = 1;
        score = 0;

        plane = new Plane(AppConfig.WINDOW_WIDTH / 2 - 22, AppConfig.WINDOW_HEIGHT - 110);
        bullets = new ArrayList<>();
    }

    public void startGame() {
        requestFocusInWindow();
        timer.start();
    }

    private void updateGame() {
        if (gameState != GameState.RUNNING) {
            return;
        }

        plane.move(leftPressed, rightPressed, upPressed, downPressed);
        updateBullets();
    }

    private void updateBullets() {
        Iterator<Bullet> iterator = bullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();

            if (!bullet.isActive()) {
                iterator.remove();
            }
        }
    }

    private void shoot() {
        if (gameState != GameState.RUNNING) {
            return;
        }

        bullets.addAll(plane.shoot());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawHud(g);
        plane.draw(g);
        drawBullets(g);

        if (gameState == GameState.PAUSED) {
            drawPause(g);
        }
    }

    private void drawHud(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        g.drawString("User: " + user.getUsername(), 20, 25);
        g.drawString("Level: " + level, 170, 25);
        g.drawString("Score: " + score, 270, 25);
        g.drawString("Lives: " + plane.getLives(), 370, 25);
        g.drawString("Fire: " + plane.getFireCount(), 470, 25);
        g.drawString("P: Pause | ESC: Menu | SPACE: Shoot", 20, AppConfig.WINDOW_HEIGHT - 55);
    }

    private void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    private void drawPause(Graphics g) {
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 42));
        g.drawString("PAUSED", 310, 300);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = true;
        }

        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = true;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            upPressed = true;
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            downPressed = true;
        }

        if (key == KeyEvent.VK_SPACE) {
            shoot();
        }

        if (key == KeyEvent.VK_P) {
            if (gameState == GameState.RUNNING) {
                gameState = GameState.PAUSED;
            } else if (gameState == GameState.PAUSED) {
                gameState = GameState.RUNNING;
            }
        }

        if (key == KeyEvent.VK_ESCAPE) {
            timer.stop();
            app.showMainMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            upPressed = false;
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            downPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
