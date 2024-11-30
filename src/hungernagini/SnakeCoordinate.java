/*
 * This class represents the coordinates of the snake's segments in the "Hunger Nagini" game.
 * Each instance of SnakeCoordinate stores an x and y position on the map grid.
 */

package hungernagini;

/**
 * SnakeCoordinate holds the x and y coordinates for a segment of the snake.
 * It also provides utility methods for setting, getting, and comparing coordinates.
 */
public class SnakeCoordinate {
    private int x; // x-coordinate on the map
    private int y; // y-coordinate on the map

    /**
     * Constructor to initialize x and y coordinates.
     * Divides coordinates by Map.UNIT_SIZE to align with grid-based units.
     * Prints out the grid-based position for debugging.
     */
    public SnakeCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x-coordinate.
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Setter for x-coordinate.
     * @param x the new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter for y-coordinate.
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for y-coordinate.
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Compares this coordinate with another to check if they are the same.
     * Overrides Object's equals method to perform coordinate-based comparison.
     * @param obj the object to compare with this coordinate
     * @return true if both coordinates have the same x and y values
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SnakeCoordinate) {
            SnakeCoordinate other = (SnakeCoordinate) obj;
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }
}
