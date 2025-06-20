package tron;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog that displays the high scores table.
 * Shows player names and their win counts in descending order.
 */
public class HighScoreDialog extends JDialog {
    /**
     * Creates a new high score dialog displaying the top players.
     *
     * @param owner The parent frame (game window)
     * @param db Database manager to retrieve scores from
     */
    public HighScoreDialog(Frame owner, DatabaseManager db) {
        super(owner, "High Scores", true);
        setSize(300, 400);
        setLocationRelativeTo(owner);

        // Get top 10 scores
        List<DatabaseManager.PlayerScore> scores = db.getTopScores(10);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title at the top
        JLabel titleLabel = new JLabel("Top 10 Players", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Scores panel
        JPanel scoresPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        scoresPanel.setBorder(new CompoundBorder(
            new EmptyBorder(10, 0, 10, 0),
            new EtchedBorder()
        ));

        // Header row
        JLabel rankHeader = new JLabel("Player", SwingConstants.CENTER);
        JLabel scoreHeader = new JLabel("Wins", SwingConstants.CENTER);
        rankHeader.setFont(new Font("Arial", Font.BOLD, 14));
        scoreHeader.setFont(new Font("Arial", Font.BOLD, 14));
        scoresPanel.add(rankHeader);
        scoresPanel.add(scoreHeader);

        // Add scores
        for(DatabaseManager.PlayerScore sc : scores) {
            JLabel nameLabel = new JLabel(sc.name, SwingConstants.CENTER);
            JLabel winsLabel = new JLabel(String.valueOf(sc.wins), SwingConstants.CENTER);
            
            nameLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            winsLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            
            scoresPanel.add(nameLabel);
            scoresPanel.add(winsLabel);
        }

        // If less than 10 scores, fill with empty rows
        for(int i = scores.size(); i < 10; i++) {
            scoresPanel.add(new JLabel("-", SwingConstants.CENTER));
            scoresPanel.add(new JLabel("-", SwingConstants.CENTER));
        }

        JScrollPane scrollPane = new JScrollPane(scoresPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with close button
        JPanel bottomPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setResizable(false);
    }
}
