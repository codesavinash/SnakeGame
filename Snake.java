public class Snake {
    private int[] x;
    private int[] y;
    private int bodyParts;
    private char direction;

    public Snake(int maxUnits, int initialLength) {
        x = new int[maxUnits];
        y = new int[maxUnits];
        bodyParts = initialLength;
        direction = 'R'; // Start moving right

        // Initialize snake position
        for (int i = 0; i < bodyParts; i++) {
            x[i] = Constants.UNIT_SIZE * (bodyParts - i - 1);
            y[i] = 0;
        }
    }

    public void move() {
        for (int i = bodyParts - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] -= Constants.UNIT_SIZE;
            case 'D' -> y[0] += Constants.UNIT_SIZE;
            case 'L' -> x[0] -= Constants.UNIT_SIZE;
            case 'R' -> x[0] += Constants.UNIT_SIZE;
        }
    }

    public void grow() {
        bodyParts++;
    }

    public char getDirection() { return direction; }
    public void setDirection(char direction) { this.direction = direction; }
    public int[] getX() { return x; }
    public int[] getY() { return y; }
    public int getBodyParts() { return bodyParts; }
}