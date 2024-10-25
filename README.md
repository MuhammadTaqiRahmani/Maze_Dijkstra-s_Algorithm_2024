
# Maze Dijkstra's Algorithm 2024

## Overview

Maze Dijkstra's Algorithm 2024 is a Java-based application that implements Dijkstra's algorithm to find the shortest path through a maze. This project visualizes the algorithm's process step by step, making it easier to understand how Dijkstra's algorithm works in finding optimal paths. It is designed for educational purposes and can be used to explore concepts in graph theory, pathfinding algorithms, and maze-solving.

## Features

* Visual representation of Dijkstra's algorithm in action.
* Step-by-step animation showing the shortest pathfinding process.
* User-friendly interface to set start and end points in the maze.
* Option to create and customize mazes.
* Adjustable algorithm speed for better visualization.
Supports multiple maze sizes and configurations.

## UI Module (JavaFX)
The user interface for this application is built using JavaFX, which provides a smooth and responsive graphical interface. The UI module allows users to:

* Easily set Start and End Points: Click directly on the grid to define where the algorithm should begin and where it should find the shortest path.
* Generate Custom Mazes: Use controls to create different maze layouts, adding walls and open paths as needed.
* Control Algorithm Speed: Adjust the visualization speed to see the algorithm's progress at your own pace.
* Dynamic Visualization: Watch as the algorithm explores the maze and discovers the shortest path in real time.
* Clear and Intuitive Controls: Buttons and sliders for starting, pausing, resetting, and customizing the maze, making the application easy to use for all users.
## Prerequisites

To run this project, make sure you have:

* Java Development Kit (JDK) 8 or later installed.
* IDE that supports Java, such as IntelliJ IDEA or Eclipse.
* (Optional) Maven or Gradle for dependency management.


## Installation

* Clone the repository

```bash
git clone https://github.com/username/Maze_Dijkstra-s_Algorithm_2024.git

```
    
* Clone the repository

```bash
cd Maze_Dijkstra-s_Algorithm_2024

```

* Open the project in your preferred IDE.

## Usage

    1. Run the Main.java file to start the application.

    2. Use the interface to set the start and end points of the maze.

    3. Click on the "Solve" button to visualize the algorithm finding the shortest path.

    4. Use the customization options to generate new maze layouts or adjust the speed of the animation.


## How It Works

Dijkstra's Algorithm is a graph traversal algorithm that finds the shortest path from a source node to all other nodes in a graph. In this project:

* The maze is represented as a grid, where each cell is a node.
* Walls are treated as obstacles, and open paths represent traversable nodes.
* The algorithm starts at the user-defined starting point and explores all possible paths until it reaches the end point, updating the shortest path distances along the way.
* The shortest path is visualized in real time.


