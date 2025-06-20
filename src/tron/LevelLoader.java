package tron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Point;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for loading game levels from files.
 * Handles parsing level files and creating Level objects.
 */
public class LevelLoader {

    /**
     * Loads a level from a file path.
     * The file should contain the level layout with:
     * '#' for walls
     * '1' for player 1 start position
     * '2' for player 2 start position
     * ' ' for empty spaces
     *
     * @param filePath Path to the level file
     * @return A new Level object representing the loaded level
     * @throws IOException if there's an error reading the file
     */
    public static Level loadLevel(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        java.util.List<String> lines = new java.util.ArrayList<>();
        while((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();

        int height = lines.size();
        int width = lines.get(0).length();
        char[][] map = new char[height][width];
        Point p1Start = null;
        Point p2Start = null;

        for(int y=0; y<height; y++) {
            String row = lines.get(y);
            for(int x=0; x<width; x++) {
                char c = row.charAt(x);
                map[y][x] = c;
                if(c == '1') {
                    p1Start = new Point(x, y);
                    // Replace '1' with ' ' since it's not a wall
                    map[y][x] = ' ';
                } else if(c == '2') {
                    p2Start = new Point(x, y);
                    // Replace '2' with ' ' since it's not a wall
                    map[y][x] = ' ';
                }
            }
        }

        // If no start positions found, fallback to default
        if(p1Start == null) p1Start = new Point(5,5);
        if(p2Start == null) p2Start = new Point(width-6, height-6);

        // Extract level name from file path
        String levelName = getLevelNameFromPath(filePath);
        return new Level(map, p1Start, p2Start, levelName);
    }

    /**
     * Extracts a level name from the file path.
     * Removes the file extension and path separators.
     *
     * @param filePath The file path to extract the name from
     * @return The extracted level name
     */
    private static String getLevelNameFromPath(String filePath) {
        // Get the file name without extension and capitalize first letter of each word
        String fileName = Paths.get(filePath).getFileName().toString();
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        
        // Split by underscores or hyphens and capitalize each word
        String[] words = fileName.split("[_-]");
        StringBuilder nameBuilder = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                nameBuilder.append(Character.toUpperCase(word.charAt(0)))
                          .append(word.substring(1).toLowerCase())
                          .append(" ");
            }
        }
        return nameBuilder.toString().trim();
    }
}
