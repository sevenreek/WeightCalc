package com.devseven.gympack.setlogger;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Dickbutt on 03.06.2017.
 */

public class Weight {
    public static ArrayList<Weight> Weights;
    public Weight(double plateWeight, int amount)
    {
        this.plateWeight = plateWeight;
        this.amount = amount;
        if(Weights==null)
        {
            Weights = new ArrayList<Weight>();
        }
        Weights.add(this);
        SortWeights();
    }
    private double plateWeight;
    public double getPlateWeight(){
        return plateWeight;
    }
    public void setPlateWeight(double plateWeight){
        this.plateWeight = plateWeight;
    }
    private int amount;
    public int getPlateAmount(){
        return amount;
    }
    public void setPlateAmount(int amount){
        this.amount = amount;
    }
    private int timesUsed;
    public int getTimesUsed(){
        return timesUsed;
    }
    public void setTimesUsed(int timesUsed){
        this.timesUsed = timesUsed;
    }
    // TODO
    Drawable image;
    public void SortWeights()
    {
        Collections.sort(Weight.Weights, new Comparator<Weight>() {
            @Override
            public int compare(Weight o1, Weight o2) {
                return Double.compare(o2.plateWeight, o1.plateWeight);
            }
        });
    }


}
