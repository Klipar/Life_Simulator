package com.life_simulator.simulation_realization;
import javafx.scene.paint.Color;

public class World  extends Base{
    private Cell[][] world;

    private Color backgroundColor = Color.rgb(16, 2, 49);
    private Color gridColor = Color.LIGHTGRAY;

    //does the world loop along one of its axes?
    private boolean XCycle;
    private boolean YCycle;

    public World(int X, int Y, boolean XCycle, boolean YCycle){
        super(X, Y);
        this.world = new Cell[X][Y]; // creating an array of a certain size
        this.XCycle = XCycle;
        this.YCycle = YCycle;
    }
    public boolean isXCycle() {
        return XCycle;
    }

    public void setXCycle(boolean xCycle) {
        XCycle = xCycle;
    }

    public boolean isYCycle() {
        return YCycle;
    }

    public void setYCycle(boolean yCycle) {
        YCycle = yCycle;
    }

    public boolean act(){
        System.out.println("call World.act");
        return true;
    }

    public boolean AddCell (Cell cell){
        if (cell == null) return false;
        if (world[cell.getX()][cell.getY()] != null) return false;
        world[cell.getX()][cell.getY()] = cell;
        return true;
    }

    public boolean DeleteCell (Cell cell){
        if (cell == null) return false;
        if (world[cell.getX()][cell.getY()] == null) return false;
        world[cell.getX()][cell.getY()] = null;
        return true;
    }

    public boolean UpdateCell (Cell cell, boolean safe){
/*  if safe == false, then the recording will take place in any case.
    If safe == true, then if there is no cell at that location, the write will not occur */
    boolean b = DeleteCell(cell);
        if (safe && b){
            return false;
        }
        return AddCell(cell);
    }

    public Cell getCell(int X, int Y){
        return world[X][Y];
    }

    public Cell[][] getWorld() {
        return world;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }
}
