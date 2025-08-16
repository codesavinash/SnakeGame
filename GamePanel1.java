import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GamePanel1 extends JPanel implements ActionListener {

    private Snake snake;
    private Apple apple;
    private int applesEaten;
    private boolean running;
    private boolean paused = false;
    private javax.swing.Timer timer;  // explicitly use javax.swing.Timer
    private int highScore;
    private Random random;
    private java.util.List<Particle> particles = new ArrayList<>(); // explicitly java.util.List

    private int pulse = 0;
    private boolean pulseDirection = true;
    private Color[] rainbowColors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA};
    private int flashCounter = 0;

    public GamePanel1() {
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        random = new Random();

        loadHighScore();
        startGame();
    }

    public void startGame() {
        snake = new Snake((Constants.SCREEN_WIDTH * Constants.SCREEN_HEIGHT) / Constants.UNIT_SIZE, 6);
        apple = new Apple();
        applesEaten = 0;
        running = true;
        paused = false;
        timer = new javax.swing.Timer(Constants.DELAY, this);
        timer.start();
        pulse = 0;
        flashCounter = 0;
        particles.clear();
    }

    public void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            String line = reader.readLine();
            highScore = (line != null) ? Integer.parseInt(line.trim()) : 0;
        } catch (IOException | NumberFormatException e) {
            highScore = 0;
        }
    }

    public void saveHighScore() {
        if (applesEaten > highScore) {
            highScore = applesEaten;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"))) {
                writer.write(Integer.toString(highScore));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new GradientPaint(0, 0, new Color(20, 20, 60), 0, getHeight(), new Color(0, 0, 0)));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (running) {
            // Blinking apple
            g.setColor((System.currentTimeMillis() / 300) % 2 == 0 ? Color.RED : Color.YELLOW);
            g.fillOval(apple.getX(), apple.getY(), Constants.UNIT_SIZE, Constants.UNIT_SIZE);

            // Draw snake with pulsing head
            for (int i = 0; i < snake.getBodyParts(); i++) {
                if (i == 0) {
                    int greenValue = Math.min(255, 100 + pulse);
                    g.setColor(new Color(0, greenValue, 0));
                } else {
                    g.setColor(rainbowColors[i % rainbowColors.length]);
                }
                g.fillRect(snake.getX()[i], snake.getY()[i], Constants.UNIT_SIZE, Constants.UNIT_SIZE);
            }

            // Draw particles
            java.util.List<Particle> dead = new ArrayList<>();
            for (Particle p : particles) {
                p.update();
                p.draw(g);
                if (!p.isAlive()) dead.add(p);
            }
            particles.removeAll(dead);

            // Score display
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            g.setColor(flashCounter > 0 ? Color.WHITE : Color.YELLOW);
            if (flashCounter > 0) flashCounter--;
            g.drawString("Score: " + applesEaten, 10, 30);
            g.setColor(Color.CYAN);
            g.drawString("High Score: " + highScore, 10, 60);

            if (paused) {
                g.setFont(new Font("Ink Free", Font.BOLD, 60));
                g.setColor(Color.WHITE);
                g.drawString("PAUSED", getWidth() / 3, getHeight() / 2);
            }

        } else {
            gameOver(g);
        }
    }

    public void checkApple() {
        if (snake.getX()[0] == apple.getX() && snake.getY()[0] == apple.getY()) {
            snake.grow();
            applesEaten++;
            apple.generateNewPosition();

            // Create particles
            for (int i = 0; i < 20; i++) particles.add(new Particle(apple.getX(), apple.getY()));

            // Flash score
            flashCounter = 5;

            // Speed increase
            if (applesEaten % 5 == 0 && timer.getDelay() > 20) timer.setDelay(timer.getDelay() - 5);

            // Play sound
            SoundUtils.playSound("eat.wav");
        }
    }

    public void checkCollisions() {
        for (int i = snake.getBodyParts() - 1; i > 0; i--) {
            if (snake.getX()[0] == snake.getX()[i] && snake.getY()[0] == snake.getY()[i]) running = false;
        }

        if (snake.getX()[0] < 0 || snake.getX()[0] >= Constants.SCREEN_WIDTH ||
                snake.getY()[0] < 0 || snake.getY()[0] >= Constants.SCREEN_HEIGHT)
            running = false;

        if (!running) {
            timer.stop();
            saveHighScore();
            SoundUtils.playSound("gameover.wav");
        }
    }

    public void gameOver(Graphics g) {
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        g.setColor(Color.BLACK);
        g.drawString("Game Over", getWidth() / 6 + 5, getHeight() / 2 + 5);
        g.setColor(Color.RED);
        g.drawString("Game Over", getWidth() / 6, getHeight() / 2);

        g.setFont(new Font("Ink Free", Font.BOLD, 35));
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + applesEaten, getWidth() / 3, getHeight() / 2 + 50);
        g.setColor(Color.CYAN);
        g.drawString("High Score: " + highScore, getWidth() / 3, getHeight() / 2 + 90);

        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.setColor(Color.WHITE);
        g.drawString("Press R to Restart", getWidth() / 3, getHeight() / 2 + 150);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused) {
            snake.move();

            if (pulseDirection) pulse += 10; else pulse -= 10;
            if (pulse >= 155) pulseDirection = false;
            if (pulse <= 0) pulseDirection = true;

            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (snake.getDirection() != 'R') snake.setDirection('L');
                }
                case KeyEvent.VK_RIGHT -> {
                    if (snake.getDirection() != 'L') snake.setDirection('R');
                }
                case KeyEvent.VK_UP -> {
                    if (snake.getDirection() != 'D') snake.setDirection('U');
                }
                case KeyEvent.VK_DOWN -> {
                    if (snake.getDirection() != 'U') snake.setDirection('D');
                }
                case KeyEvent.VK_R -> {
                    if (!running) startGame();
                }
                case KeyEvent.VK_SPACE -> paused = !paused; // Pause/resume
            }
        }
    }
}