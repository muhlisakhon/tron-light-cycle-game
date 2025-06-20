package tron;

import java.sql.*;
import java.util.*;

/**
 * Manages the game's database operations for storing and retrieving player scores.
 * Handles database connections, score updates, and high score retrieval.
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:game.db";
    private Connection conn;

    /**
     * Initializes the database manager and creates the scores table if it doesn't exist.
     * Establishes a connection to the SQLite database.
     */
    public DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
            initDB();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the scores table in the database if it doesn't exist.
     * @throws SQLException if there's an error executing the SQL
     */
    private void initDB() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS scores (player_name TEXT PRIMARY KEY, wins INTEGER)");
        st.close();
    }

    /**
     * Updates the score for a player in the database.
     * If the player doesn't exist, creates a new record.
     *
     * @param winner The name of the player to update score for
     */
    public void updateScore(String winner) {
        if(winner == null || winner.trim().isEmpty() || winner.equals("Draw")) return;

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT wins FROM scores WHERE player_name = ?");
            ps.setString(1, winner);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int currentWins = rs.getInt("wins");
                rs.close();
                ps.close();
                PreparedStatement ups = conn.prepareStatement("UPDATE scores SET wins = ? WHERE player_name = ?");
                ups.setInt(1, currentWins+1);
                ups.setString(2, winner);
                ups.executeUpdate();
                ups.close();
            } else {
                rs.close();
                ps.close();
                PreparedStatement ins = conn.prepareStatement("INSERT INTO scores(player_name, wins) VALUES(?,?)");
                ins.setString(1, winner);
                ins.setInt(2, 1);
                ins.executeUpdate();
                ins.close();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the top scores from the database.
     *
     * @param limit The maximum number of scores to retrieve
     * @return List of player scores, sorted by score in descending order
     */
    public List<PlayerScore> getTopScores(int limit) {
        List<PlayerScore> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT player_name, wins FROM scores ORDER BY wins DESC LIMIT ?");
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                list.add(new PlayerScore(rs.getString("player_name"), rs.getInt("wins")));
            }
            rs.close();
            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Closes the database connection.
     * Should be called when the application is shutting down.
     */
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Represents a player's score.
     */
    public static class PlayerScore {
        public String name;
        public int wins;

        public PlayerScore(String n, int w) {
            name = n;
            wins = w;
        }
    }
}
