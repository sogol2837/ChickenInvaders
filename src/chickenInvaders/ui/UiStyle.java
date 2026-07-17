package chickenInvaders.ui;

import chickenInvaders.AppConfig;
import chickenInvaders.util.ImageLoader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public final class UiStyle {

    public static final Color BG = new Color(3, 5, 10);
    public static final Color CARD = new Color(12, 16, 25, 225);
    public static final Color CARD_LIGHT = new Color(22, 28, 42, 230);
    public static final Color SILVER = new Color(220, 225, 230);
    public static final Color MUTED = new Color(150, 160, 175);
    public static final Color CYAN = new Color(0x1B3B6F);
    public static final Color CYAN_DARK = new Color(0x002147);
    public static final Color WARNING = new Color(0xF6D8CE);
    public static final Color DANGER = new Color(0x630000);
    public static final Color SUCCESS = new Color(0x355E3B);


    private UiStyle() {
    }

    public static JPanel createBackgroundPanel() {
        return new JPanel(new BorderLayout()) {

            private final Image backgroundImage =
                ImageLoader.load(AppConfig.IMAGE_DIR + "outside_bg.png");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(new Color(0, 10, 25));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, 36));
        label.setBorder(BorderFactory.createEmptyBorder(22, 20, 6, 20));
        return label;
    }

    public static JLabel subtitle(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(MUTED);
        label.setFont(new Font("Monospaced", Font.PLAIN, 14));
        return label;
    }

    public static JPanel card() {
        JPanel panel = new TranslucentPanel(CARD);
        panel.setBorder(compoundBorder());
        return panel;
    }

    public static Border compoundBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 225, 235), 2),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CYAN_DARK, 1),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
            )
        );
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, true);
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, false);
        return button;
    }

    public static void styleButton(JButton button, boolean primary) {
        Color normal = primary ? new Color(18, 36, 52) : new Color(22, 24, 31);
        Color hover = primary ? new Color(0, 82, 105) : new Color(38, 42, 55);
        Color disabled = new Color(35, 35, 42);

        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(normal);
        button.setForeground(primary ? Color.WHITE : SILVER);
        button.setFont(new Font("Monospaced", Font.BOLD, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primary ? CYAN : SILVER, 2),
            BorderFactory.createEmptyBorder(9, 20, 9, 20)
        ));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hover);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(normal);
                }
            }
        });

        button.addChangeListener(e -> {
            if (!button.isEnabled()) {
                button.setBackground(disabled);
                button.setForeground(new Color(120, 125, 135));
            }
        });
    }

    public static void styleTextField(JTextField field) {
        field.setBackground(new Color(4, 8, 14));
        field.setForeground(Color.WHITE);
        field.setCaretColor(CYAN);
        field.setSelectionColor(new Color(0, 95, 120));
        field.setSelectedTextColor(Color.WHITE);
        field.setFont(new Font("Monospaced", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    public static JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(SILVER);
        label.setFont(new Font("Monospaced", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        return label;
    }

    public static JScrollPane scrollPane(Component component) {
        JScrollPane pane = new JScrollPane(component);
        pane.setBorder(BorderFactory.createLineBorder(new Color(190, 205, 215), 2));
        pane.getViewport().setBackground(BG);
        pane.setBackground(BG);
        styleScrollBar(pane.getVerticalScrollBar());
        styleScrollBar(pane.getHorizontalScrollBar());
        return pane;
    }

    public static void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(90, 105, 115);
                trackColor = new Color(8, 12, 18);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return zeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return zeroButton();
            }

            private JButton zeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
    }


    public static class TranslucentPanel extends JPanel {
        private final Color fillColor;

        public TranslucentPanel(Color fillColor) {
            this.fillColor = fillColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(fillColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class SpacePanel extends JPanel {
        private static final int STAR_COUNT = 180;
        private final int[] xs = new int[STAR_COUNT];
        private final int[] ys = new int[STAR_COUNT];
        private final int[] sizes = new int[STAR_COUNT];

        public SpacePanel() {
            setBackground(BG);
            Random random = new Random(42);
            for (int i = 0; i < STAR_COUNT; i++) {
                xs[i] = random.nextInt(1000);
                ys[i] = random.nextInt(800);
                sizes[i] = random.nextInt(100) < 88 ? 1 : 2;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            g2.setColor(BG);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(10, 18, 28));
            g2.fillOval(-160, getHeight() - 210, 400, 320);
            g2.fillOval(getWidth() - 180, -130, 360, 270);

            for (int i = 0; i < STAR_COUNT; i++) {
                int x = xs[i] * getWidth() / 1000;
                int y = ys[i] * getHeight() / 800;
                int size = sizes[i];
                int alpha = 120 + (i * 31) % 120;
                g2.setColor(new Color(240, 245, 255, alpha));
                g2.fillRect(x, y, size, size);

                if (i % 29 == 0) {
                    g2.drawLine(x - 3, y, x + 3, y);
                    g2.drawLine(x, y - 3, x, y + 3);
                }
            }

            g2.setColor(new Color(0, 210, 245, 60));
            g2.drawRect(10, 10, getWidth() - 21, getHeight() - 21);
            g2.setColor(new Color(230, 240, 245, 70));
            g2.drawRect(14, 14, getWidth() - 29, getHeight() - 29);
            g2.dispose();
        }
    }
}
