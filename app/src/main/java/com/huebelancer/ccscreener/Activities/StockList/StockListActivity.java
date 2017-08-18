package com.huebelancer.ccscreener.Activities.StockList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huebelancer.ccscreener.Helpers.ActivityTitleCallback;
import com.huebelancer.ccscreener.R;

import static com.huebelancer.ccscreener.Helpers.Helpers.addFragmentToActivity;

public class StockListActivity extends AppCompatActivity implements ActivityTitleCallback { //implements ModelLayer.LoadStocksCallback {

    private static final String TAG = "StockListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        StockListFragment fragment =
                (StockListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contentLayout);
        if (fragment == null) {
            fragment = StockListFragment.newInstance();
            addFragmentToActivity(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.contentLayout
                    );
        }
    }

    @Override
    public void onNewTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
