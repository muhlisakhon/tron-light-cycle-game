# Tron Light-Cycle Battle Game

A two-player Tron-style light-cycle battle game built in Java with real-time collision detection, persistent score tracking via SQLite, and an interactive UI using Java Swing.

## Features

- ⚔️ Real-time two-player light-cycle battle gameplay
- 💾 Persistent score tracking using SQLite database
- 🎮 Keyboard-based control with responsive movement and collision handling
- 🧱 Level loading from external files for custom map configurations
- 🖼️ Java Swing UI for game setup, results display, and high score screen
- 🧠 Object-Oriented design for clean and modular architecture


## Tech Stack

- **Java**
- **Java Swing** (for GUI)
- **SQLite** (for storing scores)
- **Object-Oriented Design Patterns**

## How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/muhlisakhon/tron-light-cycle-game.git
   cd tron-light-cycle-game
2. Compile the project:
   javac -cp ".;sqlite-jdbc.jar" src/**/*.java
3. Run the game:
   java -cp ".;sqlite-jdbc.jar" Main
Make sure you have SQLite JDBC driver in your classpath.
