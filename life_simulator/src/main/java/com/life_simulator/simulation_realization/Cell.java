package com.life_simulator.simulation_realization;

import javafx.scene.paint.Color;

public class Cell extends Base {
    private Color color = Color.ORANGE;
    private int type = 0;

    public Cell(int X, int Y){
        super(X, Y);
    }

    public boolean act() {
        System.out.println("call Cell.act");
        return true;
    }

    public int getType (){
        return type;
    }

    public Color getColor (){
        return color;
    }
}
