package com.life_simulator.simulation_realization;

import java.util.ArrayList;

import com.life_simulator.simulation_realization.Factors.Factors;

public class VisualizationOnTheField {
    private static String CurrentDisplayType = Cell.getVisualizationTypes().get(0);

    public static ArrayList<String> getTypeOfDisplay(){
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.addAll(Cell.getVisualizationTypes());
        tmp.addAll(Factors.getClassFactors());
        return tmp;
    }

    public static String getCurrentDisplayType() {
        return CurrentDisplayType;
    }

    public static Boolean setCurrentDisplayType(String currentDisplayType) {
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.addAll(Factors.getClassFactors());
        tmp.addAll(Cell.getVisualizationTypes());

        if (tmp.contains(currentDisplayType)){
            CurrentDisplayType = currentDisplayType;
            if (Factors.getClassFactors().contains(currentDisplayType))
                Factors.setCurrentFactor(currentDisplayType);
            return true;
        }

        return false;
    }
}
