package com.huebelancer.ccscreener.Activities.StockList;

import com.huebelancer.ccscreener.Helpers.Filter;
import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;
import com.huebelancer.ccscreener.ModelLayer.ModelLayer;

import im.delight.android.ddp.db.Document;

/**
 * Created by mahuebel on 8/14/17.
 */

public class StockListPresenter {
    private static final String TAG = "StockListPresenter";

    ModelLayer modelLayer;
    Filter filter;
    ModelLayer.LoadStocksCallback callback;

    public StockListPresenter(ModelLayer modelLayer, ModelLayer.LoadStocksCallback callback) {
        this.modelLayer = modelLayer;
        this.callback   = callback;
        filter = Filter.getInstance();
    }

    public void loadData(ModelLayer.LoadStocksCallback callback) {
        showLoading(callback);
        modelLayer.loadStocks(callback, filter);
    }

    public void showLoading(ModelLayer.LoadStocksCallback callback) {
        callback.onLoading();
    }

    public void changeFrame(Frame frame) {
        this.filter.setFrame(frame);
        loadData(callback);
    }

    public void setFilter(Filter filter) {
        this.filter.clone(filter);
        loadData(callback);
    }

    public Filter getFilter() {
        return filter;
    }
}
