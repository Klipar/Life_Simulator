package com.life_simulator.simulation_realization;

import javafx.scene.paint.Color;

public class Cell extends Base {
    private Color color = Color.ORANGE;
    private int type = 0;

    public Cell(Base base){
        super(base.getX(), base.getY());
    }

    public boolean act(World world) {
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
