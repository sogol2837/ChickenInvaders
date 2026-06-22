package chickenInvaders.game;

import chickenInvaders.AppConfig;
import chickenInvaders.GameMain;
import chickenInvaders.GameState;
import chickenInvaders.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private final GameMain app;
    private final User user;

    private final Timer timer;

    private int gameState;

    private int planeX;
    private int planeY;
    private int planeSpeed;

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

        planeX = AppConfig.WINDOW_WIDTH / 2 - 20;
        planeY = AppConfig.WINDOW_HEIGHT - 100;
        planeSpeed = 5;
    }

    public void startGame() {
        timer.start();
    }

    private void updateGame() {
        if (gameState != GameState.RUNNING) {
            return;
        }

        if (leftPressed) {
            planeX -= planeSpeed;
        }

        if (rightPressed) {
            planeX += planeSpeed;
        }

        if (upPressed) {
            planeY -= planeSpeed;
        }

        if (downPressed) {
            planeY += planeSpeed;
        }

        if (planeX < 0) {
            planeX = 0;
        }

        if (planeX > AppConfig.WINDOW_WIDTH - 40) {
            planeX = AppConfig.WINDOW_WIDTH - 40;
        }

        if (planeY < 40) {
            planeY = 40;
        }

        if (planeY > AppConfig.WINDOW_HEIGHT - 80) {
            planeY = AppConfig.WINDOW_HEIGHT - 80;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawHud(g);
        drawPlane(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("gamePanel started. next step: Plane + Bullets + Enemies", 170, 300);
    }

    private void drawHud(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        g.drawString("User: " + user.getUsername(), 20, 25);
        g.drawString("Level: 1", 170, 25);
        g.drawString("Score: 0", 270, 25);
        g.drawString("Lives: 3", 370, 25);
    }

    private void drawPlane(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(planeX, planeY, 40, 40);

        g.setColor(Color.WHITE);
        g.drawRect(planeX, planeY, 40, 40);
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
