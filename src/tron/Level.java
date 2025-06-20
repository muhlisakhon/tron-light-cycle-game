package tron;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game level in the Tron light-cycle game.
 * Contains the level layout, wall positions, and starting positions for both players.
 */
public class Level {
    private int width;
    private int height;
    private char[][] map; // '#' for wall, ' ' for empty
    private Point player1Start;
    private Point player2Start;
    private String name;

    /**
     * Creates a new level with specified map and player starting positions.
     *
     * @param map 2D char array representing the level layout ('#' for walls)
     * @param p1Start Starting position for player 1
     * @param p2Start Starting position for player 2
     */
    public Level(char[][] map, Point p1Start, Point p2Start) {
        this.map = map;
        this.height = map.length;
        this.width = map[0].length;
        this.player1Start = p1Start;
        this.player2Start = p2Start;
        this.name = "Classic Arena"; 
    }

    /**
     * Creates a new level with specified map, player starting positions, and level name.
     *
     * @param map 2D char array representing the level layout ('#' for walls)
     * @param p1Start Starting position for player 1
     * @param p2Start Starting position for player 2
     * @param name The name of the level
     */
    public Level(char[][] map, Point p1Start, Point p2Start, String name) {
        this(map, p1Start, p2Start);
        this.name = name;
    }

    /**
     * @return The width of the level in cells
     */
    public int getWidth() { return width; }

    /**
     * @return The height of the level in cells
     */
    public int getHeight() { return height; }

    /**
     * Checks if a given position contains a wall.
     * Positions outside the level boundaries are considered walls.
     *
     * @param x X-coordinate to check
     * @param y Y-coordinate to check
     * @return true if position contains a wall or is outside boundaries
     */
    public boolean isWall(int x, int y) {
        if(x < 0 || y < 0 || x >= width || y >= height) return true;
        return map[y][x] == '#';
    }

    /**
     * @return The starting position for player 1
     */
    public Point getPlayer1Start() {
        return player1Start;
    }

    /**
     * @return The starting position for player 2
     */
    public Point getPlayer2Start() {
        return player2Start;
    }

    /**
     * @return The name of the level
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a list of all wall positions in the level.
     * @return List of Points representing wall positions
     */
    public List<Point> getWalls() {
        List<Point> walls = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map[y][x] == '#') {
                    walls.add(new Point(x, y));
                }
            }
        }
        return walls;
    }
}
