import javax.swing.*;
import java.awt.*;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        GamePanel1 panel = new GamePanel1();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close button works
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
        frame.setResizable(true); // Allow minimize/maximize/resize

        frame.add(panel);
        frame.setVisible(true);
        panel.requestFocusInWindow(); // Make sure key events work
    }
}