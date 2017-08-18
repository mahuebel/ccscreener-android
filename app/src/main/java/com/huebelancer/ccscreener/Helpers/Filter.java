package com.huebelancer.ccscreener.Helpers;

import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;

/**
 * Created by mahuebel on 8/15/17.
 */

public class Filter {
    static Filter instance;
    Frame frame;
    double profit;
    double probability;
    //TODO: implement the following booleans into the filtering scheme
    boolean hideOutOfDate;
    boolean hideImpendingEarnings;

    public Filter(Frame frame, double profit, double probability) {
        this.frame = frame;
        this.profit = profit;
        this.probability = probability;
    }

    public Filter() {}

    public static Filter getInstance() {
        if (null == instance)
            instance = new Filter(Frame.CURRENT_ITM, 1.0, 50.0);

        return instance;
    }

    //region Getters

    public Frame getFrame() {
        return frame;
    }

    public double getProfit() {
        return profit;
    }

    public double getProbability() {
        return probability;
    }



    //endregion

    //region Setters

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    //endregion

    public void clone(Filter filter) {
        this.profit      = filter.profit;
        this.frame       = filter.frame;
        this.probability = filter.probability;
    }

    @Override
    public String toString() {
        return "Frame: " + Helpers.frameToLabel(frame)
                + " Profit: " + profit
                + " Probability: " + probability;
    }
}
