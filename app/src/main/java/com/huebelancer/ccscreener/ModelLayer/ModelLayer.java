package com.huebelancer.ccscreener.ModelLayer;

import android.util.Log;

import com.huebelancer.ccscreener.Helpers.Filter;
import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;
import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;
import com.huebelancer.ccscreener.ModelLayer.Network.NetworkLayer;
import com.huebelancer.ccscreener.ModelLayer.Translation.TranslationLayer;

import java.util.Iterator;
import java.util.List;

import im.delight.android.ddp.db.Document;

/**
 * Created by mahuebel on 8/14/17.
 */

public class ModelLayer {

    private static final String TAG = "ModelLayer";


    public interface LoadStocksCallback {
        void onLoading();
        void onStocksFound(List<Stock> stocks);
        void onStocksNotFound();
    }

    public interface LoadStockCallback {
        void onLoading();
        void onStockFound(Stock stock);
        void onStockNotFound();
    }


    private NetworkLayer networkLayer;
    private TranslationLayer translationLayer;

    public ModelLayer(NetworkLayer networkLayer, TranslationLayer translationLayer) {
        this.networkLayer = networkLayer;
        this.translationLayer = translationLayer;
    }

    public void loadStocks(final LoadStocksCallback callback, Filter filter) {

        networkLayer.getStocks(new NetworkLayer.LoadDocumentsCallback() {
            @Override
            public void onDocumentsLoaded(Document[] docs) {

                //TODO: Put translation and filtering on a background thread
                List<Stock> stocks = translationLayer.translate(docs);

                Iterator<Stock> it = stocks.iterator();
                while( it.hasNext() ) {
                    Stock stock = it.next();
                    if(!stockMeetsFilter(stock, filter) ) it.remove();
                }

                callback.onStocksFound(stocks);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "No data found for query");
                callback.onStocksNotFound();
            }
        }, filter);
    }

    private boolean stockMeetsFilter(Stock stock, Filter filter) {
        Frame frame = filter.getFrame();

        Boolean isOverProfitFilter =
                stock.profitability(frame) >= filter.getProfit();

        Boolean isOverProbabilityFilter =
                stock.probability(frame) >= filter.getProbability();

        return isOverProbabilityFilter && isOverProfitFilter;
    }

    public void loadStockById(final String id, final LoadStockCallback callback) {
        networkLayer.getStock(id, new NetworkLayer.LoadDocumentCallback() {
            @Override
            public void onDocumentLoaded(Document doc) {
                callback.onStockFound(translationLayer.translate(doc));
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "No data found for this id: " + id);
                callback.onStockNotFound();
            }
        });
    }

}
