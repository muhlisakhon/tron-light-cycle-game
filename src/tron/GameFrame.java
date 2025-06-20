package tron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * The main game window that contains the game panel and manages the game life cycle.
 * Handles window initialization, game setup, and coordinates between different game components.
 */
public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private DatabaseManager dbManager;
    private String currentLevelPath;
    private String player1Name;
    private String player2Name;
    private Color player1Color;
    private Color player2Color;
    private Level currentLevel;

    /**
     * Creates and initializes the main game window.
     * Sets up the window properties and starts the game setup process.
     */
    public GameFrame() {
        setTitle("Tron Light-Cycle Battle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        dbManager = new DatabaseManager();

        while (true) {
            // Show start dialog
            StartDialog startDialog = new StartDialog(this);
            startDialog.setLocationRelativeTo(null);  // Center the start dialog
            startDialog.setVisible(true);

            // If user cancels the dialog, exit the game
            if(!startDialog.isConfirmed()) {
                System.exit(0);
            }

            player1Name = startDialog.getPlayer1Name();
            player2Name = startDialog.getPlayer2Name();
            player1Color = startDialog.getPlayer1Color();
            player2Color = startDialog.getPlayer2Color();
            currentLevelPath = startDialog.getSelectedLevel();

            // Attempt to load the selected level
            Level attemptedLevel = null;
            try {
                attemptedLevel = LevelLoader.loadLevel(currentLevelPath);
            } catch(IOException e) {
                // If failed to load, show error and prompt again
                JOptionPane.showMessageDialog(this, 
                    "Failed to load the level. Please select another level.",
                    "Load Error", 
                    JOptionPane.ERROR_MESSAGE);
                continue; // Go back to StartDialog
            }

            // Check if the loaded level is valid
            if (attemptedLevel == null || attemptedLevel.getWidth() == 0 || attemptedLevel.getHeight() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "This level file is empty or invalid. Please select another level.", 
                    "Invalid Level", 
                    JOptionPane.ERROR_MESSAGE);
                continue; // Go back to StartDialog
            }

            // If we reach here, the level is valid, break out of the loop
            currentLevel = attemptedLevel;
            break;
        }

        // Create the game panel with the valid level
        gamePanel = new GamePanel(this, player1Name, player1Color, player2Name, player2Color, currentLevel, dbManager);
        add(gamePanel);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(null);  // Center the game window
    }

    /**
     * Returns the database manager instance used by the game.
     *
     * @return the database manager instance
     */
    public DatabaseManager getDbManager() {
        return dbManager;
    }

    /**
     * The main entry point of the application.
     * Creates and displays the game window.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameFrame().setVisible(true);
        });
    }
}
