package com.life_simulator.simulation_realization.Factors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.life_simulator.simulation_realization.FactorsColorsProcessor;

public class Factors {
    static private ArrayList<Factors> instances = new ArrayList<>();

    public static final short LimitFactorValues = 100;

    static private ArrayList<String> factors = new ArrayList<>();
    static private String CurrentFactor;
    static private Map <String, FactorsColorsProcessor> factorsAndColors = new HashMap<>();

    private Map <String, Integer> factorsAndValues = new HashMap<>();

    public Factors(){
        Factors.instances.add(this);
        for (String factor : factors) {
            this.AddFactor(factor, 0);
        }
    }

    private boolean RemoveFactor (String key){
        if (factorsAndValues.containsKey(key)) {
            factorsAndValues.remove(key);
            return true;
        } else
            return false;
    }

    static private boolean RemoveColor (String key){
        if (factorsAndColors.containsKey(key)) {
            factorsAndColors.remove(key);
            return true;
        } else
            return false;
    }

    private boolean AddFactor (String key, int value){
        if (!factorsAndValues.containsKey(key)) {
            factorsAndValues.put(key, value);
            return true;
        } else
            return false;
    }

    private static boolean AddColor (String key){
        if (!factorsAndColors.containsKey(key)) {
            factorsAndColors.put(key, new FactorsColorsProcessor());
            return true;
        } else
            return false;
    }

    public Map<String, Integer> getFactorsAndValues() {
        return factorsAndValues;
    }

    public static Map<String, FactorsColorsProcessor> getFactorsAndColors() {
        return factorsAndColors;
    }

    public FactorsColorsProcessor getFactorColor(String factor){
        if (factor == null) {
            System.err.println("factor == null");
            return null;
        }
        return factorsAndColors.get(factor);
    }

    public int getFactorValue(String factor){
        if (factor == null) {
            System.err.println("factor == null");
            return 0;
        }
        return factorsAndValues.get(factor);
    }

    public void adjustFactorBy(String factor, int value){
        if ((factorsAndValues.get(factor) + value) > 100) {
            factorsAndValues.put(factor, 100);
            return;
        } else if ((factorsAndValues.get(factor) + value) < -100){
            factorsAndValues.put(factor, -100);
            return;
        }
        this.setFactorValue(factor, this.getFactorValue(factor) + value);
    }

    public void setFactorValue(String factor, int baseValue){
        if (baseValue > 100 || baseValue < -100){
            System.err.println("baseValue > 100 || baseValue < -100");
            return;
        }
        factorsAndValues.put(factor, baseValue);
    }

    public static void setClassFactors (ArrayList<String> updateFactors){
        Factors.setClassFactors(updateFactors, 0);
    }

    public static void setClassFactors (ArrayList<String> updateFactors, int newFactorValue){
        ArrayList<String> FactorsToDelete = new ArrayList<>(Factors.factors);
        FactorsToDelete.removeAll(updateFactors);

        for (String FactorToDelete : FactorsToDelete) {
            for (Factors instance : instances) {
                instance.RemoveFactor(FactorToDelete);
            }
            Factors.RemoveColor(FactorToDelete);
        }

        ArrayList<String> FactorsToAdd = new ArrayList<>(updateFactors);
        FactorsToAdd.removeAll(Factors.factors);

        for (String FactorToAdd : FactorsToAdd) {
            for (Factors instance : instances) {
                instance.AddFactor(FactorToAdd, newFactorValue);
            }
            Factors.AddColor(FactorToAdd);
        }
        Factors.factors = updateFactors;

    }

    public static ArrayList<String> getClassFactors (){
        return factors;
    }

    public static String getCurrentFactor() {
        return CurrentFactor;
    }

    public static boolean setCurrentFactor(String factor) {
        if (factors.contains(factor)){
            Factors.CurrentFactor = factor;
            return true;
        } else
            return false;
    }
}
