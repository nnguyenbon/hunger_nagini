package hungernagini;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

/**
 * Main class for the Hunger Nagini game. Handles game states, UI, and key
 * inputs.
 *
 * @author Nguyen Kim Bao Nguyen
 */
public class Main extends JPanel implements ActionListener {

    // Constants for screen dimensions and game speed
    public static final int SCREEN_WIDTH = 640;
    public static final int SCREEN_HEIGHT = 480;
    public static final int DELAY = 100;

    // Game objects and states
    Snake snake;  // Snake object
    Apple apple;  // Apple object, linked to the snake
    Map map;      // Map object
    Timer timer;  // Controls game loop timing
    boolean running;  // Indicates if the game is running
    private boolean inMenu = true; // True if game is in menu state
    private boolean inSA = false;  // True if game is in "Showing About" state
    boolean gameOver = false;      // Tracks game-over state

    /**
     * Constructor to set up the game panel
     */
    public Main() {
        this.setBackground(Color.BLACK);   // Set background color
        this.setOpaque(true);              // Ensure correct background drawing
        this.addKeyListener(new MyKeyAdapter()); // Add KeyListener for key inputs
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // Set panel size
    }

    /**
     * Custom painting method that handles drawing different screens based on
     * the game state. This method determines what to render—menu, game, "About"
     * screen, or game over.
     *
     * @param g The Graphics object used for drawing on the panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Ensures the component is properly cleared before custom painting

        if (inMenu) {
            drawMenu(g);  // Display the main menu if in "menu" state
        } else if (inSA) {
            drawAbout(g); // Show the "About" screen if in "About" state
        } else {
            if (running) {
                map.drawGrid(g);  // Render the map grid for visual structure
                snake.draw(g);    // Draw the snake on the screen
                apple.draw(g);    // Display the apple for the snake to eat

                // Display the current score
                g.setColor(Color.white);
                g.setFont(new Font("Ink Free", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + snake.applesEaten,
                        (SCREEN_WIDTH - metrics.stringWidth("Score: " + snake.applesEaten)) / 2,
                        g.getFont().getSize());
            } else {
                gameOver(g);  // Show the game over screen when not running
            }
        }
    }

    /**
     * Sets up the main menu screen by adjusting game state flags and triggering
     * a repaint to display the menu options.
     */
    public void Menu() {
        inMenu = true;    // Set the game state to "menu" mode
        running = false;  // Ensure the game is not running while in the menu
        inSA = false;     // Set 'inSA' (in "About" screen) to false, as we're now in the menu

        repaint();        // Repaint the panel to display the menu screen
    }

    /**
     * Draws the main menu screen with game title and options for the user.
     *
     * @param g Graphics object used to draw text and shapes on the panel
     */
    private void drawMenu(Graphics g) {
        // Set color and font for the game title
        g.setColor(Color.RED);
        g.setFont(new Font("Segoe Script", Font.BOLD, 70));
        g.drawString("Hunger Nagini", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Hunger Nagini")) / 2, SCREEN_HEIGHT / 2 - 100);

        // Set color and font for the menu options
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));

        // Draw menu options: "Play", "About", and "Quit", centered on the screen
        g.drawString("Press 1 to Play", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 1 to Play")) / 2, SCREEN_HEIGHT / 2);
        g.drawString("Press 2 for About", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 2 for About")) / 2, SCREEN_HEIGHT / 2 + 70);
        g.drawString("Press 3 to Quit", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 3 to Quit")) / 2, SCREEN_HEIGHT / 2 + 140);
    }

    /**
     * Initializes and starts the game
     */
    public void startGame() {
        map = new Map();           // Create new map
        snake = new Snake();       // Create new snake
        apple = new Apple(snake);  // Create new apple for the snake
        apple.newApple();          // Generate a new apple
        running = true;            // Set game state to running
        inMenu = false;            // Set state to in-game
        timer = new Timer(DELAY, this); // Initialize timer
        timer.start();             // Start timer
    }

    /**
     * Draws the game-over screen
     */
    public void gameOver(Graphics g) {
        gameOver = true; // Set the gameOver state to true

        // Set up font and color for the score display
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont()); // Metrics for centering the score text

        // Display the player's score in the center of the screen, slightly below halfway
        g.drawString("Score: " + snake.applesEaten,
                (SCREEN_WIDTH - metrics1.stringWidth("Score: " + snake.applesEaten)) / 2,
                (SCREEN_HEIGHT + 100) / 2);

        // Set up font and color for the "GAME OVER" message
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont()); // Metrics for centering the "GAME OVER" text

        // Display the "GAME OVER" message at the center of the screen
        g.drawString("GAME OVER",
                (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER")) / 2,
                SCREEN_HEIGHT / 2);

        // Set up font and color for replay and home options
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 20));

        // Display "Press 1 to RePlay" option below the "GAME OVER" text
        g.drawString("Press 1 to RePlay",
                (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 1 to RePlay")) / 2,
                SCREEN_HEIGHT / 2 + 100);

        // Display "Press 4 to get back Home" option below the replay option
        g.drawString("Press 4 to get back Home",
                (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 4 to get back Home")) / 2,
                SCREEN_HEIGHT / 2 + 140);
    }

    /**
     * Invoked periodically by the Timer to update the game state. Moves the
     * snake, checks for apple consumption, and handles collisions.
     *
     * @param e ActionEvent triggered by the Timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {  // If the game is currently active
            snake.move();            // Move the snake in its current direction
            snake.checkApple(apple); // Check if the snake has eaten the apple

            // Check for collisions with walls or itself
            if (!snake.checkCollisions()) {
                running = false;  // End the game if a collision occurs
                timer.stop();     // Stop the game timer
                gameOver = true;  // Set the gameOver state to true for display
            }
        }
        repaint(); // Repaint the screen to update game visuals
    }

    /**
     * Key adapter class for handling key events
     */
    public class MyKeyAdapter extends KeyAdapter {

        private boolean keyPressedFlag = false; // Track key press state

        @Override
        public void keyPressed(KeyEvent e) {
            if (keyPressedFlag && !inMenu) {
                return; // Ignore if already pressed outside menu
            }
            int keyCode = e.getKeyCode();

            if (inMenu) {
                handleMenu(keyCode); // Handle menu choices
            } else if (inSA) {
                if (keyCode == KeyEvent.VK_4) {
                    Menu(); // Return to menu from "About"
                }
            } else if (gameOver && keyCode == KeyEvent.VK_4) {
                Menu(); // Return to menu on game over
                gameOver = false;
            } else if (!running && !inMenu) {
                if (keyCode == KeyEvent.VK_1) {
                    startGame(); // Restart game
                }
            } else if (running) {
                handleGameControls(keyCode); // Handle game controls
            }
        }

        // Handle menu selection keys
        private void handleMenu(int keyCode) {
            switch (keyCode) {
                case KeyEvent.VK_1:
                    startGame();
                    break;
                case KeyEvent.VK_2:
                    showAbout();
                    break;
                case KeyEvent.VK_3:
                    System.exit(0);
                    break;
                case KeyEvent.VK_4:
                    Menu();
                    break;
            }
        }

        // Define a variable to track the last key press time
        private long lastKeyPressTime = 0;

        // Handle snake movement keys
        private void handleGameControls(int keyCode) {
            long currentTime = System.currentTimeMillis();

            // Check if the last key press was over 100 ms ago
            if (currentTime - lastKeyPressTime < 100) {
                return; // Ignore the key press if the delay has not passed
            }

            // Update last key press time
            lastKeyPressTime = currentTime;

            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    if (snake.direction != 'R') {
                        snake.direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snake.direction != 'L') {
                        snake.direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (snake.direction != 'D') {
                        snake.direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snake.direction != 'U') {
                        snake.direction = 'D';
                    }
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyPressedFlag = false;
        }
    }

    /**
     * Sets up the game state to display the "About" screen.
     */
    private void showAbout() {
        inSA = true;     // Sets the state to "Showing About"
        inMenu = false;  // Exits the menu state
        running = false; // Stops the game from running
        repaint();       // Triggers repaint to display the "About" screen
    }

    /**
     * Draws the "About" screen with information about the game and author
     * names.
     *
     * @param g Graphics object used to draw text and shapes on the panel
     */
    public void drawAbout(Graphics g) {
        String s1 = "About The Game";  // Title text
        String[] lines = {
            "This is a classic Snake Game, where you guide the snake",
            "to eat apples and grow longer."
        }; // Game description lines
        String s3 = "Our authors:"; // Subtitle for authors
        String[] name = {
            "Nguyen Kim Bao Nguyen", "Nguyen Dao Thu Ngan", "Nguyen Quang Huy",
            "Vo Chi Trong", "Lim The Toan", "Thai Hoang Chuong"
        }; // Array of author names
        int a = -25;          // Initial vertical offset for text positioning
        int lineHeight = 25;  // Vertical distance between lines of text

        setBackground(Color.BLACK); // Set the background color for the panel
        g.setColor(Color.RED);      // Set color for the title

        // Draw the title
        g.setFont(new Font("Helvetica", Font.BOLD, 60));
        g.drawString(s1, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(s1)) / 2, 90);

        g.setColor(Color.WHITE); // Set color for text content
        g.setFont(new Font("Helvetica", Font.PLAIN, 18));

        // Draw each line of the game description
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], (SCREEN_WIDTH - g.getFontMetrics().stringWidth(lines[i])) / 2, 130 + i * lineHeight);
        }

        // Draw "Our authors:" subtitle
        g.drawString(s3, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(s3)) / 2, SCREEN_HEIGHT / 2 + 2 * a);

        // Draw each author's name
        for (String n : name) {
            g.drawString(n, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(n)) / 2, SCREEN_HEIGHT / 2 + a);
            a += 25;
        }

        // Draw instruction to return to the main menu
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        g.drawString("Press 4 to get back Home", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 4 to get back Home")) / 2, SCREEN_HEIGHT / 2 + 140);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hunger Nagini"); // Create the main application window titled "Hunger Nagini"
        Main main = new Main();                       // Instantiate the Main game panel
        frame.add(main);                              // Add the game panel to the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application when the frame is closed
        frame.setResizable(false);                     // Prevent the frame from being resized
        frame.pack();                                  // Adjust the frame size to fit the content
        frame.setLocationRelativeTo(null);             // Center the frame on the screen
        frame.setVisible(true);                        // Make the frame visible
        main.setFocusable(true);                       // Allow the game panel to receive keyboard focus
        main.requestFocusInWindow();                   // Request focus for the game panel to ensure it can capture input
        main.Menu();                                   // Display the main menu when the game starts
    }
}
