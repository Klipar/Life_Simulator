package com.life_simulator.simulation_realization;

public class Base {
    private int X;
    private int Y;

    public Base(Base base) {
        this.X = base.getX();
        this.Y = base.getY();;
    }

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

    public void setBase (Base base){
        X = base.getX();
        Y = base.getY();
    }
    @Override
    public String toString() {
        return "(" + X + ", " + Y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Base myClass = (Base) obj;
        return this.X == myClass.getX() && this.Y == myClass.getY();
    }
}
