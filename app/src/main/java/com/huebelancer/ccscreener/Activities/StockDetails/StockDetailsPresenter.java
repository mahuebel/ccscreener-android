package com.huebelancer.ccscreener.Activities.StockDetails;

import com.huebelancer.ccscreener.ModelLayer.ModelLayer;

/**
 * Created by mahuebel on 8/15/17.
 */

public class StockDetailsPresenter {

    ModelLayer modelLayer;
    String stockId;

    public StockDetailsPresenter(ModelLayer modelLayer, String stockId) {
        this.modelLayer = modelLayer;
        this.stockId = stockId;
    }

    public void loadData(ModelLayer.LoadStockCallback callback) {
        modelLayer.loadStockById(stockId, callback);
    }
}
