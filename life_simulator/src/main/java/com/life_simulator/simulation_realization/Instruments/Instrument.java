package com.life_simulator.simulation_realization.Instruments;

import java.util.ArrayList;
import java.util.Arrays;

import com.life_simulator.simulation_realization.Base;
import com.life_simulator.simulation_realization.Diffusion;
import com.life_simulator.simulation_realization.GridElement;
import com.life_simulator.simulation_realization.World;
import com.life_simulator.simulation_realization.Factors.Factors;

public abstract class Instrument {
    static private ArrayList<String> instruments = new ArrayList<>(Arrays.asList("Cell selector", "Mass cell work", "Working with factors"));

    private static String currentInstrument = instruments.get(0);
    private static int instrumentBrushSize = 1;
    private static int instrumentBrushPressure = 10;

    public static final int brushSizeFactor = 5;

    public static ArrayList<String> getInstruments() {
        return instruments;
    }

    public static int getInstrumentBrushPressure() {
        return instrumentBrushPressure;
    }

    public static void setInstrumentBrushPressure(int instrumentBrushPressure) {
        Instrument.instrumentBrushPressure = instrumentBrushPressure;
    }

    public static void setInstruments(ArrayList<String> instruments) {
        Instrument.instruments = instruments;
    }

    public static String getCurrentInstrument() {
        return currentInstrument;
    }

    public static void setCurrentInstrument(String currentInstrument) {
        Instrument.currentInstrument = currentInstrument;
    }

    public static int getInstrumentBrushSize() {
        return instrumentBrushSize;
    }

    public static void setInstrumentBrushSize(int instrumentBrushSize) {
        Instrument.instrumentBrushSize = instrumentBrushSize;
        System.out.println("new brush size = " + Instrument.instrumentBrushSize);
    }

    public static boolean act(World world, Base base, boolean inverse){
        if (base.getX() < 0 || base.getY() < 0) return false;
        int inversion = (inverse) ? -1 : 1;
        // world.getGridElement(base).adjustFactorBy(currentInstrument, instrumentBrushPressure*inversion);
        if (currentInstrument == instruments.get(0)){
            return true;
        }

        for (Base tmpBase : Diffusion.Difuse(base, instrumentBrushSize)) {
            GridElement gridElement = world.getGridElement(tmpBase);
            if (gridElement != null){
                if (currentInstrument == instruments.get(2)){
                    gridElement.adjustFactorBy(Factors.getCurrentFactor(), instrumentBrushPressure*inversion);
                }
            }
        }
        return true;
    }

}
