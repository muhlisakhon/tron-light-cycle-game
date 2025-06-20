package tron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Initial dialog shown when starting the game.
 * Allows players to enter their names, choose colors, and select a level.
 */
public class StartDialog extends JDialog {
    private JTextField player1NameField, player2NameField;
    private JButton player1ColorBtn, player2ColorBtn;
    private JComboBox<String> levelCombo;
    private Color player1Color = Color.BLUE;
    private Color player2Color = Color.RED;
    private boolean confirmed = false;

    /**
     * Creates a new start dialog for game configuration.
     *
     * @param owner The parent frame (game window)
     */
    public StartDialog(Frame owner) {
        super(owner, "Start Game", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        panel.add(new JLabel("Player 1 Name:"));
        player1NameField = new JTextField("Player1");
        panel.add(player1NameField);

        panel.add(new JLabel("Player 1 Color:"));
        player1ColorBtn = new JButton("Choose...");
        player1ColorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choose Player 1 Color", player1Color);
            if(c != null) player1Color = c;
        });
        panel.add(player1ColorBtn);

        panel.add(new JLabel("Player 2 Name:"));
        player2NameField = new JTextField("Player2");
        panel.add(player2NameField);

        panel.add(new JLabel("Player 2 Color:"));
        player2ColorBtn = new JButton("Choose...");
        player2ColorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choose Player 2 Color", player2Color);
            if(c != null) player2Color = c;
        });
        panel.add(player2ColorBtn);

        panel.add(new JLabel("Select Level:"));
        File levelDir = new File("levels");
        String[] files = levelDir.list((dir, name) -> name.endsWith(".txt"));
        if (files == null) files = new String[0];
        ArrayList<String> levels = new ArrayList<>();
        for(String f : files) {
            levels.add("levels/" + f);
        }
        levelCombo = new JComboBox<>(levels.toArray(new String[0]));
        if(levels.size() > 0) levelCombo.setSelectedIndex(0);
        panel.add(levelCombo);

        add(panel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(e -> {
            if(player1NameField.getText().trim().isEmpty()) {
                player1NameField.setText("Player1");
            }
            if(player2NameField.getText().trim().isEmpty()) {
                player2NameField.setText("Player2");
            }
            confirmed = true;
            dispose();
        });
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        bottom.add(okBtn);
        bottom.add(cancelBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * @return true if the player confirmed the settings, false if cancelled
     */
    public boolean isConfirmed() { 
        return confirmed; 
    }

    /**
     * @return The name entered for player 1
     */
    public String getPlayer1Name() { 
        return player1NameField.getText().trim(); 
    }

    /**
     * @return The name entered for player 2
     */
    public String getPlayer2Name() { 
        return player2NameField.getText().trim(); 
    }

    /**
     * @return The color chosen for player 1
     */
    public Color getPlayer1Color() { 
        return player1Color; 
    }

    /**
     * @return The color chosen for player 2
     */
    public Color getPlayer2Color() { 
        return player2Color; 
    }

    /**
     * @return The path to the selected level file
     */
    public String getSelectedLevel() {
        return (String)levelCombo.getSelectedItem();
    }
}
