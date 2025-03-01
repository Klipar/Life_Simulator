package com.life_simulator.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;


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

        canvas.widthProperty().addListener(evt -> drawGrid(gc));
        canvas.heightProperty().addListener(evt -> drawGrid(gc));

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
                drawGrid(gc);
            }
        });

        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                double worldX = (e.getX() - offsetX) / scale;
                double worldY = (e.getY() - offsetY) / scale;
                int cellX = (int)(worldX / CELL_SIZE);
                int cellY = (int)(worldY / CELL_SIZE);
                if (cellX > (CELLS_IN_WIDTH-1) || cellX < 0 || worldX < 0) cellX = -1;
                if (cellY > (CELLS_IN_HEIGHT-1) || cellY < 0 || worldY < 0) cellY = -1;
                System.out.println("Cell: (" + cellX + ", " + cellY + ")");
            }
        });

        drawGrid(gc);
    }

    @FXML
    private void updateCanvasSize() {
        if (canvas.getScene() == null) return;

        double sceneWidth = canvas.getScene().getWidth();
        double sceneHeight = canvas.getScene().getHeight();
        double newCellSizeX = sceneWidth / CELLS_IN_WIDTH;
        double newCellSizeY = sceneHeight / CELLS_IN_HEIGHT;


        CELL_SIZE = (newCellSizeX < newCellSizeY ? newCellSizeY : newCellSizeX);

        if (CELL_SIZE < 1) CELL_SIZE = 20;

        canvas.setWidth(CELLS_IN_WIDTH * CELL_SIZE);
        canvas.setHeight(CELLS_IN_HEIGHT * CELL_SIZE);
        drawGrid(canvas.getGraphicsContext2D());
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

        drawGrid(canvas.getGraphicsContext2D());
    }

    private void drawGrid(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc.clearRect(0, 0, width, height);
        gc.save();
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
}
