/*
 * The Snake class represents the snake in the "Hunger Nagini" game.
 * It manages the snake's body, movement, growth, and collision detection.
 */
package hungernagini;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Snake class handles the snake coordinates, growth, movement, and drawing.
 *
 */
public class Snake {

    // A list representing the body of the snake, with each part stored as a SnakeCoordinate (x, y)
    public ArrayList<SnakeCoordinate> body;

    // The current direction the snake is moving: 'U' for up, 'D' for down, 'L' for left, 'R' for right
    public char direction = 'R'; // Default direction is right

    // The current number of body parts in the snake (starts with 1)
    private int bodyParts = 1;

    // Tracks the number of apples eaten by the snake
    public int applesEaten;

    /**
     * Constructor initializes the snake's body with one part at (0, 0).
     */
    public Snake() {
        body = new ArrayList<>();
        body.add(new SnakeCoordinate(0, 0)); // Initial head position
    }

    /**
     * Moves the snake's body and head based on the current direction.
     */
    public void move() {
        // Move each body part to the position of the part in front of it
        for (int i = bodyParts - 1; i > 0; i--) {
            body.get(i).setX(body.get(i - 1).getX());
            body.get(i).setY(body.get(i - 1).getY());
        }

        // Move the snake's head according to the current direction
        SnakeCoordinate head = body.get(0);
        switch (direction) {
            case 'U':
                head.setY(head.getY() - Map.UNIT_SIZE); // Up
                break;
            case 'D':
                head.setY(head.getY() + Map.UNIT_SIZE); // Down
                break;
            case 'L':
                head.setX(head.getX() - Map.UNIT_SIZE); // Left
                break;
            case 'R':
                head.setX(head.getX() + Map.UNIT_SIZE); // Right
                break;
        }
    }

    /**
     * Draws the snake on the screen. The head is drawn as a green pentagon, and
     * the body is a darker green color.
     *
     * @param g the Graphics object used for drawing.
     */
    public void draw(Graphics g) {
        for (int i = 0; i < body.size(); i++) {
            if (i == 0) {
                g.setColor(Color.GREEN); // The snake's head is green
                int xCenter = body.get(i).getX() + Map.UNIT_SIZE / 2; // Center x of pentagon
                int yCenter = body.get(i).getY() + Map.UNIT_SIZE / 2; // Center y of pentagon
                int radius = Map.UNIT_SIZE / 2; // Radius for pentagon

                // Calculate points for a pentagon
                int[] xPoints = new int[5];
                int[] yPoints = new int[5];
                double angleOffset = 0; // Default rotation angle
                switch (direction) {
                    case 'U':
                        angleOffset = -Math.PI / 2;
                        break; // Up
                    case 'D':
                        angleOffset = Math.PI / 2;
                        break; // Down
                    case 'L':
                        angleOffset = Math.PI;
                        break; // Left
                    case 'R':
                        angleOffset = 0;
                        break; // Right
                }

                for (int j = 0; j < 5; j++) {
                    double angle = 2 * Math.PI / 5 * j + angleOffset; // Calculate angle for each point
                    xPoints[j] = (int) (xCenter + radius * Math.cos(angle)); // Calculate x coordinate
                    yPoints[j] = (int) (yCenter + radius * Math.sin(angle)); // Calculate y coordinate
                }

                // Draw the head as a pentagon
                g.fillPolygon(xPoints, yPoints, 5);
            } else {
                // Random color generation for body parts
                Random random = new Random();
                int red = random.nextInt(100); // Red color range: 0 - 99
                int green = random.nextInt(100) + 155; // Green color range: 155 - 255
                int blue = random.nextInt(100); // Blue color range: 0 - 99
                g.setColor(new Color(red, green, blue)); // Set color for body parts
                // Draw each body part as an oval of size Map.UNIT_SIZE
                g.fillOval(body.get(i).getX(), body.get(i).getY(), Map.UNIT_SIZE, Map.UNIT_SIZE);
            }
        }
    }

    /**
     * Grows the snake by adding a new body part. The new part is added at a
     * calculated position based on the current direction.
     */
    public void grow() {
        // Get the coordinates of the last part of the snake
        SnakeCoordinate lastPart = body.get(body.size() - 1);

        int newX = lastPart.getX();
        int newY = lastPart.getY();

        // Adjust new part coordinates based on the direction
        switch (direction) {
            case 'U':
                newY += Map.UNIT_SIZE; // Add below if moving up
                break;
            case 'D':
                newY -= Map.UNIT_SIZE; // Add above if moving down
                break;
            case 'L':
                newX += Map.UNIT_SIZE; // Add to the right if moving left
                break;
            case 'R':
                newX -= Map.UNIT_SIZE; // Add to the left if moving right
                break;
        }

        body.add(new SnakeCoordinate(newX, newY)); // Add new part
        applesEaten++; // Increment apples eaten
        bodyParts++; // Increase body length
    }

    /**
     * Checks for collisions with the snake own body or the walls.
     *
     * @return true if no collision occurs, false if a collision is detected.
     */
    public boolean checkCollisions() {
        SnakeCoordinate head = body.get(0); // Head of the snake

        // Check for self-collision
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return false; // Collision with itself
            }
        }

        // Check for wall collisions (out of bounds)
        if (head.getX() < 0 || head.getX() >= Main.SCREEN_WIDTH
                || head.getY() < 0 || head.getY() >= Main.SCREEN_HEIGHT) {
            return false; // Collision with wall
        }

        return true; // No collision
    }

    /**
     * Checks if the snake's head has collided with an apple. If a collision
     * occurs, the snake grows and a new apple is generated.
     *
     * @param apple the Apple object to check for collision with.
     */
    public void checkApple(Apple apple) {
        SnakeCoordinate head = body.get(0); // Head of the snake

        // Check if the head is at the same position as the apple
        if (head.getX() == apple.getX() && head.getY() == apple.getY()) {
            grow(); // Snake grows
            apple.newApple(); // Generate a new apple
        }
    }
}
