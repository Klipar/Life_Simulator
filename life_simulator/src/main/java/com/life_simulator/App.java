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

    private final double CELL_SIZE = 20;

    @SuppressWarnings("unused")
    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Canvas canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Scene scene = new Scene(root, 820, 600);
        root.getChildren().add(canvas);

        // Bind Canvas dimensions to the window size
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());

        // Update the grid when resizing
        canvas.widthProperty().addListener(evt -> drawGrid(gc));
        canvas.heightProperty().addListener(evt -> drawGrid(gc));

        // Zoom: scroll with the mouse
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

        // Pan with the mouse
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
                double worldX = (e.getX() - offsetX) / scale;
                double worldY = (e.getY() - offsetY) / scale;
                // Calculating cell indices (assuming that cells have the size CELL_SIZE)
                int cellX = (int)(worldX / CELL_SIZE);
                int cellY = (int)(worldY / CELL_SIZE);
                System.out.println("Cell: (" + cellX + ", " + cellY + ")");
            }
        });

        primaryStage.setTitle("Life simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Draw the mesh after the first render
        drawGrid(gc);
    }

    // Method for drawing a grid
    private void drawGrid(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        gc.clearRect(0, 0, width, height);
        gc.save();
        // Apply transformations: shift and scale
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);

        gc.setStroke(Color.LIGHTGRAY);
        for (double x = 0; x <= width; x += CELL_SIZE) {
            gc.strokeLine(x, 0, x, height);
        }
        for (double y = 0; y <= height; y += CELL_SIZE) {
            gc.strokeLine(0, y, width, y);
        }
        gc.restore();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
