package tron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Main game panel that handles the game logic, rendering, and player interactions.
 * This panel contains the game board, status bar, and manages the game loop.
 */
public class GamePanel extends JPanel implements ActionListener {
    private static final int CELL_SIZE = 20;
    private static final int STATUS_BAR_HEIGHT = 50;
    private static final int PLAYER_CIRCLE_SIZE = 20;
    private javax.swing.Timer timer;
    private Player player1;
    private Player player2;
    private Level level;
    private Set<Point> trailPlayer1;
    private Set<Point> trailPlayer2;
    private boolean gameOver;
    private String winner;
    private long startTime;
    private GameFrame parentFrame;
    private DatabaseManager dbManager;

    /**
     * Creates a new game panel with the specified players and level configuration.
     * Initializes the game board, players, and UI components.
     *
     * @param parent The parent GameFrame containing this panel
     * @param p1Name Player 1's name
     * @param p1Color Player 1's color
     * @param p2Name Player 2's name
     * @param p2Color Player 2's color
     * @param level The game level to be loaded
     * @param dbManager Database manager for score tracking
     */
    public GamePanel(GameFrame parent, String p1Name, Color p1Color, String p2Name, Color p2Color, Level level, DatabaseManager dbManager) {
        this.parentFrame = parent;
        this.level = level;
        this.dbManager = dbManager;
        
        // Set the panel size to include both the game board and status bar
        int totalWidth = level.getWidth() * CELL_SIZE;
        int totalHeight = level.getHeight() * CELL_SIZE + STATUS_BAR_HEIGHT;
        setPreferredSize(new Dimension(totalWidth, totalHeight));
        
        resetGame(p1Name, p1Color, p2Name, p2Color, level);
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    /**
     * Resets the game state with new player configurations and level.
     * This method can be called to start a new game or restart the current game.
     *
     * @param p1Name Player 1's name
     * @param p1Color Player 1's color
     * @param p2Name Player 2's name
     * @param p2Color Player 2's color
     * @param newLevel The new level to load
     */
    public void resetGame(String p1Name, Color p1Color, String p2Name, Color p2Color, Level newLevel) {
        this.level = newLevel;
        Point p1Start = level.getPlayer1Start();
        Point p2Start = level.getPlayer2Start();
        player1 = new Player(p1Name, p1Color, p1Start, Direction.RIGHT);
        player2 = new Player(p2Name, p2Color, p2Start, Direction.LEFT);

        trailPlayer1 = new HashSet<>();
        trailPlayer2 = new HashSet<>();
        gameOver = false;
        winner = null;
        startTime = System.currentTimeMillis();
        
        if(timer != null) timer.stop();
        timer = new javax.swing.Timer(150, this);
        timer.start();
        
        repaint();
    }

    private void endGame() {
        timer.stop();
        long elapsedTime = System.currentTimeMillis() - startTime;
        
        dbManager.updateScore(winner);
        
        GameOverDialog dialog = new GameOverDialog(
            (Frame)SwingUtilities.getWindowAncestor(this),
            winner,
            elapsedTime,
            dbManager
        );
        dialog.setVisible(true);
        
        if(dialog.isPlayAgain()) {
            StartDialog sd = new StartDialog((GameFrame)SwingUtilities.getWindowAncestor(this));
            sd.setLocationRelativeTo(this);
            sd.setVisible(true);
            if(sd.isConfirmed()) {
                String p1Name = sd.getPlayer1Name();
                String p2Name = sd.getPlayer2Name();
                Color p1Color = sd.getPlayer1Color();
                Color p2Color = sd.getPlayer2Color();
                try {
                    Level newLevel = LevelLoader.loadLevel(sd.getSelectedLevel());
                    resetGame(p1Name, p1Color, p2Name, p2Color, newLevel);
                } catch(IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to load level.");
                }
            } else {
                System.exit(0);
            }
        }
    }

    private void handleKeyPress(KeyEvent e) {
        if(gameOver) return;
        
        int key = e.getKeyCode();
        
        // Player 1 controls (WASD)
        if(key == KeyEvent.VK_W && player1.getDirection() != Direction.DOWN) {
            player1.setDirection(Direction.UP);
        }
        if(key == KeyEvent.VK_S && player1.getDirection() != Direction.UP) {
            player1.setDirection(Direction.DOWN);
        }
        if(key == KeyEvent.VK_A && player1.getDirection() != Direction.RIGHT) {
            player1.setDirection(Direction.LEFT);
        }
        if(key == KeyEvent.VK_D && player1.getDirection() != Direction.LEFT) {
            player1.setDirection(Direction.RIGHT);
        }
        
        // Player 2 controls (Arrow keys)
        if(key == KeyEvent.VK_UP && player2.getDirection() != Direction.DOWN) {
            player2.setDirection(Direction.UP);
        }
        if(key == KeyEvent.VK_DOWN && player2.getDirection() != Direction.UP) {
            player2.setDirection(Direction.DOWN);
        }
        if(key == KeyEvent.VK_LEFT && player2.getDirection() != Direction.RIGHT) {
            player2.setDirection(Direction.LEFT);
        }
        if(key == KeyEvent.VK_RIGHT && player2.getDirection() != Direction.LEFT) {
            player2.setDirection(Direction.RIGHT);
        }
    }

    private void movePlayer(Player player, Set<Point> trail) {
        Point oldPos = player.getPosition();
        Point newPos = new Point(oldPos);
        
        switch(player.getDirection()) {
            case UP: newPos.y--; break;
            case DOWN: newPos.y++; break;
            case LEFT: newPos.x--; break;
            case RIGHT: newPos.x++; break;
        }
        
        player.setPosition(newPos);
        trail.add(new Point(oldPos));  // Add the old position to trail
    }

    private void checkCollisions() {
        Point p1Pos = player1.getPosition();
        Point p2Pos = player2.getPosition();

        // Check wall collisions
        if(level.isWall(p1Pos.x, p1Pos.y)) {
            gameOver = true;
            winner = player2.getName();
        }
        if(level.isWall(p2Pos.x, p2Pos.y)) {
            gameOver = true;
            winner = player1.getName();
        }

        // Check trail collisions (excluding current position)
        if(trailPlayer1.contains(p2Pos) || trailPlayer2.contains(p1Pos)) {
            gameOver = true;
            // Determine winner based on who hit whose trail
            if(trailPlayer1.contains(p2Pos) && trailPlayer2.contains(p1Pos)) {
                winner = "Draw"; // Both hit a trail simultaneously
            } else if(trailPlayer1.contains(p2Pos)) {
                winner = player1.getName();
            } else {
                winner = player2.getName();
            }
        }

        // Head-on collision
        if(p1Pos.equals(p2Pos)) {
            gameOver = true;
            winner = "Draw";
        }

        if(gameOver) {
            endGame();
        }
    }

    /**
     * Handles the game update cycle. Called by the timer to update game state.
     * Moves players, checks for collisions, and updates the display.
     *
     * @param e The action event (not used)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameOver) {
            timer.stop();
            return;
        }

        movePlayer(player1, trailPlayer1);
        movePlayer(player2, trailPlayer2);

        checkCollisions();
        repaint();
    }

    /**
     * Renders the game state including the status bar, game board, walls,
     * player trails, and current player positions.
     * Uses anti-aliasing for smooth graphics.
     *
     * @param g The graphics context to paint on
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the status bar background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), STATUS_BAR_HEIGHT);

        // Draw player 1 info
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString(player1.getName(), 10, STATUS_BAR_HEIGHT/3 + 5);
        
        // Draw player 1 color circle
        g2d.setColor(player1.getColor());
        g2d.fillOval(10 + g2d.getFontMetrics().stringWidth(player1.getName()) + 10, 
                     STATUS_BAR_HEIGHT/3 - PLAYER_CIRCLE_SIZE/2, 
                     PLAYER_CIRCLE_SIZE, PLAYER_CIRCLE_SIZE);

        // Draw player 2 info
        g2d.setColor(Color.WHITE);
        int p2NameWidth = g2d.getFontMetrics().stringWidth(player2.getName());
        g2d.drawString(player2.getName(), getWidth() - p2NameWidth - 40, STATUS_BAR_HEIGHT/3 + 5);
        
        // Draw player 2 color circle
        g2d.setColor(player2.getColor());
        g2d.fillOval(getWidth() - 30, 
                     STATUS_BAR_HEIGHT/3 - PLAYER_CIRCLE_SIZE/2, 
                     PLAYER_CIRCLE_SIZE, PLAYER_CIRCLE_SIZE);

        // Draw current level info
        g2d.setColor(Color.WHITE);
        String levelInfo = "Level: " + level.getName();
        int levelWidth = g2d.getFontMetrics().stringWidth(levelInfo);
        g2d.drawString(levelInfo, (getWidth() - levelWidth) / 2, STATUS_BAR_HEIGHT/3 + 5);

        // Draw elapsed time
        long elapsed = System.currentTimeMillis() - startTime;
        String timeStr = String.format("Time: %ds", elapsed/1000);
        int timeWidth = g2d.getFontMetrics().stringWidth(timeStr);
        g2d.drawString(timeStr, (getWidth() - timeWidth) / 2, STATUS_BAR_HEIGHT * 2/3 + 5);

        // Offset the game board drawing by STATUS_BAR_HEIGHT
        g2d.translate(0, STATUS_BAR_HEIGHT);

        // Draw the game board background
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, level.getWidth() * CELL_SIZE, level.getHeight() * CELL_SIZE);

        // Draw walls
        g2d.setColor(Color.GRAY);
        for (Point wall : level.getWalls()) {
            g2d.fillRect(wall.x * CELL_SIZE, wall.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw player trails with rectangular shapes
        drawTrail(g2d, trailPlayer1, player1.getColor());
        drawTrail(g2d, trailPlayer2, player2.getColor());

        // Draw current positions with round shape
        drawPlayer(g2d, player1);
        drawPlayer(g2d, player2);

        // Reset the translation
        g2d.translate(0, -STATUS_BAR_HEIGHT);
    }

    private void drawTrail(Graphics2D g2d, Set<Point> trail, Color color) {
        g2d.setColor(color);
        for (Point p : trail) {
            g2d.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawPlayer(Graphics2D g2d, Player player) {
        g2d.setColor(player.getColor());
        int x = player.getPosition().x * CELL_SIZE;
        int y = player.getPosition().y * CELL_SIZE;
        g2d.fillOval(x, y, CELL_SIZE, CELL_SIZE);
    }
}
