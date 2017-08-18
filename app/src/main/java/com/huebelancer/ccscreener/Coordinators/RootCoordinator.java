package com.huebelancer.ccscreener.Coordinators;

import com.huebelancer.ccscreener.Activities.StockDetails.StockDetailsFragment;
import com.huebelancer.ccscreener.Activities.StockList.StockListActivity;
import com.huebelancer.ccscreener.Helpers.Helpers;

/**
 * Created by mahuebel on 8/14/17.
 */

public class RootCoordinator {
    public void handleStockClick(StockListActivity activity, String id, int frameId) {
        StockDetailsFragment fragment = StockDetailsFragment.newInstance(id);
        Helpers.replaceFragmentInActivity(
                activity.getSupportFragmentManager(),
                fragment,
                frameId
        );
    }
}
