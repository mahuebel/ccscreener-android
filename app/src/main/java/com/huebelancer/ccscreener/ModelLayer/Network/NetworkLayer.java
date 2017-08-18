package com.huebelancer.ccscreener.ModelLayer.Network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.huebelancer.ccscreener.Helpers.Constants;
import com.huebelancer.ccscreener.Helpers.Filter;
import com.huebelancer.ccscreener.Helpers.Helpers;

import java.util.HashMap;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.SubscribeListener;
import im.delight.android.ddp.db.Document;
import im.delight.android.ddp.db.Query;

import static com.huebelancer.ccscreener.Helpers.Helpers.frameToLabel;


/**
 * Created by mahuebel on 8/14/17.
 */

public class NetworkLayer implements MeteorCallback {

    private static final String TAG = "NetworkLayer";



    public interface LoadDocumentsCallback {
        void onDocumentsLoaded(Document[] docs);
        void onDataNotAvailable();
    }

    public interface LoadDocumentCallback {
        void onDocumentLoaded(Document doc);
        void onDataNotAvailable();
    }



    private Meteor meteor;
    private LoadDocumentsCallback docsCallback = null;
    private LoadDocumentCallback docCallback = null;
    private String docId;
    private Filter filter = null;


    public NetworkLayer(Meteor meteor) {
        this.meteor = meteor;
        this.meteor.addCallback(this);
    }


    public void getStocks(@NonNull LoadDocumentsCallback callback, Filter filter) {
        if (checkConnection()) {
            String frameLabel = frameToLabel(filter.getFrame());

            Map<String, String> frame = new HashMap<>();
            frame.put("frame", frameLabel);

            meteor.subscribe(
                Constants.SUBSCRIBE_OPTIONABLE,
                new Object[]{frame},
                new SubscribeListener() {
                    @Override
                    public void onSuccess() {
                        String PROBABILITY = "probabilities." + frameLabel;

                        Query query = meteor.getDatabase()
                                .getCollection(Constants.COLLECTION_STOCKS)
                                .whereNotNull(Constants.FIELD_CURRENT_CALL_DATE)
                                .whereNotNull(Constants.FIELD_ASKS)
                                .whereNotNull(Constants.FIELD_BIDS)
                                .whereNotNull(Constants.FIELD_STRIKES)
                                .whereGreaterThan(Constants.FIELD_PRICE, 2.5)
                                .whereLessThan(Constants.FIELD_PRICE, 75d)
                                .whereEqual(Constants.FIELD_IS_OPTION, true);

                        Document[] docs = query.find(100);

                        if (docs == null) {
                            Log.d(TAG, "docs is null");
                            callback.onDataNotAvailable();
                        } else if (docs.length == 0) {
                            Log.d(TAG, "docs is empty");
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDocumentsLoaded(query.find());
                        }
                        docsCallback = null;
                    }

                    @Override
                    public void onError(String error, String reason, String details) {
                        Log.e(TAG, error);
                        Log.e(TAG, reason);
                        Log.e(TAG, details);
                    }
                }
            );
        } else {
            this.filter  = filter;
            docsCallback = callback;
        }
    }

    public void getStock(String id, LoadDocumentCallback callback) {
        if (checkConnection()){
            Document document = meteor.getDatabase()
                    .getCollection(Constants.COLLECTION_STOCKS)
                    .getDocument(id);

            if (document == null) {
                callback.onDataNotAvailable();
            } else {
                callback.onDocumentLoaded(document);
            }
            docCallback = null;
        } else {
            docId = id;
            docCallback = callback;
        }

    }



    private Boolean checkConnection() {
        if (!meteor.isConnected()) {
            meteor.connect();
            Log.d(TAG, "connecting to Meteor server");
            return false;
        } else {
            return true;
        }
    }

    //region Helper methods



    //endregion


    //region Meteor callbacks

    @Override
    public void onConnect(boolean signedInAutomatically) {
        Log.d(TAG, "connected to Meteor server");
        if (docsCallback != null) {
            getStocks(docsCallback, filter);
        }
        if (docCallback != null) {
            getStock(docId, docCallback);
        }
    }

    @Override
    public void onDisconnect() {
        Log.d(TAG, "disconnected from Meteor server");
    }

    @Override
    public void onException(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
//        Log.d(TAG, collectionName + " onDataAdded " + newValuesJson);
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
//        Log.d(TAG, collectionName + " onDataChanged " + updatedValuesJson);
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }

    //endregion

}
