package com.life_simulator.simulation_realization;

import com.life_simulator.simulation_realization.Factors.Factors;

import javafx.scene.paint.Color;

public class FactorsColorsProcessor {
    private Color colorLow = Color.rgb(0, 0, 255);
    private Color colorMiddle = Color.rgb(255, 255, 255);
    private Color colorHeight = Color.rgb(255, 0, 0);

    public FactorsColorsProcessor(){
        return;
    }


    public Color GetColorFromValue(int inp){
        if (inp == 0) return this.colorMiddle;
        if (inp > 0)
            return Color.rgb(getPortionInt((int)(colorMiddle.getRed()   * 255), (int)(colorHeight.getRed()   * 255), inp),
                             getPortionInt((int)(colorMiddle.getGreen() * 255), (int)(colorHeight.getGreen() * 255), inp),
                             getPortionInt((int)(colorMiddle.getBlue()  * 255), (int)(colorHeight.getBlue()  * 255), inp));
        else
            return Color.rgb(getPortionInt((int)(colorMiddle.getRed()   * 255), (int)(colorLow.getRed()   * 255), inp),
                             getPortionInt((int)(colorMiddle.getGreen() * 255), (int)(colorLow.getGreen() * 255), inp),
                             getPortionInt((int)(colorMiddle.getBlue()  * 255), (int)(colorLow.getBlue()  * 255), inp));
    }

    private int getPortionInt(int from, int to, int portion){
        return (int)((from-to)/Factors.LimitFactorValues)+to;
    }



    public Color getColorMiddle() {
        return colorMiddle;
    }
    public void setColorMiddle(Color colorMiddle) {
        this.colorMiddle = colorMiddle;
    }
    public Color getColorLow() {
        return colorLow;
    }
    public void setColorLow(Color colorLow) {
        this.colorLow = colorLow;
    }
    public Color getColorHeight() {
        return colorHeight;
    }
    public void setColorHeight(Color colorHeight) {
        this.colorHeight = colorHeight;
    }
}
