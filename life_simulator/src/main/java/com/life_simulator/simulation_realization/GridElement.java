package com.life_simulator.simulation_realization;

public class GridElement extends Base{
    private Cell cell;

    public GridElement(Base base){
        super(base.getX(), base.getY());
    }

    public boolean AddCell (Cell cell_inp){
        if (cell_inp == null || cell != null) return false;
        cell = cell_inp;
        return true;
    }

    public Cell getCell (){
        return cell;
    }

    public boolean DeleteCell(){
        if (cell == null) return false;
        this.cell = null;
        return true;
    }

    public boolean act(World world){
        if (cell == null) return false;
        return cell.act(world);
    }
}
