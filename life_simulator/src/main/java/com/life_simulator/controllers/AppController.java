package com.life_simulator.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import com.life_simulator.simulation_realization.Cell;
import com.life_simulator.simulation_realization.World;



public class AppController {
    @FXML private Canvas canvas;
    @FXML private VBox sideMenu;
    @FXML private Button toggleMenuButton;

    private boolean isMenuOpen = true;
    private double menuWidth = 200;

    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private double mouseAnchorX, mouseAnchorY;
    private double initialOffsetX, initialOffsetY;
    private double CELL_SIZE = 20;

    private final int CELLS_IN_WIDTH = 50;
    private final int CELLS_IN_HEIGHT = 30;
    World world = new World(CELLS_IN_WIDTH, CELLS_IN_HEIGHT, false, false);

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        toggleMenuButton.setOnAction(e -> toggleMenu());

        canvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((o, oldVal, newVal) -> updateCanvasSize());
                newScene.heightProperty().addListener((o, oldVal, newVal) -> updateCanvasSize());
                updateCanvasSize();
            }
        });

        canvas.widthProperty().addListener(evt -> UpdateCanvas(gc));
        canvas.heightProperty().addListener(evt -> UpdateCanvas(gc));

        canvas.setOnScroll(this::handleZoom);
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
                UpdateCanvas(gc);
            }
        });

        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                double worldX = (e.getX() - offsetX) / scale;
                double worldY = (e.getY() - offsetY) / scale;
                int cellX = (int)(worldX / CELL_SIZE);
                int cellY = (int)(worldY / CELL_SIZE);
                if (cellX > (world.getX() - 1) || cellX < 0 || worldX < 0) cellX = -1;
                if (cellY > (world.getY() - 1) || cellY < 0 || worldY < 0) cellY = -1;
                System.out.println("Cell: (" + cellX + ", " + cellY + ")");


                if (!world.DeleteCell(world.getCell(cellX, cellY))) world.AddCell(new Cell(cellX, cellY));
                UpdateCanvas(gc);
            }
        });
        world.AddCell(new Cell(10, 10));
        UpdateCanvas(gc);
    }

    @FXML
    private void updateCanvasSize() {
        if (canvas.getScene() == null) return;

        double sceneWidth = canvas.getScene().getWidth();
        double sceneHeight = canvas.getScene().getHeight();
        double newCellSizeX = sceneWidth / world.getX();
        double newCellSizeY = sceneHeight / world.getY();


        CELL_SIZE = (newCellSizeX < newCellSizeY ? newCellSizeY : newCellSizeX);

        if (CELL_SIZE < 1) CELL_SIZE = 20;

        canvas.setWidth(world.getX() * CELL_SIZE);
        canvas.setHeight(world.getY() * CELL_SIZE);
        UpdateCanvas(canvas.getGraphicsContext2D());
    }

    private void toggleMenu() {
        double targetX = isMenuOpen ? menuWidth : 0;
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sideMenu);
        transition.setToX(targetX);
        transition.play();
        updateCanvasSize();
        isMenuOpen = !isMenuOpen;
    }

    private void handleZoom(ScrollEvent event) {
        double delta = event.getDeltaY();
        if (delta == 0) return;

        double zoomFactor = (delta > 0) ? 1.1 : 1 / 1.1;
        double oldScale = scale;
        scale *= zoomFactor;

        double mouseX = event.getX();
        double mouseY = event.getY();
        offsetX = mouseX - (mouseX - offsetX) * (scale / oldScale);
        offsetY = mouseY - (mouseY - offsetY) * (scale / oldScale);

        UpdateCanvas(canvas.getGraphicsContext2D());
    }

    public void UpdateCanvas (GraphicsContext gc){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        FillBackgroundCanvas(gc);
        drawGrid(gc);
        drawCells(gc);
    }

    private void FillBackgroundCanvas(GraphicsContext gc) {
        gc.save();
        gc.setFill(world.getBackgroundColor());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.restore();
    }

    private void drawGrid(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc.save();
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);
        gc.setStroke(world.getGridColor());
        for (double x = 0; x <= width; x += CELL_SIZE)
            gc.strokeLine(x, 0, x, height);

        for (double y = 0; y <= height; y += CELL_SIZE)
            gc.strokeLine(0, y, width, y);

        gc.restore();
    }

    private void drawCells(GraphicsContext gc) {
        gc.save();
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);

        for (int x = 0; x < world.getX(); x++) {
            for (int y = 0; y < world.getY(); y++) {
                if (world.getCell(x, y) != null) {
                    gc.setFill(world.getCell(x, y).getColor());
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        gc.restore();
    }
}
