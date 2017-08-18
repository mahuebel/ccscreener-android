package com.huebelancer.ccscreener.ModelLayer.Database.Models;

import android.util.Log;

import com.huebelancer.ccscreener.Helpers.Constants;
import com.huebelancer.ccscreener.Helpers.Helpers;
import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.huebelancer.ccscreener.Helpers.Helpers.frameIsNext;
import static com.huebelancer.ccscreener.Helpers.Helpers.isUpToDate;

/**
 * Created by mahuebel on 8/14/17.
 */

public class Stock {
    private static final String TAG = "Stock";

    //region Fields

    public String id;
    public String symbol;
    public String exchange;
    public Double price;
    public Boolean isOption;

    public Map<String, Object> strikes;
    public Map<String, Object> asks;
    public Map<String, Object> bids;
    public Map<String, Object> premiums;
    public Map<String, Object> volatilities;
    public Map<String, Object> probabilities;
    public Map<String, Map<String, Object>> profits;

    public Date currentCallDate;
    public Date nextCallDate;
    public Date earningsRptDate;

    public Date priceUpdatedAt;
    public Date chainUpdatedAt;
    public Date earningsUpdatedAt;

    public Date createdAt;
    public Date updatedAt;

    //endregion

    //region Constructor Methods

    public Stock(String id, String symbol, String exchange, Double price,
                 Boolean isOption, Map<String, Object> strikes, Map<String, Object> asks,
                 Map<String, Object> bids, Map<String, Object> premiums,
                 Map<String, Object> volatilities, Map<String, Object> probabilities,
                 Map<String, Map<String, Object>> profits, Date currentCallDate,
                 Date nextCallDate, Date earningsRptDate, Date priceUpdatedAt,
                 Date chainUpdatedAt, Date earningsUpdatedAt,
                 Date createdAt, Date updatedAt) {
        this.id = id;
        this.symbol = symbol;
        this.exchange = exchange;
        this.price = price;
        this.isOption = isOption;
        this.strikes = strikes;
        this.asks = asks;
        this.bids = bids;
        this.premiums = premiums;
        this.volatilities = volatilities;
        this.probabilities = probabilities;
        this.profits = profits;
        this.currentCallDate = currentCallDate;
        this.nextCallDate = nextCallDate;
        this.earningsRptDate = earningsRptDate;
        this.priceUpdatedAt = priceUpdatedAt;
        this.chainUpdatedAt = chainUpdatedAt;
        this.earningsUpdatedAt = earningsUpdatedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //endregion

    //region Profit Methods

    public double minProfit(Frame frame) {
        String f = Helpers.frameToLabel(frame);
        Map<String, Object> profit = profits.get(f);
        try {
            return Double.valueOf(profit.get("min").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0d;
        }
    }

    public double maxProfit(Frame frame) {
        String f = Helpers.frameToLabel(frame);
        Map<String, Object> profit = profits.get(f);
        try {
            return Double.valueOf(profit.get("max").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0d;
        }
    }

    public double profitability(Frame frame) {
        return Math.min(minProfit(frame), maxProfit(frame));
    }

    //endregion

    //region Helpers

    public double probability(Frame frame) {
        String f = Helpers.frameToLabel(frame);
        try {
            Log.d(TAG, symbol + " " + f);
            Double probability = Double.valueOf(probabilities.get(f).toString());
            Log.d(TAG, symbol + " " + probability);
            return (Math.round(probability * 10000d) / 100d);
        } catch (Exception e) {
            e.printStackTrace();
            return 0d;
        }
    }

    public double volatility(Frame frame) {
        String f = Helpers.frameToLabel(frame);
        try {
            Double volatility = Double.valueOf(volatilities.get(f).toString());
            return (Math.round(volatility * 10000d) / 100d);
        } catch (Exception e) {
            e.printStackTrace();
            return 0d;
        }
    }

    public double strike(String frame) {
        try {
            return Math.round(Double.valueOf(strikes.get(frame).toString()) * 100d) / 100d;
        } catch (Exception e) {
            e.printStackTrace();
            return 0d;
        }
    }

    public double ask(String frame) {
        try {
            return Math.round(Double.valueOf(asks.get(frame).toString()) * 100d) / 100d;
        } catch (Exception e) {
            e.printStackTrace();
            return 0d;
        }
    }

    public double premium(String frame) {
        try {
            Log.d(TAG, premiums.get(frame).toString());
            return Math.round(Double.valueOf(premiums.get(frame).toString()) * 100d) / 100d;
        } catch (Exception e) {
            e.printStackTrace();
            return 0d;
        }
    }

    //endregion

    //region Date Methods

    public Date callDate(Frame frame) {
        switch (frame) {
            case CURRENT_ITM:
            case CURRENT_OTM:
                return this.currentCallDate;
            case NEXT_ITM:
            case NEXT_OTM:
                return this.nextCallDate;
            default:
                return this.currentCallDate;
        }
    }

    public boolean hasImpendingEarningsReport(Frame frame) {
        if (earningsRptDate != null) {
            if (frameIsNext(frame)) {
                return earningsRptDate.after(new Date())
                        && earningsRptDate.before(nextCallDate);
            } else {
                return earningsRptDate.after(new Date())
                        && earningsRptDate.before(currentCallDate);
            }
        }

        return false;
    }

    public boolean isAllUpToDate(Frame frame) {
        boolean price = isPriceUpToDate();
        boolean chain = isChainUpToDate(frame);

        return price && chain;
    }

    public boolean isPriceUpToDate() {
        if (priceUpdatedAt == null) {
            return false;
        } else {
            return isUpToDate(priceUpdatedAt);
        }
    }

    public boolean isChainUpToDate(Frame frame) {
        if (chainUpdatedAt == null){
            return false;
        } else {
            return isUpToDate(chainUpdatedAt) && isProfitUpToDate(frame);
        }
    }

    public boolean isProfitUpToDate(Frame frame) {
        return profits != null
                && profits.get(Helpers.frameToLabel(frame)) != null
                && profits.get(Helpers.frameToLabel(frame)).get(Constants.UPDATED_AT) != null
                && isUpToDate(
                    new Date(
                        ((LinkedHashMap<String, Long>)profits
                    .get(Helpers.frameToLabel(frame))
                        .get(Constants.UPDATED_AT))
                                .get("$date")
                    ));
    }

    //endregion


}
