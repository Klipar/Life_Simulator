package com.life_simulator.simulation_realization;

import java.util.HashMap;
import java.util.Map;

public class GridElement extends Base{
    private Cell cell;
    private Map <String, Integer> factors = new HashMap<>();

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
        return cell.act(world, this);
    }

    public Map<String, Integer> getFactors() {
        return factors;
    }

    public void setFactors(Map<String, Integer> factors) {
        this.factors = factors;
    }

    public int getFactor(String factor){
        return factors.get(factor);
    }

    public void setFactor(String factor, int baseValue){
        factors.put(factor, baseValue);
    }
}
