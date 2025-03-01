package com.life_simulator.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;


public class AppController {
    @FXML private Canvas canvas;
    @FXML private VBox sideMenu;
    @FXML private Button toggleMenuButton;

    private boolean isMenuOpen = true;
    private double menuWidth = 200;
    @SuppressWarnings("unused")
    private double stageWidth;

    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private double mouseAnchorX, mouseAnchorY;
    private double initialOffsetX, initialOffsetY;
    private final double CELL_SIZE = 20;

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // sideMenu.setLayoutX(CANVAS_WIDTH);
        toggleMenuButton.setOnAction(e -> toggleMenu());

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
                System.out.println("Cell: (" + cellX + ", " + cellY + ")");
            }
        });

        drawGrid(gc);
    }

    @SuppressWarnings("unused")
    public void setStage(Stage stage) {
        stageWidth = stage.getWidth();
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            stageWidth = newVal.doubleValue();
            updateMenuPosition();
        });

        updateMenuPosition();
    }

    private void updateMenuPosition() {
        if (!isMenuOpen) {
            sideMenu.setTranslateX(menuWidth);
        } else {
            sideMenu.setTranslateX(0);
        }
    }

    private void toggleMenu() {
        double targetX = isMenuOpen ? menuWidth : 0;
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sideMenu);
        transition.setToX(targetX);
        transition.play();
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
