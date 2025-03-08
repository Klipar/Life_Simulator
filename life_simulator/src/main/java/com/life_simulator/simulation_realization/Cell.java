package com.life_simulator.simulation_realization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.scene.paint.Color;

public class Cell extends Base {
    private static ArrayList<String> VisualizationTypes = new ArrayList<>(Arrays.asList("Cell type mode", "Genome mode", "Ancestor mods"));
    private Color color = Color.ORANGE;
    private int type = 0;

    private int EnergyLevel = 100;

    private Map <String, Integer> resistance = new HashMap<>();

    Random random = new Random();



    public Cell(Base base){
        super(base.getX(), base.getY());

        resistance.put("LevelOfOrganicContaminationResistance", 0);
        resistance.put("TemperatureResistance", 0);
        resistance.put("LightingLevelResistance", 0);
        resistance.put("AcidityLevelResistance", 0);
        resistance.put("HumidityLevelResistance", 0);
        fillResistance(resistance, 100);
    }

    public boolean act(World world, GridElement gridElement) {
        System.out.println("call Cell.act => " + this);

        GenerateEnergy(world, gridElement);

        Map <String, Integer> factors = gridElement.getFactors();
        for (Map.Entry<String, Integer> entry : factors.entrySet())
            EnergyLevel -= CalculateEnergyConsumption(entry.getValue(), resistance.get(entry.getKey()+"Resistance"), factors.size());

        if (!IsAlive()){
            gridElement.DeleteCell();
            return false;
        }

        TransferEnergy(world, gridElement);

        MakeAction(world, gridElement);
        return true;
    }

    private void MakeAction(World world, GridElement gridElement){
        return;
    }

    private void TransferEnergy(World world, GridElement gridElement){
        return;
    }

    private int GenerateEnergy(World world, GridElement gridElement){
        return 0;
    }

    private void fillResistance(Map<String, Integer> resistance, int range) {
        int total = 0;
        int size = resistance.size();
        int[] values = new int[size];

        for (int i = 0; i < size; i++) {
            values[i] = random.nextInt(range - total - (size - i - 1));
            total += values[i];
        }

        values[size - 1] += (range - total);

        int index = 0;
        for (String key : resistance.keySet()) {
            resistance.put(key, values[index++]);
        }
    }

    private int CalculateEnergyConsumption (int factor, int resistance, int lenOfFactors){
        int result = Math.abs(factor) - (int)(((float)resistance*lenOfFactors)*0.5);
        return (result > 0) ? result : 0;
    }

    private boolean IsAlive (){
        return (EnergyLevel > 0);
    }

    public int getType (){
        return type;
    }

    public Color getColor (){
        return color;
    }

    public static ArrayList<String> getVisualizationTypes() {
        return VisualizationTypes;
    }

    public static void setVisualizationTypes(ArrayList<String> visualizationTypes) {
        VisualizationTypes = visualizationTypes;
    }
}
