package com.life_simulator.simulation_realization;

abstract public class Base {
    private int X;
    private int Y;

    public Base(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    abstract public boolean act();
}
