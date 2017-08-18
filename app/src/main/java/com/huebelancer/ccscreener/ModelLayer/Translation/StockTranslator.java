package com.huebelancer.ccscreener.ModelLayer.Translation;

import android.util.Log;

import com.huebelancer.ccscreener.Helpers.Constants;
import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.ddp.db.Document;

/**
 * Created by mahuebel on 8/14/17.
 */

public class StockTranslator {

    private static final String TAG = "StockTranslator";

    public StockTranslator() {

    }


    public List<Stock> translate(Document[] docs) {
        List<Stock> stocks = new ArrayList<>();
        Log.d(TAG, docs.length + " stocks to translate");
        for (Document from : docs) {
            Stock stock = translate(from);

            if (stock != null)
                stocks.add(stock);
        }

        return stocks;
    }



    public Stock translate(Document from) {
        try {

            String id, symbol, exchange;
            Double price;
            Boolean isOption;

            Map<String, Object> strikes, asks, bids,
                    premiums, volatilities, probabilities;

            Map<String, Map<String, Object>> profits;

            Date currentCallDate, nextCallDate, earningsRptDate,
                    priceUpdatedAt, chainUpdatedAt, earningsUpdatedAt,
                    createdAt, updatedAt;

            id = from.getId();
            symbol = from.getField(Constants.FIELD_SYMBOL).toString();
            exchange = from.getField(Constants.FIELD_EXCHANGE).toString();
            price =  Double.valueOf(from.getField(Constants.FIELD_PRICE) + "");
            isOption = (Boolean) from.getField(Constants.FIELD_IS_OPTION);

            strikes       = (Map<String, Object>) from.getField(Constants.FIELD_STRIKES);
            asks          = (Map<String, Object>) from.getField(Constants.FIELD_ASKS);
            bids          = (Map<String, Object>) from.getField(Constants.FIELD_BIDS);
            premiums      = (Map<String, Object>) from.getField(Constants.FIELD_PREMIUMS);
            volatilities  = (Map<String, Object>) from.getField(Constants.FIELD_VOLATILITIES);
            probabilities = (Map<String, Object>) from.getField(Constants.FIELD_PROBABILITIES);

            profits = (Map<String, Map<String, Object>>) from.getField(Constants.FIELD_PROFITS);

            LinkedHashMap<String, Long> current, next, earnings, priceUp, chainUp,
                    earningsUp, created, updated;

            current    = (LinkedHashMap<String, Long>) from.getField(Constants.FIELD_CURRENT_CALL_DATE);
            next       = (LinkedHashMap<String, Long>) from.getField(Constants.FIELD_NEXT_CALL_DATE);
            earnings   = (LinkedHashMap<String, Long>) from.getField(Constants.FIELD_EARNINGS_RPT_DATE);
            priceUp    = (LinkedHashMap<String, Long>) from.getField(Constants.FIELD_PRICE_UPDATED);
            chainUp    = (LinkedHashMap<String, Long>) from.getField(Constants.FIELD_CHAIN_UPDATED);
            earningsUp = (LinkedHashMap<String, Long>) from.getField(Constants.FIELD_EARNINGS_UPDATED);
            created    = (LinkedHashMap<String, Long>) from.getField(Constants.CREATED_AT);
            updated    = (LinkedHashMap<String, Long>) from.getField(Constants.UPDATED_AT);


            currentCallDate   = new Date(current.get("$date"));
            nextCallDate      = new Date(next.get("$date"));
            earningsRptDate   = earnings != null ? new Date(earnings.get("$date")) : null;
            priceUpdatedAt    = new Date(priceUp.get("$date"));
            chainUpdatedAt    = new Date(chainUp.get("$date"));
            earningsUpdatedAt = earningsUp != null ? new Date(earningsUp.get("$date")) : null;
            createdAt         = new Date(created.get("$date"));
            updatedAt         = new Date(updated.get("$date"));


            return new Stock(
                    id, symbol, exchange, price, isOption,
                    strikes, asks, bids, premiums,
                    volatilities, probabilities, profits,
                    currentCallDate, nextCallDate, earningsRptDate,
                    priceUpdatedAt, chainUpdatedAt, earningsUpdatedAt,
                    createdAt, updatedAt
            );
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, from.toString());
            return null;
        }
    }

}
