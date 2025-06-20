package tron;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.IOException;

/**
 * Dialog shown when a game ends, displaying the winner and game statistics.
 * Provides options to play again or exit the game.
 */
public class GameOverDialog extends JDialog {
    private boolean playAgain = false;
    private boolean showHighScores = false;
    private DatabaseManager dbManager;

    /**
     * Creates a new game over dialog with the specified winner and game duration.
     *
     * @param owner The parent frame (game window)
     * @param winner The name of the winning player or "Draw"
     * @param gameTime The duration of the game in milliseconds
     * @param dbManager Database manager for updating scores
     */
    public GameOverDialog(Frame owner, String winner, long gameTime, DatabaseManager dbManager) {
        super(owner, "Game Over", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create congratulations panel
        JPanel congratsPanel = new JPanel(new BorderLayout(10, 10));
        congratsPanel.setBackground(new Color(240, 240, 240));
        congratsPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Trophy icon panel
        JPanel trophyPanel = new TrophyPanel();
        trophyPanel.setPreferredSize(new Dimension(64, 64));
        congratsPanel.add(trophyPanel, BorderLayout.WEST);

        // Message panel
        JPanel messagePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        messagePanel.setOpaque(false);
        
        JLabel congratsLabel = new JLabel("Congratulations!");
        congratsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        congratsLabel.setForeground(new Color(51, 51, 51));
        
        JLabel winnerLabel = new JLabel(winner + " wins!");
        winnerLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        messagePanel.add(congratsLabel);
        messagePanel.add(winnerLabel);
        congratsPanel.add(messagePanel, BorderLayout.CENTER);

        mainPanel.add(congratsPanel, BorderLayout.NORTH);

        // Game time
        JLabel timeLabel = new JLabel(String.format("Time: %d seconds", gameTime/1000));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(timeLabel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton playAgainButton = new JButton("Play Again");
        JButton highScoresButton = new JButton("High Scores");
        JButton exitButton = new JButton("Exit");

        playAgainButton.addActionListener(e -> {
            playAgain = true;
            dispose();
        });

        highScoresButton.addActionListener(e -> {
            showHighScores = true;
            HighScoreDialog dialog = new HighScoreDialog(owner, dbManager);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        exitButton.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        buttonPanel.add(playAgainButton);
        buttonPanel.add(highScoresButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(owner);
    }

    private class TrophyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            
            // Trophy color
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(255, 215, 0),
                w, h, new Color(218, 165, 32)
            );
            g2d.setPaint(gradient);

            // Draw cup
            int cupWidth = w * 3/4;
            int cupHeight = h * 2/3;
            int x = (w - cupWidth) / 2;
            
            // Cup top (arc)
            g2d.fillArc(x, 0, cupWidth, cupHeight/2, 0, 180);
            
            // Cup body
            g2d.fillRect(x + cupWidth/4, cupHeight/4, cupWidth/2, cupHeight/2);
            
            // Base
            int baseWidth = cupWidth * 2/3;
            int baseHeight = h/6;
            int baseX = (w - baseWidth) / 2;
            int baseY = h - baseHeight;
            
            // Stem
            g2d.fillRect(x + cupWidth/3, cupHeight * 3/4, cupWidth/3, baseY - cupHeight * 3/4);
            
            // Base rectangle
            g2d.fillRect(baseX, baseY, baseWidth, baseHeight);

            // Handles
            g2d.setStroke(new BasicStroke(3));
            g2d.drawArc(x - 5, cupHeight/4, cupWidth/3, cupHeight/3, 90, 180);
            g2d.drawArc(x + cupWidth - cupWidth/3 + 5, cupHeight/4, cupWidth/3, cupHeight/3, -90, 180);
        }
    }

    /**
     * Checks if the player wants to play another game.
     *
     * @return true if the player clicked "Play Again", false otherwise
     */
    public boolean isPlayAgain() {
        return playAgain;
    }

    /**
     * Checks if the player wants to view high scores.
     *
     * @return true if the player clicked "High Scores", false otherwise
     */
    public boolean isShowHighScores() {
        return showHighScores;
    }
}
