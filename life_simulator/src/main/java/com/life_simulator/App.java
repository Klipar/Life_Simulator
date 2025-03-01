package com.life_simulator;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
    // Initial values of the transformation
    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private double mouseAnchorX, mouseAnchorY;
    private double initialOffsetX, initialOffsetY;

    private final double CELL_SIZE = 20; // cell size

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Canvas canvas = new Canvas(1000, 1200);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Initial grid drawing
        drawGrid(gc);

        // Zoom: Handle scrolling of the mouse wheel
        canvas.setOnScroll((ScrollEvent event) -> {
            double delta = event.getDeltaY();
            // Determine the zoom ratio: zoom in or out
            if (delta == 0) return;
            double zoomFactor = (delta > 0) ? 1.1 : 1 / 1.1;
            double oldScale = scale;
            scale *= zoomFactor;
            // Adjust the offset so that the point under the cursor remains stable
            double mouseX = event.getX();
            double mouseY = event.getY();
            offsetX = mouseX - (mouseX - offsetX) * (scale / oldScale);
            offsetY = mouseY - (mouseY - offsetY) * (scale / oldScale);
            drawGrid(gc);
        });

        // Pan: by holding down the middle mouse button
        canvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.MIDDLE) {
                mouseAnchorX = e.getX();
                mouseAnchorY = e.getY();
                initialOffsetX = offsetX;
                initialOffsetY = offsetY;
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (e.isMiddleButtonDown()) {
                offsetX = initialOffsetX + (e.getX() - mouseAnchorX);
                offsetY = initialOffsetY + (e.getY() - mouseAnchorY);
                drawGrid(gc);
            }
        });

        // Left mouse button: output the cell coordinates to the console
        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                // Convert coordinates from screen to world coordinates
                double worldX = (e.getX() - offsetX) / scale;
                double worldY = (e.getY() - offsetY) / scale;
                // Calculating cell indices (assuming that cells have the size CELL_SIZE)
                int cellX = (int)(worldX / CELL_SIZE);
                int cellY = (int)(worldY / CELL_SIZE);
                System.out.println("Cell: (" + cellX + ", " + cellY + ")");
            }
        });

        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Life simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method for drawing a grid
    private void drawGrid(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.save();
        // Apply transformations: shift and scale
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);

        int cols = 800;
        int rows = 600;
        gc.setStroke(Color.LIGHTGRAY);
        for (int i = 0; i <= cols; i++) {
            double x = i * CELL_SIZE;
            gc.strokeLine(x, 0, x, rows * CELL_SIZE);
        }
        for (int j = 0; j <= rows; j++) {
            double y = j * CELL_SIZE;
            gc.strokeLine(0, y, cols * CELL_SIZE, y);
        }
        gc.restore();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
