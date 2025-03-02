package com.life_simulator.simulation_realization;

public class GridElement extends Base{
    private Cell cell;

    public GridElement(Base base){
        super(base.getX(), base.getY());
    }

    public boolean act(World world){
        if (cell == null) return false;
        return cell.act(world);
    }
}
