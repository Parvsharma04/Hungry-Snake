import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Board extends JPanel implements ActionListener {

    private int dots;
    private int apple_x;
    private int apple_y;

    private Image dot;
    private Image apple;
    private Image head;

    private final int TOTAL_DOTS = 900;
    private final int DOT_SIZE = 10;

    private final int[] x = new int[TOTAL_DOTS];
    private final int[] y = new int[TOTAL_DOTS];

    private javax.swing.Timer timer;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;

    private boolean inGame = true;

    Board() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(300, 300));

        loadImages();
        initGame();
    }

    public void loadImages() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/dot.png"));
        dot = i1.getImage();

        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("icons/apple.png"));
        apple = i2.getImage();

        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icons/head.png"));
        head = i3.getImage();
    }

    public void initGame() {
        dots = 3;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }

        locateApple();

        timer = new javax.swing.Timer(140, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over !";
        String score = "   Score: " + (dots - 3);
        String msgRestart = "Press Space to Restart";
        Font font = new Font("SansSerif", Font.BOLD, 20);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        int msgWidth = metrics.stringWidth(msg);
        int scoreWidth = metrics.stringWidth(score);
        int maxWidth = Math.max(msgWidth, scoreWidth);
        int x = (300 - maxWidth) / 2;
        int y = 300 / 2 - lineHeight / 2;
        g.setColor(Color.RED);
        g.drawString(msg, x, y-20);
        g.setColor(Color.YELLOW);
        g.drawString(score, x, y + lineHeight);
        g.setColor(Color.WHITE);
        font = new Font("SansSerif", Font.PLAIN, 15);
        g.setFont(font);
        g.drawString(msgRestart, x - 25, y + lineHeight + 70);
    }

    public void locateApple() {
        int rx = (int) (Math.random() * 29);
        apple_x = rx * DOT_SIZE;
        int ry = (int) (Math.random() * 29);
        apple_y = ry * DOT_SIZE;
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                up = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_SPACE && !inGame) {
                inGame = true;
                initGame();
            }
        }
    }

    public void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++;
            locateApple();
        }
    }

    public void checkCollison() {
        if (dots > 4) {
            for (int i = dots; i > 0; i--) {
                if (x[0] == x[i] && y[0] == y[i]) {
                    inGame = false;
                }
            }
        }
        if (x[0] >= 300) {
            inGame = false;
        }
        if (y[0] >= 300) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent a) {
        if (inGame) {
            checkApple();
            checkCollison();
            move();
        }
        repaint();
    }
}

public class SnakeGame extends JFrame {

    SnakeGame() {
        super("Hungry Snake");

        add(new Board());
        pack(); // Refreshes the frames to reflect the changes
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
