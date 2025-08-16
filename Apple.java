import java.util.Random;

public class Apple {
    private int x;
    private int y;
    private Random random;

    public Apple() {
        random = new Random();
        generateNewPosition();
    }

    public void generateNewPosition() {
        x = random.nextInt(Constants.SCREEN_WIDTH / Constants.UNIT_SIZE) * Constants.UNIT_SIZE;
        y = random.nextInt(Constants.SCREEN_HEIGHT / Constants.UNIT_SIZE) * Constants.UNIT_SIZE;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}