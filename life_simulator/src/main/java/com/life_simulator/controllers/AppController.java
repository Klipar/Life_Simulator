package com.life_simulator.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

import com.life_simulator.simulation_realization.Cell;
import com.life_simulator.simulation_realization.World;
import com.life_simulator.simulation_realization.Base;



public class AppController {
    @FXML private Canvas canvas;
    @FXML private VBox rightSideMenu;
    @FXML private Button toggleRightMenuButton;
    @FXML private Rectangle RightMenuHitbox;
    @FXML private Button StartStopSimulation;
    @FXML private AnchorPane rootPane;
    @FXML private StackPane controlPanel;
    @FXML private TextField inputField;

    @FXML private VBox leftSideMenu;
    @FXML private Button toggleLeftMenuButton;
    @FXML private Rectangle leftMenuHitbox;

    private final double HIDE_OFFSET = 50;
    private final double HIDE_OFFSET_Left_MenuButton = -50;
    private boolean isHiddenRightMenuButton = false;
    private boolean isHiddenLeftMenuButton = false;
    private final BooleanProperty running = new SimpleBooleanProperty(false);
    private boolean isControlPanelHidden = false;
    private static final double HIDE_OFFSET_TOP_MENU = -60;

    private boolean isRightMenuOpen = true;
    private boolean isLeftMenuOpen = true;
    private double LeftMenuWidth = 200;
    private double RightMenuWidth = 200;

    private int fps_lock = 1000/30; //30 fps lock
    private int numberOfIterations = -1; //if -1 = ∞
    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private double mouseAnchorX, mouseAnchorY;
    private double initialOffsetX, initialOffsetY;
    private double CELL_SIZE = 20;

    private final int CELLS_IN_WIDTH = 50;
    private final int CELLS_IN_HEIGHT = 30;

    private Base DraggsBuffer;
    private boolean dragging = false;

    World world = new World(new Base(CELLS_IN_WIDTH, CELLS_IN_HEIGHT), false, false);

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        toggleRightMenuButton.setOnAction(e -> toggleRightMenu());
        toggleLeftMenuButton.setOnAction(e -> toggleLeftMenu());

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
            this.dragging = true;
            if (e.isMiddleButtonDown()) {
                offsetX = initialOffsetX + (e.getX() - mouseAnchorX);
                offsetY = initialOffsetY + (e.getY() - mouseAnchorY);
                UpdateCanvas(gc);
            } else if (e.isPrimaryButtonDown()) { //e.isSecondaryButtonDown()
                double worldX = (e.getX() - offsetX) / scale;
                double worldY = (e.getY() - offsetY) / scale;
                Base base = new Base ((int)(worldX / CELL_SIZE), (int)(worldY / CELL_SIZE));
                if (base.getX() > (world.getX() - 1) || base.getX() < 0 || worldX < 0) base.setX(-1);
                if (base.getY() > (world.getY() - 1) || base.getY() < 0 || worldY < 0) base.setY(-1);
                if (!base.equals(DraggsBuffer)){
                    DraggsBuffer = base;
                    System.out.println("GridElement: " + base);
                }
            }
        });

        StartStopSimulation.textProperty().bind(running.map(r -> r ? "Stop" : "Start"));

        canvas.setOnMouseClicked(e -> {
            if (dragging){
                this.dragging = false;
                return;
            }
            if (e.getButton() == MouseButton.PRIMARY) {
                double worldX = (e.getX() - offsetX) / scale;
                double worldY = (e.getY() - offsetY) / scale;
                Base base = new Base ((int)(worldX / CELL_SIZE), (int)(worldY / CELL_SIZE));
                if (base.getX() > (world.getX() - 1) || base.getX() < 0 || worldX < 0) base.setX(-1);
                if (base.getY() > (world.getY() - 1) || base.getY() < 0 || worldY < 0) base.setY(-1);
                System.out.println("Cell: (" + base.getX() + ", " + base.getY() + ")");

                if (!world.DeleteCell(world.getCell(base))) world.AddCell(new Cell(base));
                UpdateCanvas(gc);
            }
        });
        UpdateCanvas(gc);

        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                inputField.setText(oldValue);
            }
        });

        Thread gameLoop = new Thread(() -> {
            long timestamp = 0;
            while (true) {
                try {
                    timestamp = System.currentTimeMillis();
                    if (running.get()){
                        if (numberOfIterations == 0){
                            Platform.runLater(() -> {
                                running.set(false);
                            });
                            continue;
                        } else if (numberOfIterations > 0){
                            numberOfIterations--;
                            Platform.runLater(() -> {
                                inputField.setText(String.valueOf(numberOfIterations));
                            });
                        }
                        Base base = new Base(0,0);

                        while (base.getX() < world.getX()){
                            while(base.getY() < world.getY()){
                                // System.out.println(base);
                                if (world.getCell(base) != null) {
                                    // System.out.println("==>" + base);
                                    world.act(base);
                                }
                                base.setY(base.getY()+1);
                            }
                            base.setY(0);
                            base.setX(base.getX()+1);
                        }
                        UpdateCanvas(gc);
                    }
                    long tmp = fps_lock - (System.currentTimeMillis() - timestamp);
                    if (tmp > 0) Thread.sleep(tmp); //fps lock;
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        hideRightButton();
        hideLeftButton();
        toggleRightMenu();
        toggleLeftMenu();

    controlPanel.setTranslateY(HIDE_OFFSET_TOP_MENU);
    isControlPanelHidden = true;

        gameLoop.setDaemon(true);
        gameLoop.start();
    }

    @FXML
    private void StartStopSimulation() {
        String inputText = inputField.getText();
        if (inputText == ""){
            numberOfIterations = -1;
            inputField.setText("∞");
        } else {
            numberOfIterations = Integer.parseInt(inputText);
        }
        running.set(!running.get());
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

    private void toggleRightMenu() {
        double targetX = isRightMenuOpen ? RightMenuWidth : 0;
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), rightSideMenu);
        transition.setToX(targetX);
        transition.play();
        updateCanvasSize();
        isRightMenuOpen = !isRightMenuOpen;
    }

    private void toggleLeftMenu() {
        double targetX = isLeftMenuOpen ? -LeftMenuWidth : 0;
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), leftSideMenu);
        transition.setToX(targetX);
        transition.play();
        updateCanvasSize();
        isLeftMenuOpen = !isLeftMenuOpen;
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

    @FXML
    private void showRightButton() {
        if (!isHiddenRightMenuButton) return;
        animateButton(0);
        isHiddenRightMenuButton = false;
    }

    @FXML
    private void hideRightButton() {
        if (isHiddenRightMenuButton) return;
        animateButton(HIDE_OFFSET);
        isHiddenRightMenuButton = true;
    }

    private void animateButton(double offset) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(150), toggleRightMenuButton);
        transition.setToX(offset);
        transition.play();
    }

    @FXML
    private void showLeftButton() {
        if (!isHiddenLeftMenuButton) return;
        animateLeftButton(0);
        isHiddenLeftMenuButton = false;
    }

    @FXML
    private void hideLeftButton() {
        if (isHiddenLeftMenuButton) return;
        animateLeftButton(HIDE_OFFSET_Left_MenuButton);
        isHiddenLeftMenuButton = true;
    }

    private void animateLeftButton(double offset) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(150), toggleLeftMenuButton);
        transition.setToX(offset);
        transition.play();
    }


    @FXML
    private void showControlPanel() {
        if (!isControlPanelHidden) return;
        animateControlPanel(0);
        isControlPanelHidden = false;
    }

    @FXML
    private void hideControlPanel() {
        if (isControlPanelHidden) return;
        animateControlPanel(HIDE_OFFSET_TOP_MENU);
        isControlPanelHidden = true;
    }

    private void animateControlPanel(double offsetY) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(150), controlPanel);
        transition.setToY(offsetY);
        transition.play();
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
        // gc.setLineWidth((width*height)/);
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
        Base base = new Base(0,0);

        while (base.getX() < world.getX()){
            while(base.getY() < world.getY()){
                if (world.getCell(base) != null) {
                    gc.setFill(world.getCell(base).getColor());
                    gc.fillRect(base.getX() * CELL_SIZE, base.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
                base.setY(base.getY()+1);
            }
            base.setY(0);
            base.setX(base.getX()+1);
        }
        gc.restore();
    }
}
