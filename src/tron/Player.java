package tron;

import java.awt.Color;
import java.awt.Point;

/**
 * Represents a player in the Tron light-cycle game.
 * Maintains the player's state including position, direction, color, and name.
 */
public class Player {
    private String name;
    private Color color;
    private Point position;
    private Point previousPosition;
    private Direction direction;

    /**
     * Creates a new player with specified attributes.
     *
     * @param name The player's display name
     * @param color The color of the player's light-cycle and trail
     * @param start The initial position on the game board
     * @param dir The initial direction of movement
     */
    public Player(String name, Color color, Point start, Direction dir) {
        this.name = name;
        this.color = color;
        this.position = start;
        this.previousPosition = new Point(start);
        this.direction = dir;
    }

    /**
     * @return The player's display name
     */
    public String getName() { return name; }

    /**
     * @return The color of the player's light-cycle and trail
     */
    public Color getColor() { return color; }

    /**
     * @return The current position on the game board
     */
    public Point getPosition() { return position; }

    /**
     * @return The previous position before the last move
     */
    public Point getPreviousPosition() { return previousPosition; }

    /**
     * @return The current direction of movement
     */
    public Direction getDirection() { return direction; }

    /**
     * Updates the player's movement direction.
     * @param d The new direction to move in
     */
    public void setDirection(Direction d) { this.direction = d; }
    
    /**
     * Updates the player's position, storing the current position as previous.
     * @param p The new position on the game board
     */
    public void setPosition(Point p) { 
        this.previousPosition = new Point(this.position);
        this.position = p; 
    }
}
