package com.huebelancer.ccscreener.Activities.StockDetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huebelancer.ccscreener.Activities.StockList.StockListActivity;
import com.huebelancer.ccscreener.Coordinators.RootCoordinator;
import com.huebelancer.ccscreener.Dependencies.DependencyRegistry;
import com.huebelancer.ccscreener.Helpers.ActivityTitleCallback;
import com.huebelancer.ccscreener.Helpers.Helpers;
import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;
import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;
import com.huebelancer.ccscreener.ModelLayer.ModelLayer;
import com.huebelancer.ccscreener.R;

import java.util.ArrayList;
import java.util.List;


public class StockDetailsFragment extends Fragment implements ModelLayer.LoadStockCallback {
    private static final String KEY_STOCK_ID = "stock_id";
    private static final String TAG = "StockDetailsFragment";


    private RecyclerView recyclerView;
    private TextView symbol, price, updated, priceUpdated, optionsUpdated, earnings;

    private StockDetailsPresenter presenter;
    private RootCoordinator coordinator;

    private String stockId;
    private Stock stock;
    private List<String> frames;

    private ActivityTitleCallback callback;

    public StockDetailsFragment() {}

    public static StockDetailsFragment newInstance(String id) {
        StockDetailsFragment fragment = new StockDetailsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_STOCK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stockId = getArguments().getString(KEY_STOCK_ID);
            Log.d(TAG, stockId);
        }

        callback = (StockListActivity)getActivity();
        frames = getFrames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_stock_details, container, false);

        attachUI(root);

        DependencyRegistry.shared.inject(this, stockId);

        return root;
    }

    public void configureWith(StockDetailsPresenter presenter, RootCoordinator coordinator) {
        this.presenter = presenter;
        this.coordinator = coordinator;
        loadData();
    }

    private void loadData() {
        presenter.loadData(this);
    }

    private void attachUI(View root) {

        //region Attach TextViews

        symbol         = (TextView) root.findViewById(R.id.details_symbol);
        price          = (TextView) root.findViewById(R.id.details_price);
        updated        = (TextView) root.findViewById(R.id.details_updated);
        priceUpdated   = (TextView) root.findViewById(R.id.details_price_updated);
        optionsUpdated = (TextView) root.findViewById(R.id.details_options_updated);
        earnings       = (TextView) root.findViewById(R.id.details_earnings);

        //endregion

        //region Attach RecyclerView

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        initializeListView();

        //endregion

    }

    private void initializeListView() {
        DetailViewAdapter adapter = new DetailViewAdapter(stock, frames);
        recyclerView.setAdapter(adapter);
    }

    private List<String> getFrames() {
        List<String> frames = new ArrayList<>();

        for (Frame frame : Frame.values()) {
            frames.add(Helpers.frameToLabel(frame));
        }

        return frames;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLoading() {
        //show loading screen
    }

    @Override
    public void onStockFound(Stock stock) {

        //hide loading screen

        callback.onNewTitle(stock.symbol);
        updateTextViews(stock);
        updateListView(stock);
    }

    //region Update UI methods

    private void updateTextViews(Stock stock) {
        Log.d(TAG, "updating TextViews");
        symbol.setText(stock.symbol);
        price.setText(String.valueOf(stock.price));
        updated.setText(
                DateUtils.getRelativeTimeSpanString(
                        stock.updatedAt.getTime()
                )
        );
        priceUpdated.setText(
                DateUtils.getRelativeTimeSpanString(
                        stock.priceUpdatedAt.getTime()
                )
        );
        optionsUpdated.setText(
                DateUtils.getRelativeTimeSpanString(
                        stock.chainUpdatedAt.getTime()
                )
        );
        if (stock.earningsRptDate != null)
            earnings.setText(
                    DateUtils.getRelativeTimeSpanString(
                            stock.earningsRptDate.getTime()
                    )
            );
    }

    private void updateListView(Stock stock) {
        this.stock = stock;
        DetailViewAdapter adapter = (DetailViewAdapter) recyclerView.getAdapter();
        adapter.stock = this.stock;
        adapter.notifyDataSetChanged();
    }

    //endregion

    @Override
    public void onStockNotFound() {
        Toast.makeText(getActivity(), "No Stock Found", Toast.LENGTH_LONG).show();
    }
}
