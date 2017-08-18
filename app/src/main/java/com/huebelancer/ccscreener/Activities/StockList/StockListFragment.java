package com.huebelancer.ccscreener.Activities.StockList;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huebelancer.ccscreener.Coordinators.RootCoordinator;
import com.huebelancer.ccscreener.Dependencies.DependencyRegistry;
import com.huebelancer.ccscreener.Helpers.ActivityTitleCallback;
import com.huebelancer.ccscreener.Helpers.Filter;
import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;
import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;
import com.huebelancer.ccscreener.ModelLayer.ModelLayer;
import com.huebelancer.ccscreener.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.huebelancer.ccscreener.Helpers.Helpers.dateDisplay;


public class StockListFragment extends Fragment implements ModelLayer.LoadStocksCallback, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "StockListFragment";

    private StockListPresenter presenter;
    private RootCoordinator coordinator;
    private List<Stock> stocks = new ArrayList<>();
    private RecyclerView recyclerView;
    private BottomNavigationView bottomMenu;

    private View filterView;
    private SeekBar profit;
    private SeekBar probability;
    private TextView profitLabel;
    private TextView probabilityLabel;

    private Filter tempFilter;

    private ActivityTitleCallback titleCallback;

    public StockListFragment() {}

    public static StockListFragment newInstance() {
        StockListFragment fragment = new StockListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titleCallback = (StockListActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stock_list, container, false);

        attachUI(rootView);

        DependencyRegistry.shared.inject(this);

        return rootView;
    }

    public void configureWith(StockListPresenter presenter, RootCoordinator coordinator) {
        this.presenter = presenter;
        this.coordinator = coordinator;

        loadData();
    }

    private void attachUI(View root) {
        bottomMenu = (BottomNavigationView) root.findViewById(R.id.bottom_nav);
        bottomMenu.setOnNavigationItemSelectedListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        initializeListView();
    }


    private void initializeListView() {
        StockViewAdapter adapter = new StockViewAdapter(stocks, Frame.CURRENT_ITM, (v, position) -> rowTapped(v, position));
        recyclerView.setAdapter(adapter);
    }


    private void rowTapped(View v, int position) {
        Stock stock = stocks.get(position);
        goToStockDetails(stock.id);
    }

    private void goToStockDetails(String id) {
        coordinator.handleStockClick(
                (StockListActivity)getActivity(),
                id,
                R.id.contentLayout
        );
    }


    private void loadData() {
        presenter.loadData(this);
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
        //TODO: implement loading screen view
        //show loading screen
    }

    @Override
    public void onStocksFound(List<Stock> stocks) {
        //hide loading screen



        this.stocks = stocks;


        switch (presenter.filter.getFrame()) {
            case CURRENT_ITM:
                titleCallback.onNewTitle(
                        getString(R.string.frame_itm, getCurrentCallDate())
                );
                break;
            case CURRENT_OTM:
                titleCallback.onNewTitle(
                        getString(R.string.frame_otm, getCurrentCallDate())
                );
                break;
            case NEXT_ITM:
                titleCallback.onNewTitle(
                        getString(R.string.frame_itm, getNextCallDate())
                );
                break;
            case NEXT_OTM:
                titleCallback.onNewTitle(
                        getString(R.string.frame_otm, getNextCallDate())
                );
                break;
            default:
                titleCallback.onNewTitle(
                        getString(R.string.frame_itm, getCurrentCallDate())
                );
        }

        StockViewAdapter adapter = (StockViewAdapter) recyclerView.getAdapter();
        adapter.stocks = this.stocks;
        adapter.frame  = presenter.filter.getFrame();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStocksNotFound() {
        Toast.makeText(getActivity(), "No Stocks Found", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                showFilterDialog();
                break;
            case R.id.action_frame:
                showFrameDialog();
                break;
            case R.id.action_search:

                break;
        }
        return false;
    }

    //region Dialog Methods

    private void showFrameDialog() {
        CharSequence[] items = new CharSequence[] {
                getString(R.string.frame_itm, getCurrentCallDate()),
                getString(R.string.frame_otm, getCurrentCallDate()),
                getString(R.string.frame_itm, getNextCallDate()),
                getString(R.string.frame_otm, getNextCallDate())
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick a Frame")
                .setItems(items, (dialogInterface, i) -> {
                    Frame frame;
                    switch (i) {
                        case 0:
                            //CurrentITM
                            frame = Frame.CURRENT_ITM;
                            break;
                        case 1:
                            //CurrentOTM
                            frame = Frame.CURRENT_OTM;
                            break;
                        case 2:
                            //NextITM
                            frame = Frame.NEXT_ITM;
                            break;
                        case 3:
                            //NextOTM
                            frame = Frame.NEXT_OTM;
                            break;
                        default:
                            frame = Frame.CURRENT_ITM;
                    }
                    adjustTempFilterForFrame(frame);
                    presenter.setFilter(tempFilter);
                    dialogInterface.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showFilterDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        filterView = inflater.inflate(R.layout.filter, recyclerView, false);

        attachDialogUI();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Minimums")
                .setView(filterView)
                .setNeutralButton("Done", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    filterView = null;
                });

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(dialogInterface -> {
            Log.d(TAG, "setting up new filter: " + tempFilter.toString());
            presenter.setFilter(tempFilter);
            Log.d(TAG, "After setting: " + presenter.filter.toString());
        });
        dialog.show();
    }

    private void adjustTempFilterForSlider(int position, boolean isProfitSlider) {
        if (tempFilter == null) {
            tempFilter = new Filter();
            tempFilter.clone(presenter.filter);
        }

        if (isProfitSlider) {
            tempFilter.setProfit(position + 0.0);
        } else {
            tempFilter.setProbability(position + 0.0);
        }
    }

    private void adjustTempFilterForFrame(Frame frame) {
        if (tempFilter == null) {
            tempFilter = new Filter();
            tempFilter.clone(presenter.filter);
        }

        tempFilter.setFrame(frame);
    }

    private void attachDialogUI() {
        profit           = (SeekBar) filterView.findViewById(R.id.profit_slider);
        probability      = (SeekBar) filterView.findViewById(R.id.probability_slider);

        int filteredProfit      = (int)presenter.filter.getProfit();
        int filteredProbability = (int)presenter.filter.getProbability();

        profit.setProgress(filteredProfit);
        probability.setProgress(filteredProbability);

        profit.setOnSeekBarChangeListener(seekListener);
        probability.setOnSeekBarChangeListener(seekListener);

        Log.d(TAG, "Dialog creation " + presenter.filter.toString());

        profitLabel      = (TextView) filterView.findViewById(R.id.dialog_profit_label);
        probabilityLabel = (TextView) filterView.findViewById(R.id.dialog_probability_label);

        profitLabel.setText(filteredProfit + "%");
        probabilityLabel.setText(filteredProbability + "%");

        profit.setOnSeekBarChangeListener(seekListener);
        probability.setOnSeekBarChangeListener(seekListener);
    }

    //endregion

    //region Call Date Methods

    //TODO: refactor call date methods to helper class

    private String getCurrentCallDate() {
        Date date = new Date();

        if (stocks.size() > 2) {
            for (int i = 0; i < 3; i++) {
                Date current = stocks.get(i).currentCallDate;
                if (i == 0) {
                    date = current;
                } else {
                    if (current.before(date)) {
                        date = current;
                    }
                }
            }

        } else if (stocks.size() > 0) {
            date = stocks.get(0).currentCallDate;
        } else {
            return "Current Call -";
        }

        return dateDisplay(date, new SimpleDateFormat("MMM dd")) + " -";
    }

    private String getNextCallDate(){
        Date date = new Date();

        if (stocks.size() > 2) {
            for (int i = 0; i < 3; i++) {
                Date current = stocks.get(i).nextCallDate;
                if (i == 0) {
                    date = current;
                } else {
                    if (current.before(date)) {
                        date = current;
                    }
                }
            }

        } else if (stocks.size() > 0) {
            date = stocks.get(0).nextCallDate;
        } else {
            return "Next Call -";
        }

        return dateDisplay(date, new SimpleDateFormat("MMM dd")) + " -";
    }

    //endregion


    private SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            switch (seekBar.getId()) {
                case R.id.profit_slider:
                    profitLabel.setText(i+"%");
                    adjustTempFilterForSlider(i, true);
                    break;
                case R.id.probability_slider:
                    probabilityLabel.setText(i+"%");
                    adjustTempFilterForSlider(i, false);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };
}
