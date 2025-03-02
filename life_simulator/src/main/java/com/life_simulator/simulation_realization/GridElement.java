package com.life_simulator.simulation_realization;

import java.util.HashMap;
import java.util.Map;

public class GridElement extends Base{
    private Cell cell;

    private Map <String, Integer> factors = new HashMap<>();

    public GridElement(Base base){
        super(base.getX(), base.getY());

        factors.put("LevelOfOrganicContamination", 0);
        factors.put("Temperature", 0);
        factors.put("LightingLevel", 0);
        factors.put("AcidityLevel", 0);
        factors.put("HumidityLevel", 0);
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

    public int getLevelOfOrganicContamination() {
        return factors.get("LevelOfOrganicContamination");
    }

    public void setLevelOfOrganicContamination(int levelOfOrganicContamination) {
        factors.put("LevelOfOrganicContamination", levelOfOrganicContamination);
    }

    public int getTemperature() {
        return factors.get("Temperature");
    }

    public void setTemperature(int temperature) {
        factors.put("Temperature", temperature);
    }

    public int getLightingLevel() {
        return factors.get(("LightingLevel"));
    }

    public void setLightingLevel(int lightingLevel) {
        factors.put("LightingLevel", lightingLevel);
    }

    public int getAcidityLevel() {
        return factors.get("AcidityLevel");
    }

    public void setAcidityLevel(int acidityLevel) {
        factors.put("AcidityLevel", acidityLevel);
    }

    public int getHumidityLevel() {
        return factors.get("HumidityLevel");
    }

    public void setHumidityLevel(int humidityLevel) {
        factors.put("humidityLevel", humidityLevel);
    }
}
