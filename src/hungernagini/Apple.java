/*
 * The Apple class represents the apple in the "Hunger Nagini" game.
 * Each apple is positioned randomly on the game grid, avoiding any overlap with the snake's body.
 * The apple is drawn on the screen as a red oval.
 */

package hungernagini;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Apple holds the x and y coordinates for an apple's position on the map.
 * The apple's location is randomly generated and does not overlap with the snake.
 */
public class Apple {

    private int x; // x-coordinate of the apple on the map
    private int y; // y-coordinate of the apple on the map
    private Snake snake; // Reference to the Snake object, used to avoid apple spawning on snake
    private Random random = new Random(); // Random object to generate random coordinates

    /**
     * Constructor that takes a Snake object to avoid overlap.
     * Calls newApple() to generate the initial position of the apple.
     */
    public Apple(Snake snake) {
        this.snake = snake;
        newApple(); // Generate the apple's initial position
    }

    /**
     * Generates a new position for the apple, ensuring it doesn't overlap with the snake.
     * Loops until a position not on the snake is found.
     */
    public void newApple() {
        boolean appleOnSnake;
        
        do {
            // Randomly generate x and y coordinates, ensuring they fall within screen limits
            x = random.nextInt(Main.SCREEN_WIDTH / Map.UNIT_SIZE - 1) * Map.UNIT_SIZE;
            y = random.nextInt(Main.SCREEN_HEIGHT / Map.UNIT_SIZE - 1) * Map.UNIT_SIZE;
            
            // Check if the generated position is on the snake
            appleOnSnake = !checkAppleCordinate(snake.body);
        } while (appleOnSnake); // Repeat if the apple is on the snake
    }

    /**
     * Checks if the apple's coordinates overlap with any segment of the snake.
     * @param list the list of snake segments to check against
     * @return true if the apple's coordinates are not on the snake, false otherwise
     */
    public boolean checkAppleCordinate(ArrayList<SnakeCoordinate> list) {
        for (SnakeCoordinate point : list) {
            // Return false if apple's coordinates overlap with any snake segment
            if (point.getX() == x && point.getY() == y) {
                return false;
            }
        }
        return true;
    }

    /**
     * Draws the apple on the screen as a red oval.
     * @param g the Graphics object used to draw the apple
     */
    public void draw(Graphics g) {
        g.setColor(Color.RED); // Set color to red
        g.fillOval(x, y, Map.UNIT_SIZE, Map.UNIT_SIZE); // Draw the apple as a filled oval
    }

    /**
     * Getter for the x-coordinate of the apple.
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for the y-coordinate of the apple.
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
}
