package com.life_simulator.simulation_realization;
import javafx.scene.paint.Color;

public class World extends Base{
    private GridElement[][] world;

    private Color backgroundColor = Color.rgb(255, 255, 255);
    private Color gridColor = Color.LIGHTGRAY;

    private Base worldMaxCords = new Base(0,0); //start from 1 to X,Y
    //does the world loop along one of its axes?
    private boolean XCycle;
    private boolean YCycle;

    public World(Base base, boolean XCycle, boolean YCycle){
        super(base.getX(), base.getY());
        this.worldMaxCords.setBase(base);
        this.world = new GridElement[base.getX()][base.getY()]; // creating an array of a certain size

        while (base.getX() > 0){
            while(base.getY() > 0){
                this.world[base.getX()-1][base.getY()-1] = new GridElement(base);
                base.setY(base.getY()-1);
            }
            base.setY(this.getY());
            base.setX(base.getX()-1);
        }
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

    public boolean act(Base base){
        return world[base.getX()][base.getY()].act(this);
    }

    public boolean AddCell (Cell cell){
        if (cell == null || world[cell.getX()][cell.getY()].getCell() != null) return false;
        world[cell.getX()][cell.getY()].AddCell(cell);
        return true;
    }

    public boolean DeleteCell (Cell cell){
        if (cell == null || world[cell.getX()][cell.getY()].getCell() == null) return false;
        world[cell.getX()][cell.getY()].DeleteCell();
        return true;
    }

    public boolean DeleteCell (Base base){
        if (base == null || world[base.getX()][base.getY()].getCell() == null) return false;
        world[base.getX()][base.getY()].DeleteCell();
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

    public Cell getCell(Base base){
        return world[base.getX()][base.getY()].getCell();
    }

    public GridElement[][] getWorld() {
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

    public GridElement getGridElement(Base base){
        if (base.getX() > (this.worldMaxCords.getX()-1)
        || base.getY() > (this.worldMaxCords.getY()-1)
        || base.getX() < 0 || base.getY() < 0){
            return null;
        }
        return world[base.getX()][base.getY()];
    }
}
