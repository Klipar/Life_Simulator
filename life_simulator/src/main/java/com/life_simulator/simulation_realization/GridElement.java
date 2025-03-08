package com.life_simulator.simulation_realization;

import java.util.Map;

import com.life_simulator.simulation_realization.Factors.Factors;

public class GridElement extends Base{
    private Cell cell = null;
    private Factors factors = new Factors();

    public GridElement(Base base){
        super(base.getX()-1, base.getY()-1);
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
        return factors.getFactorsAndValues();
    }

    public int getFactor(String factor){
        return this.factors.getFactorValue(factor);
    }

    public void adjustFactorBy(String factor, int value){
        this.factors.adjustFactorBy(factor, value);
    }

    public void setFactorValue(String factor, int baseValue){
        this.factors.setFactorValue(factor,baseValue);
        return;
    }

    @Override
    public String toString() {
        String str = new String();
        str += "GridElement on: (" + this.getX() + ", " + this.getY() + ")\n";
        str += "Life: " + ((this.getCell() != null) ? "true" : "false") + "\n";
        for (String key : factors.getFactorsAndValues().keySet()) {
            Integer value = factors.getFactorsAndValues().get(key);
            str += key + ": " + value + "\n";
        }
        return str;
    }
}
