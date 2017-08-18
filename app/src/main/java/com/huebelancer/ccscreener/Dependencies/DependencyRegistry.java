package com.huebelancer.ccscreener.Dependencies;

import android.os.Bundle;

import com.huebelancer.ccscreener.Activities.StockDetails.StockDetailsFragment;
import com.huebelancer.ccscreener.Activities.StockDetails.StockDetailsPresenter;
import com.huebelancer.ccscreener.Activities.StockList.StockListFragment;
import com.huebelancer.ccscreener.Activities.StockList.StockListPresenter;
import com.huebelancer.ccscreener.Coordinators.RootCoordinator;
import com.huebelancer.ccscreener.Helpers.Constants;
import com.huebelancer.ccscreener.ModelLayer.ModelLayer;
import com.huebelancer.ccscreener.ModelLayer.Network.NetworkLayer;
import com.huebelancer.ccscreener.ModelLayer.Translation.StockTranslator;
import com.huebelancer.ccscreener.ModelLayer.Translation.TranslationLayer;

import java.util.NoSuchElementException;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorSingleton;

/**
 * Created by mahuebel on 8/14/17.
 */

public class DependencyRegistry {
    public static DependencyRegistry shared = new DependencyRegistry();

    private Meteor meteor = MeteorSingleton.getInstance();

    //region Coordinators

    public RootCoordinator rootCoordinator = new RootCoordinator();

    //endregion

    //region Singletons

    public StockTranslator stockTranslator = new StockTranslator();

    public TranslationLayer translationLayer = createTranslationLayer();
    private TranslationLayer createTranslationLayer() {
        return new TranslationLayer(stockTranslator);
    }

    public NetworkLayer networkLayer = new NetworkLayer(meteor);

    public ModelLayer modelLayer = createModelLayer();
    private ModelLayer createModelLayer() {
        return new ModelLayer(networkLayer,translationLayer);
    }

    //endregion

    //region Injection Methods

    public void inject(StockListFragment fragment) {
        StockListPresenter presenter = new StockListPresenter(modelLayer, fragment);
        fragment.configureWith(presenter, rootCoordinator);
    }

    public void inject(StockDetailsFragment fragment, String id) {
        StockDetailsPresenter presenter = new StockDetailsPresenter(modelLayer, id);
        fragment.configureWith(presenter, rootCoordinator);
    }

    //endregion

    //region Helpers

    private int idFromBundle(Bundle bundle) {
        if (bundle == null) throw new NoSuchElementException("Unable to get stock id from bundle");

        int stockId = bundle.getInt(Constants.stockIdKey);
        if (stockId == 0) throw new NoSuchElementException("Unable to get stock id from bundle");
        return stockId;
    }

    //endregion

}
