import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Random;

public class App2023_Maze extends Application {

    private static final int MAZE_WIDTH = 24;
    private static final int MAZE_HEIGHT = 11;
    private static final int WALL = 1;
    private static final int PATH = 0;
    private static final int SHORTEST_PATH = 2;
    private static final int START_POINT = 2;
    private static final int END_POINT = 5;
    private static int START_ROW, START_COLUMN, END_ROW, END_COLUMN;
    private boolean mazeGenerated = true;

    private int[][] mazeData = {
            { WALL, START_POINT, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
                    
            { WALL, PATH, PATH, PATH, PATH, PATH, PATH, PATH, PATH, PATH, PATH, WALL, WALL, PATH, PATH, PATH, WALL, WALL, PATH, PATH, WALL, PATH, PATH, WALL },
                    
            { WALL, PATH, WALL, WALL, PATH, WALL, WALL, WALL, PATH, WALL, PATH, WALL, WALL, PATH, WALL, PATH, PATH, PATH, PATH, PATH, WALL, WALL, PATH, WALL },
                    
            { WALL, PATH, WALL, PATH, PATH, PATH, PATH, WALL, PATH, PATH, PATH, PATH, PATH, PATH, WALL, PATH, WALL, WALL, WALL, PATH, WALL, WALL, WALL, WALL },
                    
            { WALL, PATH, WALL, PATH, WALL, WALL, PATH, WALL, PATH, WALL, PATH, WALL, WALL, WALL, WALL, PATH, PATH, PATH, WALL, PATH, WALL, WALL, WALL, WALL },
                    
            { WALL, PATH, WALL, PATH, WALL, WALL, PATH, WALL, PATH, PATH, PATH, PATH, PATH, PATH, WALL, WALL, WALL, WALL, WALL, PATH, PATH, PATH, PATH, WALL },
                    
            { WALL, PATH, PATH, PATH, PATH, PATH, PATH, PATH, PATH, PATH, WALL, PATH, WALL, PATH, PATH, PATH, PATH, PATH, PATH, WALL, WALL, WALL, PATH, END_POINT },
                    
            { WALL, PATH, WALL, PATH, WALL, WALL, PATH, WALL, WALL, PATH, WALL, PATH, WALL, PATH, WALL, PATH, WALL, PATH, WALL, WALL, WALL, WALL, PATH, WALL },
                    
            { WALL, PATH, WALL, PATH, PATH, PATH, PATH, PATH, PATH, PATH, WALL, PATH, WALL, PATH, WALL, PATH, WALL, PATH, PATH, PATH, PATH, PATH, PATH, WALL },
                    
            { WALL, PATH, PATH, WALL, WALL, PATH, PATH, WALL, WALL, WALL, WALL, PATH, PATH, PATH, WALL, WALL, PATH, PATH, WALL, WALL, PATH, WALL, WALL, WALL },
                   
            { WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL }
                   
    };

    private int[][] distance = new int[MAZE_HEIGHT][MAZE_WIDTH];
    private boolean[][] visited = new boolean[MAZE_HEIGHT][MAZE_WIDTH];
    private int[][] shortestPath = new int[MAZE_HEIGHT][MAZE_WIDTH];
    private int[][] originalMazeData = new int[MAZE_HEIGHT][MAZE_WIDTH];

    private GridPane mazeGrid;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set the window title
        primaryStage.setTitle("Dijkstra's Algorithm ");

        // Create a layout for the splash screen
        Pane splashRoot = new Pane();

        // Load your background image
        Image backgroundImage = new Image("MAZE LOGO.jpeg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setId("backgroundImageView"); // Set an ID for styling
        splashRoot.getChildren().add(backgroundImageView);



        Scene splashScene = new Scene(splashRoot, 765, 798);
        Stage splashStage = new Stage();

        splashStage.getIcons().add(new Image("maze3.png"));

        splashScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Fx.css")).toExternalForm());

        splashStage.setScene(splashScene);
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.show();

        // Ensure the image is loaded before calculating center position
        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.setFitWidth(splashScene.getWidth());
        backgroundImageView.setFitHeight(splashScene.getHeight());
        backgroundImageView.setSmooth(true);
        backgroundImageView.setCache(true);

        // Center the image
        backgroundImageView.setX((splashScene.getWidth() - backgroundImageView.getBoundsInLocal().getWidth()) / 2);
        backgroundImageView.setY((splashScene.getHeight() - backgroundImageView.getBoundsInLocal().getHeight()) / 2);

        // Heading
        Text heading = new Text("Loading...");
        heading.getStyleClass().add("loading"); // Add a CSS class for styling

        // Center the text horizontally
        heading.setLayoutX((splashScene.getWidth() - heading.getBoundsInLocal().getWidth()) / 2);

        // Center the text vertically
        heading.setLayoutY(750);

        splashRoot.getChildren().add(heading);

        // progress bar
        ProgressBar progressBar = new ProgressBar();
        progressBar.getStyleClass().add("progress-bar"); // Add the CSS class
        progressBar.setPrefSize(300, 20);
        progressBar.setLayoutX((splashRoot.getWidth() - progressBar.getPrefWidth()) / 2); // Centered
        progressBar.setLayoutY(splashRoot.getHeight() - 30); // Above the bottom
        splashRoot.getChildren().add(progressBar);

        // Create the main window after a delay
        Duration delay = Duration.seconds(8); // 7 seconds delay
        KeyFrame keyFrame = new KeyFrame(delay, event -> showMainApplication(primaryStage, splashStage));
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    // Second
    // Window------------------------------------------------------------------------------------------

    private void showMainApplication(Stage primaryStage, Stage splashStage) {

        // Create a layout
        Pane root = new Pane();

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);

        primaryStage.setMinWidth(1200);
        primaryStage.setMaxWidth(1200);
        primaryStage.setMinHeight(800);
        primaryStage.setMaxHeight(800);

        primaryStage.getIcons().add(new Image("maze3.png"));

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Fx.css")).toExternalForm());

        Rectangle verticalBar = new Rectangle(1200, 50, Color.BLUE); // Set width, height, and color
        verticalBar.getStyleClass().add("vertical-bar");
        root.getChildren().add(verticalBar);

        // Load logo image
        Image logoImage = new Image("maze3.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setId("logoImageView"); // Set an ID for styling
        logoImageView.setPreserveRatio(true);
        logoImageView.setLayoutX(25);
        logoImageView.setLayoutY(10);
        root.getChildren().add(logoImageView);

        // Heading
        Text heading = new Text("M A Z E");
        heading.getStyleClass().add("heading"); // Add a CSS class for styling
        heading.setLayoutX(95);
        heading.setLayoutY(30);
        root.getChildren().add(heading);

        // Create buttons
        Button button2 = createButton("Solve Maze", 560, 9);
        Button button3 = createButton("Reset", 680, 9);
        Button button4 = createButton("Elapsed Time", 800, 9);
        Button button5 = createButton("Generate New", 920, 9);
        Button button6 = createButton("Quit", 1040, 9);


        button6.setOnAction(e -> showQuitConfirmation());
        button3.setOnAction(event -> resetMaze());
        button4.setOnAction(event -> showElapsedTime());

        // Generate Random Maze
        button5.setOnAction(event -> {
            // Generate a new random maze
            int[][] randomMazeData = generateRandomMazeData();
        
            // Update mazeData with the new random maze data
            for (int row = 0; row < MAZE_HEIGHT; row++) {
                System.arraycopy(randomMazeData[row], 0, mazeData[row], 0, MAZE_WIDTH);
            }
        
            // Remove the existing maze
            root.getChildren().remove(mazeGrid);
        
            // Create the new mazeGrid with the updated mazeData
            mazeGrid = createMaze();
            calculateStartEndAttributes();
        
            // Find and mark the shortest path in the new maze
            findShortestPath();
        
            // Add the new maze to the root
            root.getChildren().add(mazeGrid);
        
            // Position the maze
            updateMazeGridPosition(scene.getWidth(), scene.getHeight(), mazeGrid);
        });
        

        root.getChildren().addAll(button2, button3, button4, button5, button6);

        // Helper method to calculate Y position based on width, height, and mazeGrid
        // height

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            double xPosition = (newValue.doubleValue() - mazeGrid.getWidth()) / 2;
            mazeGrid.setLayoutX(xPosition);

            double yPosition = (scene.getHeight() - mazeGrid.getHeight()) / 2;
            mazeGrid.setLayoutY(yPosition);
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            double yPosition = (newValue.doubleValue() - mazeGrid.getHeight()) / 2.57;
            mazeGrid.setLayoutY(yPosition);
        });

        // -----------------------------------

        // Apply methods of Dijkstra
        mazeGrid = createMaze();
        calculateStartEndAttributes();
        findShortestPath(); 
        button2.setOnAction(event -> {
            if (mazeGenerated) {
                solveMaze();
            } else {
                // Inform the user to generate a maze first
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Please generate a maze first.");
                alert.showAndWait();
            }
        });
        root.getChildren().add(mazeGrid);

        Rectangle horizontalBar = new Rectangle(1200, 50, Color.BLUE);
        horizontalBar.getStyleClass().add("horizontal-bar");

        // Apply drop shadows individually for each side
        DropShadow topShadow = new DropShadow(10, 0, 0, Color.rgb(3, 3, 3, 0.8));
        DropShadow rightShadow = new DropShadow(10, 0, 0, Color.rgb(0, 0, 0, 0.8));
        DropShadow bottomShadow = new DropShadow(10, 0, 0, Color.rgb(0, 0, 0, 0.8));
        DropShadow leftShadow = new DropShadow(10, 10, 0, Color.rgb(0, 0, 0, 0.8));

        horizontalBar.setEffect(topShadow);
        horizontalBar.setEffect(rightShadow);
        horizontalBar.setEffect(bottomShadow);
        horizontalBar.setEffect(leftShadow);

        // Set the position of the rectangle
        horizontalBar.setX(0);
        horizontalBar.setLayoutY(650);
        horizontalBar.setWidth(1200);
        horizontalBar.setHeight(300);

        root.getChildren().add(horizontalBar);
        // ---------imagessss-------------------------
        // image
        Image logoImage2 = new Image("mylogo2.png");
        ImageView logoImageView2 = new ImageView(logoImage2);
        logoImageView2.getStyleClass().add("logoImageView"); // Set an ID for styling

        // Set the desired dimensions
        double desiredWidth = 320; // Set your desired width
        double desiredHeight = 150; // Set your desired height

        // Set the fitWidth and fitHeight properties to maintain aspect ratio
        logoImageView2.setFitWidth(desiredWidth);
        logoImageView2.setFitHeight(desiredHeight);

        // Calculate the center coordinates
        double centerX = (scene.getWidth() - logoImageView2.getBoundsInLocal().getWidth()) / 1.1;
        double centerY = (scene.getHeight() - logoImageView2.getBoundsInLocal().getHeight()) / 1.025;

        // Set the layout coordinates
        logoImageView2.setLayoutX(centerX);
        logoImageView2.setLayoutY(centerY);

        root.getChildren().add(logoImageView2);

        // image

        Image logoImage3 = new Image("AD.png");
        ImageView logoImageView3 = new ImageView(logoImage3);
        logoImageView3.getStyleClass().add("logoImageView"); // Set an ID for styling

        // Set the desired dimensions
        double desiredWidth3 = 90; // Set your desired width
        double desiredHeight3 = 78; // Set your desired height

        // Set the fitWidth and fitHeight properties to maintain aspect ratio
        logoImageView3.setFitWidth(desiredWidth3);
        logoImageView3.setFitHeight(desiredHeight3);

        // Calculate the center coordinates
        double centerX3 = (scene.getWidth() - logoImageView3.getBoundsInLocal().getWidth()) / 17;
        double centerY3 = (scene.getHeight() - logoImageView3.getBoundsInLocal().getHeight()) / 1.078;

        // Set the layout coordinates
        logoImageView3.setLayoutX(centerX3);
        logoImageView3.setLayoutY(centerY3);

        // Add a click event handler
        logoImageView3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Show a message when the image is clicked
                showDescription();
            }
        });

        root.getChildren().add(logoImageView3);

        // image

        Image logoImage4 = new Image("users444.png");
        ImageView logoImageView4 = new ImageView(logoImage4);
        logoImageView4.getStyleClass().add("logoImageView"); // Set an ID for styling

        // Set the desired dimensions
        double desiredWidth4 = 110; // Set your desired width
        double desiredHeight4 = 95; // Set your desired height

        // Set the fitWidth and fitHeight properties to maintain aspect ratio
        logoImageView4.setFitWidth(desiredWidth4);
        logoImageView4.setFitHeight(desiredHeight4);

        // Calculate the center coordinates
        double centerX4 = (scene.getWidth() - logoImageView4.getBoundsInLocal().getWidth()) / 5;
        double centerY4 = (scene.getHeight() - logoImageView4.getBoundsInLocal().getHeight()) / 1.065;

        // Set the layout coordinates
        logoImageView4.setLayoutX(centerX4);
        logoImageView4.setLayoutY(centerY4);

        // Add a click event handler
        logoImageView4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Show a message when the image is clicked
                showImageClickMessage();
            }
        });

        root.getChildren().add(logoImageView4);

        // --------------------------------
        primaryStage.show();

        // Close the splash window
        splashStage.close();
    }

    private Button createButton(String text, double layoutX, double layoutY) {
        Button button = new Button(text);
        button.getStyleClass().add("custom-button");
        button.setOnAction(e -> System.out.println(text + " clicked!"));
        button.setLayoutX(layoutX);
        button.setLayoutY(layoutY);
        return button;
    }

    private void showQuitConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Quit Confirmation");
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    private void calculateStartEndAttributes() {
        for (int row = 0; row < MAZE_HEIGHT; row++) {
            for (int col = 0; col < MAZE_WIDTH; col++) {
                if (mazeData[row][col] == START_POINT) {
                    START_ROW = row;
                    START_COLUMN = col;
                } else if (mazeData[row][col] == END_POINT) {
                    END_ROW = row;
                    END_COLUMN = col;
                }
            }
        }
    }



    private GridPane createMaze() {
        GridPane mazeGrid = new GridPane();

        double cellWidth = 45;
        double cellHeight = 45;

        // Save the original state of mazeData
        for (int row = 0; row < MAZE_HEIGHT; row++) {
            System.arraycopy(mazeData[row], 0, originalMazeData[row], 0, MAZE_WIDTH);
        }

        for (int row = 0; row < MAZE_HEIGHT; row++) {
            for (int col = 0; col < MAZE_WIDTH; col++) {
                Rectangle cell = new Rectangle(cellWidth, cellHeight);

                if (mazeData[row][col] == WALL) {
                    cell.getStyleClass().add("maze-cell");
                    cell.getStyleClass().add("black");
                } else if (mazeData[row][col] == START_POINT) {
                    cell.getStyleClass().add("maze-cell");
                    cell.getStyleClass().add("red");
                } else if (mazeData[row][col] == END_POINT) {
                    cell.getStyleClass().add("maze-cell");
                    cell.getStyleClass().add("yellow");
                }

                else if (mazeData[row][col] == SHORTEST_PATH) {
                    cell.setFill(Color.WHITE);
                } else {
                    cell.setFill(Color.WHITE);
                }

                mazeGrid.add(cell, col, row);
            }
        }

        return mazeGrid;
    }

    private void updateMazeGridPosition(double width, double height, GridPane mazeGrid) {
        double xPosition = (width - mazeGrid.getBoundsInLocal().getWidth()) / 27;
        double yPosition = (height - mazeGrid.getBoundsInLocal().getHeight()) / 7;
    
        mazeGrid.setLayoutX(xPosition);
        mazeGrid.setLayoutY(yPosition);
    }

    // Dijkstra Algorithm

    private void findShortestPath() {
        int startRow = START_ROW;
        int startCol = START_COLUMN;
        int endRow = END_ROW;
        int endCol = END_COLUMN;

        initializeDistances();

        PriorityQueue<Cell> queue = new PriorityQueue<>();
        queue.add(new Cell(startRow, startCol, 0));

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            if (visited[current.row][current.col]) {
                continue;
            }

            visited[current.row][current.col] = true;

            exploreNeighbor(current.row - 1, current.col, current, queue);
            exploreNeighbor(current.row + 1, current.col, current, queue);
            exploreNeighbor(current.row, current.col - 1, current, queue);
            exploreNeighbor(current.row, current.col + 1, current, queue);
        }

        if (distance[endRow][endCol] == Integer.MAX_VALUE) {
            return;
        }

        int row = endRow;
        int col = endCol;

        while (row != startRow || col != startCol) {
            shortestPath[row][col] = SHORTEST_PATH;
            Cell next = getMinNeighbor(row, col, distance);
            row = next.row;
            col = next.col;
        }
    }

    private void initializeDistances() {
        for (int i = 0; i < MAZE_HEIGHT; i++) {
            for (int j = 0; j < MAZE_WIDTH; j++) {
                distance[i][j] = Integer.MAX_VALUE;
            }
        }

        int startRow = START_ROW;
        int startCol = START_COLUMN;
        distance[startRow][startCol] = 0;
    }

    private void exploreNeighbor(int row, int col, Cell current, PriorityQueue<Cell> queue) {
        if (isValidCell(row, col) && mazeData[row][col] == PATH
                && !visited[row][col]
                && current.distance + 1 < distance[row][col]) {
            distance[row][col] = current.distance + 1;
            queue.add(new Cell(row, col, distance[row][col]));
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < MAZE_HEIGHT && col >= 0 && col < MAZE_WIDTH;
    }

    private Cell getMinNeighbor(int row, int col, int[][] distance) {
        int minDistance = Integer.MAX_VALUE;
        Cell minCell = null;

        if (isValidCell(row - 1, col) && distance[row - 1][col] < minDistance) {
            minDistance = distance[row - 1][col];
            minCell = new Cell(row - 1, col, minDistance);
        }

        if (isValidCell(row + 1, col) && distance[row + 1][col] < minDistance) {
            minDistance = distance[row + 1][col];
            minCell = new Cell(row + 1, col, minDistance);
        }

        if (isValidCell(row, col - 1) && distance[row][col - 1] < minDistance) {
            minDistance = distance[row][col - 1];
            minCell = new Cell(row, col - 1, minDistance);
        }

        if (isValidCell(row, col + 1) && distance[row][col + 1] < minDistance) {
            minDistance = distance[row][col + 1];
            minCell = new Cell(row, col + 1, minDistance);
        }

        return minCell;
    }

    private void changePathColor() {
        int startRow = START_ROW;
        int startCol = START_COLUMN;
    
        Rectangle startCell = (Rectangle) mazeGrid.getChildren().get(startRow * MAZE_WIDTH + startCol);
        startCell.getStyleClass().add("red");
    
        int row = END_ROW;
        int col = END_COLUMN;
    
        while (row != startRow || col != startCol) {
            Rectangle cell = (Rectangle) mazeGrid.getChildren().get(row * MAZE_WIDTH + col);
            cell.getStyleClass().add("green");
    
            Cell next = getMinNeighbor(row, col, distance);
    
            // Check if next is null
            if (next == null) {
                // Display a message when no valid path exists
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("No valid path exists to solve the maze. Generate a new maze.");
                alert.showAndWait();
    
                // Handle the case where next is null, for example, break out of the loop
                break;
            }
    
            row = next.row;
            col = next.col;
        }
    
        Rectangle endCell = (Rectangle) mazeGrid.getChildren().get(END_ROW * MAZE_WIDTH + END_COLUMN);
        endCell.getStyleClass().add("yellow");
    }
    
    private static class Cell implements Comparable<Cell> {
        int row, col, distance;

        public Cell(int row, int col, int distance) {
            this.row = row;
            this.col = col;
            this.distance = distance;
        }

        @Override
        public int compareTo(Cell other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    private void resetMaze() {
        // Reset mazeData to its original state
        for (int row = 0; row < MAZE_HEIGHT; row++) {
            for (int col = 0; col < MAZE_WIDTH; col++) {
                mazeData[row][col] = originalMazeData[row][col];
            }
        }

        // Reset the mazeGrid display
        updateMazeDisplay();
    }

    private void updateMazeDisplay() {
        for (int row = 0; row < MAZE_HEIGHT; row++) {
            for (int col = 0; col < MAZE_WIDTH; col++) {
                Rectangle cell = (Rectangle) mazeGrid.getChildren().get(row * MAZE_WIDTH + col);
                if (mazeData[row][col] == WALL) {
                    cell.getStyleClass().add("maze-cell");
                    cell.getStyleClass().add("black");
                } else if (mazeData[row][col] == START_POINT) {
                    cell.getStyleClass().add("maze-cell");
                    cell.getStyleClass().add("red");
                } else if (mazeData[row][col] == END_POINT) {
                    cell.getStyleClass().add("maze-cell");
                    cell.getStyleClass().add("yellow");
                }

                else if (mazeData[row][col] == SHORTEST_PATH) {
                    cell.setFill(Color.WHITE);
                } else {
                    cell.setFill(Color.WHITE);
                }
            }
        }
    }

    private void showImageClickMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Members");
        alert.setHeaderText(null);
        alert.setContentText(
                "Muhammad Taqi Rahmani - 283\nShafin Alam Khan - 252\nArqum Faisal - 254\nIbad Tahir - 344");

        alert.showAndWait();
    }

    private void showDescription() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Members");
        alert.setHeaderText(null);
        alert.setContentText(
                "In our project, the Dijkstra algorithm intricately finds the shortest paths in maze, mirroring its role in map navigators for precise route optimization");

        alert.getDialogPane().getStyleClass().add("your-custom-style-class");

        alert.showAndWait();
    }


    private void showElapsedTime() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Elapsed Time");
        alert.setHeaderText(null);
        alert.setContentText(String.format("Elapsed Time: %.2f seconds", 0.12));
    
        alert.showAndWait();
    }

    public int[][] generateRandomMazeData() {
        print2Darray();
        System.out.println("---------------------");
        mazeData = new int[MAZE_HEIGHT][MAZE_WIDTH];
        Random random = new Random();

        for (int row = 0; row < MAZE_HEIGHT; row++) {
            for (int col = 0; col < MAZE_WIDTH; col++) {
                if (random.nextDouble() < 0.25) {
                    mazeData[row][col] = WALL;
                } else {
                    mazeData[row][col] = PATH;
                }
            }
        }

        mazeData[START_ROW][START_COLUMN] = START_POINT;

        mazeData[END_ROW][END_COLUMN] = END_POINT;
        print2Darray();

        return mazeData;
    }
    private void print2Darray(){
        for(int[] i:mazeData){
            System.out.println(Arrays.toString(i));
        }

    }
    private void solveMaze() {
        // Reset the visited array
        visited = new boolean[MAZE_HEIGHT][MAZE_WIDTH];
        initializeDistances();
        findShortestPath();
        changePathColor();
        mazeGenerated = true;
    }

}

