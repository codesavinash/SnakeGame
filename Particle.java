import java.awt.*;
import java.util.Random;

public class Particle {
    private int x, y;
    private int vx, vy;
    private int life = 30;
    private Random random = new Random();

    public Particle(int startX, int startY) {
        x = startX;
        y = startY;
        vx = random.nextInt(7) - 3;
        vy = random.nextInt(7) - 3;
    }

    public void update() {
        x += vx;
        y += vy;
        life--;
    }

    public boolean isAlive() { return life > 0; }

    public void draw(Graphics g) {
        g.setColor(new Color(255, 255, 0, Math.max(0, life * 8)));
        g.fillOval(x, y, 5, 5);
    }
}