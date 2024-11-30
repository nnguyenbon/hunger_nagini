/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hungernagini;

import java.awt.*;
import javax.swing.*;

/**
 *
 */
// Map class which extends JPanel to handle drawing of the game
public class Map extends JPanel {

    public static final int UNIT_SIZE = 20;

    public Map() {
        this.setBackground(Color.BLACK); // Set background color for the map
    }

    // Draws the grid on the map
    public void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.DARK_GRAY);

        // Draw vertical lines
        for (int i = 0; i < Main.SCREEN_WIDTH / UNIT_SIZE; i++) {
            g2d.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, Main.SCREEN_HEIGHT);
        }

        // Draw horizontal lines
        for (int i = 0; i < Main.SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g2d.drawLine(0, i * UNIT_SIZE, Main.SCREEN_WIDTH, i * UNIT_SIZE);
        }
        
    }

}
