package com.life_simulator.simulation_realization;

import java.util.ArrayList;

public abstract class Diffusion {
    public static ArrayList<Base> Difuse(Base base, int rad){
        if (base == null || rad < 0){
            System.err.println("base == null or rad < 1 !!!");
            return null;
        }

        ArrayList<Base> ret = new ArrayList<Base>();
        Base tmp = new Base (base.getX()-rad, base.getY()-rad);
        base.setX(base.getX()+rad);
        base.setY(base.getY()+rad);

        while (tmp.getX() <= base.getX()){
            while (tmp.getY() <= base.getY()){
                if (tmp.getX() >= 0 && tmp.getY() >= 0)
                    ret.add(new Base(tmp));
                tmp.setY(tmp.getY()+1);
            }
            tmp.setY(base.getY()-(2*rad));
            tmp.setX(tmp.getX()+1);
        }
        return ret;
    }
}
